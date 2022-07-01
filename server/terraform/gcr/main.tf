provider "google" {
  project = var.project_id
}

# Enable APIs for the gh-oidc module
//resource "google_project_service" "iam_api" {
//  service            = "iam.googleapis.com"
//}
//resource "google_project_service" "cloudresourcemanager_api" {
//  service            = "cloudresourcemanager.googleapis.com"
//}
//resource "google_project_service" "iamcredentials_api" {
//  service            = "iamcredentials.googleapis.com.googleapis.com"
//}
//resource "google_project_service" "sts_api" {
//  service            = "sts.googleapis.com"
//}

# Enable APIs for GCR
//resource "google_project_service" "gcr_api" {
//  service            = "containerregistry.googleapis.com"
//}

//resource "google_container_registry" "registry" {
//  project  = var.project_id
//}

resource "google_service_account" "sa" {
  project    = var.project_id
  account_id = "github-actions-service-account"
}

resource "google_project_iam_member" "storage_admin" {
  project = var.project_id
  role = "roles/storage.admin"
  member = "serviceAccount:${google_service_account.sa.email}"
}

module "gh_oidc" {
  source         = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  project_id     = var.project_id
  pool_id        = "github-identity-pool"
  provider_id    = "github-identity-provider"
  sa_mapping     = {
    "(google_service_account.sa.account_id)" = {
      sa_name   = google_service_account.sa.name
      attribute = "attribute.repository/sttawm/a-pass"
    }
  }
}


