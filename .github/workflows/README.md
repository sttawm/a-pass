This github action is based on [this Github Action repository](https://github.com/google-github-actions/deploy-cloudrun).

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
  --role="$1" \
  --member="serviceAccount:${SERVICE{@${PROJECT_ID}.iam.gserviceaccount.com"
```

# 4. (Optional) Fine-tune the service
It's defined via (./service.template.yaml) file. See [here](https://cloud.google.com/run/docs/configuring/containers) for further customization and configuration.

TODO: How to allow public access? https://cloud.google.com/run/docs/authenticating/public	

# 5. Configure secrets
This should be used to set the application key.

See https://cloud.google.com/run/docs/configuring/secrets
