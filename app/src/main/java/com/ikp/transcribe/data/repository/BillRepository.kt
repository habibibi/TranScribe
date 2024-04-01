package com.ikp.transcribe.data.repository

import android.util.Log
import com.ikp.transcribe.data.model.Item
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File


class BillRepository{

    private val baseUrl =
        "https://pbd-backend-2024.vercel.app"
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .build()
    private val billService : BillService by lazy{
        retrofit.create(BillService::class.java)
    }

    suspend fun getBill(token : String, image : File) : List<Item>{
        val filePart = MultipartBody.Part.createFormData(
            "file", image.getName(), RequestBody.create(
                MediaType.parse("image/*"), image
            )
        )
        val tmp = "Bearer " + token.replace("\n","")
        try {
            val response = billService.getBill(
                tmp,
                filePart
            )
            if (response.isSuccessful) {
                return response.body()!!.items.items
            } else {
                // Handle HTTP error response
                Log.e("bill", "HTTP Error: ${response.code()}")
            }
        } catch (e: Exception) {
            // Handle network or other errors
            Log.e("bill", "Error: ${e.message}", e)
        }
        return emptyList()
    }
}