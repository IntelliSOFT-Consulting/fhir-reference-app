package com.intellisoft.fhirstarterapp.model


/****
 *
 * {"User":{"username":"xx","password":"sss"}}
 *
 */

data class User(
    val User: LoginData
)

data class LoginData(
    val username: String,
    val password: String
)

/**
 * Successful response
 *
 *  {"user":{"id":"1234","username":"some username","email":"some@gmail.com"},"token":"some token"}
 *
 * */
data class LoginResponse(
    val user: UserInformation,
    val token: String,
)

data class UserInformation(
    val id: String,
    val username: String,
    val email: String
)


/***
 * expecting 190 records
 * */
data class PatientRecord(
    val users: List<Patient>,
    val status: String,
    val message: String
)

data class Patient(
    val name: String,
    val dob: String,
    val gender: String
)