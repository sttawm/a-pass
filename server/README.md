## Terraform

```
# Authenticate to Google Cloud
gcloud auth application-default login
```

```
gcloud projects create "$PROJECT_ID"
```

Enable billing: https://cloud.google.com/billing/docs/how-to/verify-billing-enabled

Set the PROJECT_ID var in the GA and .tf, and set the project number in GA via `gcloud projects list`

