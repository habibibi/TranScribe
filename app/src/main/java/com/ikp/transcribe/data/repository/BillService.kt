package com.ikp.transcribe.data.repository

import com.ikp.transcribe.data.model.BillResponse
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response

interface BillService {
    @Multipart
    @POST("api/bill/upload")
    suspend fun getBill(@Header("Authorization") token : String, @Part photoPart : MultipartBody.Part) : Response<BillResponse>
}