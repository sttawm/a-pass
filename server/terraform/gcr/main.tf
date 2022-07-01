variable "project_id" {
  default = "apass-4"
}

provider "google" {
  project = var.project_id
}

# Enables the IAM API
resource "google_project_service" "iam_api" {
  service            = "iam.googleapis.com"
}

# Enables the GCR API
resource "google_project_service" "gcr_api" {
  service            = "containerregistry.googleapis.com"
}

//resource "google_container_registry" "registry" {
//  project  = var.project_id
//}

//resource "google_iam_workload_identity_pool" "github" {
//provider                  = google-beta
//  workload_identity_pool_id = "github-${var.project_id}"
//  project                   = var.project_id
//  display_name = "Github identity pool"
//}

module "github_actions_sa" {
  source  = "terraform-google-modules/service-accounts/google"
  version = "~> 4.0"

  project_id = var.project_id

  names        = ["github-actions-service-account"]
  display_name = "Github Actions SA for GCP - Managed by Terraform"

  project_roles =[]
}

module "gh_oidc" {
  source         = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  project_id     = var.project_id
  pool_id        = "github-identity-pool"
  provider_id    = "github-identity-provider"
  sa_mapping     = {
    "my_service_account" = {
      sa_name   = "projects/${var.project_id}/serviceAccounts/${module.github_actions_sa.email}"
      attribute = "attribute.repository/sttawm/a-pass"
    }
  }
}
