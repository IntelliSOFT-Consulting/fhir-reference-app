package com.intellisoft.fhirstarterapp.model

data class User(
    val User: LoginData
)

data class LoginData(
    val username: String,
    val password: String
)

data class LoginResponse(
    val user: UserInformation,
    val token: String,
)

data class UserInformation(
    val id: String,
    val username: String,
    val email: String
)