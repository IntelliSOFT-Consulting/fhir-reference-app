package com.intellisoft.fhirstarterapp.network

import com.intellisoft.fhirstarterapp.model.LoginResponse
import com.intellisoft.fhirstarterapp.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface Interface {

    @POST("users/login")
    @Headers("Accept: application/json")

    suspend fun signInUser(
        @Body data: User
    ): Response<LoginResponse>

//    @POST("users/register")
//    suspend fun signInUser(
//        @Body dbSignIn: DbSignIn
//    ): Response<DbSignInResponse>
//
//    @GET("users/profile/")
//    suspend fun getUserInfo(
//        @Header("Authorization") token: String, // Add this line to pass the Bearer Token
//    ): Response<DbUserInfoResponse>
//

}