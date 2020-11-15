package com.example.cft1stproject.retrofit

import com.example.cft1stproject.model.json.PostImgModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ImgurAPI {

    @Multipart
    @POST("upload")
    fun uploadImg(
        @Header("Authorization") token: String,
        @Part("type") type: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("image") file: RequestBody
        ) : Call<PostImgModel>
    //"; filename="file.png"

}