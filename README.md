# Reference App

Welcome to the FHIR Resource Showcase App repository! This application demonstrates the implementation of several FHIR (Fast Healthcare Interoperability Resources) resources, providing a comprehensive example of how to work with FHIR standards in a practical, real-world scenario.
## Introduction
FHIR Resource Showcase App is designed to help developers understand and implement FHIR resources in healthcare applications. The app includes examples of various FHIR resources, demonstrating how to create, read, update, and delete (CRUD) operations with them.

## Features
* Implementation of multiple FHIR resources 
* CRUD operations for each resource
* Interactive UI for exploring FHIR data
* Sample data for demonstration purposes
* Integration with a FHIR server



## Technologies Used
* Kotlin: The primary programming language used for the Android application.
* FHIR SDK: For working with FHIR resources.
* Retrofit: For network requests.
* Room: For local database storage.
* MVVM Architecture: For a scalable and maintainable codebase.


## Setup
### Prerequisites
* Android Studio
* Java Development Kit (JDK)
* FHIR server (optional, for integration)


### Installation
* Clone the repository:
```
git clone https://github.com/IntelliSOFT-Consulting/fhir-reference-app.git

```
* cd fhir-reference-app
#### Open the project in Android Studio:

* Launch Android Studio.
* Select "Open an existing Android Studio project".
* Navigate to the cloned directory and select it.

#### Build the project:

* Sync the project with Gradle files.
* Build the project to ensure all dependencies are resolved.

### Configuration
#### FHIR Server Integration:
If you have a FHIR server, update the baseUrl in the RetrofitClient to point to your server.
#### Usage
Run the app:

* Connect an Android device or use an emulator.
* Run the app from Android Studio.

#### Explore FHIR Resources:

* Use the interactive UI to perform CRUD operations on various FHIR resources.
* View sample data and modify it as needed.

### FHIR Resources Implemented
* Patient: Basic patient information and demographics.
* Observation: Vital signs, laboratory results, etc.
* Encounter: Information about patient encounters.
* Condition: Clinical conditions and diagnoses.


## Contributing
We welcome contributions to enhance the functionality and scope of this app. To contribute:
```
Fork the repository.
Create a new branch with a descriptive name.
Make your changes.
Commit and push your changes to your fork.
Open a pull request with a detailed description of your changes.
License
This project is licensed under the MIT License. See the LICENSE file for more information.

```