package com.bangkit.sostainable.data.remote.api.service

import com.bangkit.sostainable.data.remote.response.auth.AuthResponse
import com.bangkit.sostainable.data.remote.response.event.EventResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("no_rekening") noRekening: String,
        @Field("nama_bank") namaBank: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthResponse

    @GET
    suspend fun getEvents(): EventResponse
}