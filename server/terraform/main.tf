variable "project_id" {
  default = "apass-12345"
}
variable "service_name" {
  default = "api-service"
}
variable "database_password" {
  default = "209389207402"
}
variable "database_user" {
  default = "sttawm"
}
variable "database_name" {
  default = "my-database"
}

terraform {
  required_version = ">= 0.14"

  required_providers {
    # Cloud Run support was added on 3.3.0
    google = ">= 3.3"
  }
}

provider "google" {
  project = var.project_id
}

# Create a postgres instance
resource "google_sql_database_instance" "instance" {
  name                = "postgres"
  region              = "us-central1"
  database_version    = "POSTGRES_13"
  deletion_protection = true
  settings {
    tier = "db-f1-micro"
  }
}

# Create a database
resource "google_sql_database" "database" {
  name     = var.database_name
  instance = google_sql_database_instance.instance.name
}

# Create a database user
resource "google_sql_user" "database-user" {
  name = var.database_user
  instance = google_sql_database_instance.instance.name
  password = var.database_password
}

# Enables the Cloud Run API
resource "google_project_service" "run_api" {
  service            = "run.googleapis.com"

}

resource "google_cloud_run_service" "default" {
  name     = var.service_name
  location = "us-central1"
  template {
    spec {
      containers {
        image = format("gcr.io/%s/%s:b34b028aa44fa7bf08c9a5989e623c60df2627f5", var.project_id, var.service_name)
        ports {
          container_port = 9000
        }
        env {
          name  = "APP_CONF_FILE"
          value = "application.conf"
        }
        env {
          name  = "APP_SECRET_KEY"
          value = "Sjgu+U5wK4uTrRKyrRq1DyWiolpPXrBV44tcM8im4jI="
        }
        env {
          name  = "DB_HOST"
          value = "/cloudsql/${google_sql_database_instance.instance.connection_name}"
        }
        env {
          name  = "DB_DATABASE"
          value = var.database_name
        }
        env {
          name  = "DB_USERNAME"
          value = var.database_user
        }
        env {
          name  = "DB_PASSWORD"
          value = var.database_password
        }
      }
    }
    metadata {
      annotations = {
        "autoscaling.knative.dev/minScale" = "0"
        "autoscaling.knative.dev/maxScale" = "1"
      }
    }
  }
  traffic {
    percent         = 100
    latest_revision = true
  }

  # Waits for the Cloud Run API to be enabled
  depends_on = [google_project_service.run_api]
}

# Allow unauthenticated users to invoke the service
resource "google_cloud_run_service_iam_member" "run_all_users" {
  service  = google_cloud_run_service.default.name
  location = google_cloud_run_service.default.location
  role     = "roles/run.invoker"
  member   = "allUsers"
}

# Display the service URL
output "service_url" {
  value = google_cloud_run_service.default.status[0].url
}
