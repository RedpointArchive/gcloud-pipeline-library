# Jenkins gcloud pipeline library

This repository contains a `gcloud` pipeline step that you can use to load service account credentials in a temporary environment for your build jobs. Instead of configuring gcloud on each agent, it effectively lets you load service account credentials from Jenkins and use those for invocations of `gcloud` and other commands.

## Usage

You can use this step like so:

```groovy
library "gcloud-pipeline-library"

node {
  gcloud(serviceAccountCredential: 'my-credential-name') {
    sh 'gcloud ...'
  }
}
```

or alternatively:

```groovy
library "gcloud-pipeline-library"

node {
  gcloud(serviceAccountPath: 'path/to/service/account.json') {
    sh 'gcloud ...'
  }
}
```

## Credentials

If using the first form (`serviceAccountCredential`), the credential should be of the type **Secret file**, where the file is a JSON service account key obtained from the Google Cloud console.

## Setup

You need to add this library to your Jenkins instance to use it.

### Determine Version

On this page, locate the current commit hash of the project. It is recommended that you use a commit hash instead of `master`, as this library is updated to support the latest `gcloud` commands (which can change in breaking ways when new SDK versions are released). The commit hash is located here:

![The commit hash is shown next to "Latest commit"](./commit-hash.png)

**WARNING:** If you use `master`, your builds might stop working when new updates are made to this library, and you might need to update the Google Cloud SDK installed on your build agents. In addition, updates to this library will cause all builds using this library to kick off, because Jenkins polls for changes to pipeline libraries and starts builds if they've changed. You probably don't want updates to this library kicking off builds on the weekend, so please, locate and use a commit hash as outlined above.

### Install into Jenkins

Under **Manage Jenkins -> Configure System**, scroll down until you find the **Global Pipeline Libraries** section, then add this repository like so:

![Setup Instructions](./setup.png)

## License

```
MIT License

Copyright (c) 2018 Redpoint Games

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```