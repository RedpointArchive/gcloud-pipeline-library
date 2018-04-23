# Jenkins gcloud pipeline library

This repository contains a `gcloud` pipeline step that you can use to load service account credentials in a temporary environment for your build jobs. Instead of configuring gcloud on each agent, it effectively lets you load service account credentials from Jenkins and use those for invocations of `gcloud` and other commands.

## Usage

You can use this step like so:

```groovy
@Library("gcloud-pipeline-library")

node {
  gcloud(serviceAccountCredential: 'my-credential-name') {
    sh 'gcloud ...'
  }
}
```

or alternatively:

```groovy
@Library("gcloud-pipeline-library")

node {
  gcloud(serviceAccountPath: 'path/to/service/account.json') {
    sh 'gcloud ...'
  }
}
```

## Setup

You need to add this library to your Jenkins instance first. Under **Manage Jenkins -> Configure System**, scroll down until you find the **Global Pipeline Libraries** section, then add this repository like so:

![Setup Instructions](./setup.png)