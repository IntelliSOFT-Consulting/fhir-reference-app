package com.intellisoft.fhirstarterapp.network

import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.intellisoft.fhirstarterapp.MainActivity
import com.intellisoft.fhirstarterapp.fhir.enums.UrlData
import com.intellisoft.fhirstarterapp.model.UniversalMap
import com.intellisoft.fhirstarterapp.model.User
import com.intellisoft.fhirstarterapp.utils.LocalData
import com.intellisoft.fhirstarterapp.viewModel.FhirViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject


class RetrofitCallsAuthentication {


    fun loginUser(context: Context, data: User) {

        CoroutineScope(Dispatchers.Main).launch {

            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {
                starLogin(context, data)
            }.join()
        }

    }

    fun registerUser(context: Context, data: User) {

        CoroutineScope(Dispatchers.Main).launch {

            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {
                startRegister(context, data)
            }.join()
        }

    }

    private suspend fun starLogin(context: Context, data: User) {


        val job1 = Job()
        CoroutineScope(Dispatchers.Main + job1).launch {

            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Please wait..")
            progressDialog.setMessage("Authentication in progress..")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            var messageToast = ""
            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {

                val baseUrl = context.getString(UrlData.BASE_URL.message)

                val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
                try {
                    val apiInterface = apiService.signInUser(data)

                    if (apiInterface.isSuccessful) {

                        val statusCode = apiInterface.code()
                        val body = apiInterface.body()

                        if (statusCode == 200 || statusCode == 201) {

                            if (body != null) {
                                val accessToken = body.token
                                val user = body.user

                                //Save to shared prefs

                                LocalData().saveSharedPref("access_token", accessToken, context)
                                LocalData().saveSharedPref("isLoggedIn", "true", context)
                                LocalData().saveSharedPref(
                                    "user_info",
                                    Gson().toJson(user),
                                    context
                                )
                                messageToast = "Login successful.."

                                val intent = Intent(context, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                context.startActivity(intent)
                                if (context is Activity) {
                                    context.finish()
                                }

                            } else {
                                messageToast = "Error: Body is null"
                            }
                        } else {
                            messageToast = "Error: The request was not successful"
                        }
                    } else {
                        val errorCode = apiInterface.code()

                        apiInterface.errorBody()?.let {
                            val errorBody = JSONObject(it.string())
                            messageToast = errorBody.getString("message")
                        }
                    }


                } catch (e: Exception) {

                    messageToast = "Cannot login user.."
                }


            }.join()
            CoroutineScope(Dispatchers.Main).launch {

                progressDialog.dismiss()
                Toast.makeText(context, messageToast, Toast.LENGTH_LONG).show()

            }

        }

    }

    private suspend fun startRegister(context: Context, data: User) {


        val job1 = Job()
        CoroutineScope(Dispatchers.Main + job1).launch {

            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Please wait..")
            progressDialog.setMessage("Authentication in progress..")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            var messageToast = "<MutableList<UniversalMap>>"
            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {

                val baseUrl = context.getString(UrlData.BASE_URL.message)

                val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
                try {
                    val apiInterface = apiService.registerUser(data)
                    Log.e("Tag", "Payload $data")
                    if (apiInterface.isSuccessful) {

                        val statusCode = apiInterface.code()
                        val body = apiInterface.body()

                        if (statusCode == 200 || statusCode == 201) {

                            if (body != null) {
                                val user = body.user
                                if (body.status == "success") {

                                    //Save to shared prefs

                                    LocalData().saveSharedPref(
                                        "user_info",
                                        Gson().toJson(user),
                                        context
                                    )
                                    messageToast = "Registration successful.."

                                    if (context is Activity) {
                                        context.finish()
                                    }
                                } else {
                                    messageToast = "${body.validation}"
                                }

                            } else {
                                messageToast = "Error: Body is null"
                            }
                        } else {
                            messageToast = "Error: The request was not successful"
                        }
                    } else {
                        val errorCode = apiInterface.code()
                        Log.e("Tag", "Payload $errorCode")
                        Log.e("Tag", "Payload ${apiInterface.errorBody()}")
//                        apiInterface.errorBody()?.let {
//                            val errorBody = JSONObject(it.string())
//                            messageToast = errorBody.getString("message")
//                        }
                    }


                } catch (e: Exception) {
                    Log.e("Tag", "Payload ${e.message}")
                    messageToast = "Cannot register user ${e.message}"

                }


            }.join()
            CoroutineScope(Dispatchers.Main).launch {

                progressDialog.dismiss()
                Toast.makeText(context, messageToast, Toast.LENGTH_LONG).show()

            }

        }

    }

      fun loadDesignations(context: Context, viewModel: FhirViewModel) = runBlocking {
        val baseUrl = context.getString(UrlData.BASE_URL.message)
        val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
        try {
            val apiInterface = apiService.loadDesignations()
            if (apiInterface.isSuccessful) {

                val statusCode = apiInterface.code()
                val body = apiInterface.body()

                if (statusCode == 200 || statusCode == 201) {

                    if (body != null) {
                        val responseDesignationList = ArrayList<UniversalMap>()

                        body.designations.forEach{
                            val universal = UniversalMap(it.key, it.value)
                            responseDesignationList.add(universal)
                        }

                        viewModel.getDesignations(responseDesignationList)
                    } else {

                    }
                } else {

                }
            } else {
                val errorCode = apiInterface.code()
                Log.e("Tag", "Payload $errorCode")
                Log.e("Tag", "Payload ${apiInterface.errorBody()}")
            }


        } catch (e: Exception) {
            Log.e("Tag", "Payload ${e.message}")

        }

    }

    fun loadCounties(context: Context, viewModel: FhirViewModel) = runBlocking {
        val baseUrl = context.getString(UrlData.BASE_URL.message)
        val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
        try {
            val apiInterface1 = apiService.loadCounties()
            if (apiInterface1.isSuccessful) {

                val statusCode = apiInterface1.code()
                val body = apiInterface1.body()

                if (statusCode == 200 || statusCode == 201) {

                    if (body != null) {
                        val responseCountyList = ArrayList<UniversalMap>()

                        body.counties.forEach{
                            val universal = UniversalMap(it.key, it.value)
                            responseCountyList.add(universal)
                        }

                        viewModel.getCounties(responseCountyList)
                    } else {

                    }
                } else {

                }
            } else {
                val errorCode = apiInterface1.code()
                Log.e("Tag", "County Payload $errorCode")
                Log.e("Tag", "County Payload ${apiInterface1.errorBody()}")
            }


        } catch (e: Exception) {
            Log.e("Tag", "County Payload ${e.message}")

        }
    }

    private fun startLoadingCounties(context: Context, viewModel: FhirViewModel) {
       val job1 = Job()
        CoroutineScope(Dispatchers.Main + job1).launch {

            var messageToast = ""
            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {

                val baseUrl = context.getString(UrlData.BASE_URL.message)

                val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
                try {
                    val apiInterface = apiService.loadCounties()
                    if (apiInterface.isSuccessful) {

                        val statusCode = apiInterface.code()
                        val body = apiInterface.body()

                        if (statusCode == 200 || statusCode == 201) {

                            if (body != null) {
                                val counties = body.counties
                                Log.e("County List", "Counties $counties")
                                messageToast = Gson().toJson(counties)
                                Log.e("County List", "Counties $messageToast")
                            } else {
                                messageToast = "Error: Body is null"
                            }
                        } else {
                            messageToast = "Error: The request was not successful"
                        }
                    } else {
                        val errorCode = apiInterface.code()
                        Log.e("Tag", "Payload $errorCode")
                        Log.e("Tag", "Payload ${apiInterface.errorBody()}")
//                        apiInterface.errorBody()?.let {
//                            val errorBody = JSONObject(it.string())
//                            messageToast = errorBody.getString("message")
//                        }
                    }


                } catch (e: Exception) {
                    Log.e("Tag", "Payload ${e.message}")
                    messageToast = "Cannot register user ${e.message}"

                }


            }.join()
            CoroutineScope(Dispatchers.Main).launch {
                Log.e("County List", "Counties Final $messageToast")
                //Toast.makeText(context, messageToast, Toast.LENGTH_LONG).show()
                //viewModel.getCounties(messageToast)
            }

        }
    }

    private fun startLoadingDesignations(context: Context, viewModel: FhirViewModel) {
        //val viewModel = FhirViewModel(context.applicationContext as Application)
        val job1 = Job()
        CoroutineScope(Dispatchers.Main + job1).launch {

            var messageToast = ""
            val job = Job()
            CoroutineScope(Dispatchers.IO + job).launch {

                val baseUrl = context.getString(UrlData.BASE_URL.message)

                val apiService = RetrofitBuilder.getRetrofit(baseUrl).create(Interface::class.java)
                try {
                    val apiInterface = apiService.loadDesignations()
                    if (apiInterface.isSuccessful) {

                        val statusCode = apiInterface.code()
                        val body = apiInterface.body()

                        if (statusCode == 200 || statusCode == 201) {

                            if (body != null) {
                                var designations = body.designations
                                Log.e("Designation List", "Designations $designations")
                                messageToast = Gson().toJson(designations)
                                Log.e("Designation List", "Designations $messageToast")
                            } else {
                                messageToast = "Error: Body is null"
                            }
                        } else {
                            messageToast = "Error: The request was not successful"
                        }
                    } else {
                        val errorCode = apiInterface.code()
                        Log.e("Tag", "Payload $errorCode")
                        Log.e("Tag", "Payload ${apiInterface.errorBody()}")
                    }


                } catch (e: Exception) {
                    Log.e("Tag", "Payload ${e.message}")
                    messageToast = "Cannot register user ${e.message}"

                }


            }.join()
            CoroutineScope(Dispatchers.Main).launch {
                Log.e("Designation List", "Designation Final $messageToast")
                //Toast.makeText(context, messageToast, Toast.LENGTH_LONG).show()
                //viewModel.getDesignations(messageToast)
            }

        }
    }


}