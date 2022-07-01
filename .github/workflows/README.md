This github action is based on [this Github Action repository](https://github.com/google-github-actions/deploy-cloudrun).

It requires setup in google-cloud. Here are the steps:

# 0. Variables
```
export REGION="us-central1"
export PROJECT="apass-354122"
export SERVICE="apass-service-account"
export DB_ROOT_PASSWORD=""
```

# 1. Create a project in google cloud
TODO: The CLI command to create a project

Enable the APIs

# 2. Authenticate Github
Then, see [Setting up Workload Identify Federation](https://github.com/google-github-actions/auth#setting-up-workload-identity-federation) for the required auth setup.

# 3. Authenticate the Google IAM user
Then, add the roles:


For each of the following roles:
* `roles/run.admin`
* `roles/iam.serviceAccountUser`
* `roles/storage.admin`

...do:

```
export SERVICE=my-service-account
gcloud projects add-iam-policy-binding "${PROJECT_ID}" \
  --role="$ROLE" \
  --member="serviceAccount:${SERVICE}@${PROJECT_ID}.iam.gserviceaccount.com"
```

# 4. (Optional) Fine-tune the service
It's defined via (./service.template.yaml) file. See [here](https://cloud.google.com/run/docs/configuring/containers) for further customization and configuration.

TODO: How to allow public access? https://cloud.google.com/run/docs/authenticating/public	
# 5. Configure secrets
This should be used to set the application key.

See https://cloud.google.com/run/docs/configuring/secrets

# Setting up a Cloud SQL Postgres instance

_Steps copies from [here](https://cloud.google.com/sql/docs/postgres/connect-instance-cloud-run#gcloud)_

```
# 1. Enable Cloud APIs
gcloud services enable compute.googleapis.com sqladmin.googleapis.com run.googleapis.com \
containerregistry.googleapis.com cloudbuild.googleapis.com servicenetworking.googleapis.com

# 2. Create a Cloud SQL Instance with a Private IP

# allocate an IP address range
gcloud compute addresses create google-managed-services-default \
--global --purpose=VPC_PEERING --prefix-length=16 \
--description="peering range for Google" --network=default


# create a private connection to the allocated IP address range
gcloud services vpc-peerings connect --service=servicenetworking.googleapis.com \
--ranges=google-managed-services-default --network=default \
--project=$PROJECT

gcloud sql instances create quickstart-instance \
--database-version=POSTGRES_13 \
--tier=db-f1-micro \
 --region=us-central \
 --root-password=$DB_ROOT_PASSWORD \
 --no-assign-ip \
--network=default

gcloud sql instances patch quickstart-instance --require-ssl
```


