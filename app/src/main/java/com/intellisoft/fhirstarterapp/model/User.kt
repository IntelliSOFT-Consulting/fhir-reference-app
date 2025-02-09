package com.intellisoft.fhirstarterapp.model


/****
 *
 * {"User":{"username":"xx","password":"sss"}}
 *
 */

data class User(
    val User: PayloadData
)

data class PayloadData(
    val username: String="",
    val name: String="",
    val telephone: String="",
    val email: String="",
    val designationId: String="",
    val countyId: String="",
    val password: String="",
    val confirm_password: String =""
)

data class RegistrationResponse(
    val status: String,
    val user: UserRegistrationInfo,
    val validation: Map<String, List<String>>?//Validation?
)

data class CountyResponse(
    val counties: Map<String, String>
)

data class DesignationResponse(
    val designations: Map<String, String>
)

data class UniversalMap(
    val code: String,
    val value: String
)

//data class DesignationResponse(
//    val designations: Map<String, String>
//)

data class UserRegistrationInfo(
    val User: PayloadData
)

data class Validation(
    val username: List<String>?,
    val email: List<String>?,
    val confirm_password: List<String>?,
    val password: List<String>?
)



//{
//    "User": {
//    "username": "test2545",
//    "name": "Test 254",
//    "email": "test2545@gmail.com",
//    "password": "test254",
//    "confirm_password": "test254"
//}
//}

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

