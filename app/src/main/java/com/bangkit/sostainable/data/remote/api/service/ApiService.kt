package com.bangkit.sostainable.data.remote.api.service

import com.bangkit.sostainable.data.remote.response.auth.AuthResponse
import com.bangkit.sostainable.data.remote.response.event.EventResponse
import com.bangkit.sostainable.data.remote.response.profile.ProfileResponse
import com.bangkit.sostainable.data.remote.response.profile.update.UpdateProfileResponse
import com.bangkit.sostainable.data.remote.response.event.detail.DetailResponse
import com.bangkit.sostainable.data.repository.auth.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body resquest: User
    ): AuthResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginJson
    ): AuthResponse

    @GET("event")
    suspend fun getEvents(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): EventResponse

    @GET("event/detail/{id}")
    suspend fun getDetailEvent(@Path("id") id: String): DetailResponse

    @Multipart
    @POST("event/create")
    suspend fun postEvent(
        @Part("judul_event") judulEvent: RequestBody,
        @Part("tipe_lokasi") tipeLokasi: RequestBody,
        @Part fileFoto: List<MultipartBody.Part>,
        @Part("deskripsi") deskripsiEvent: RequestBody,
        @Part("jam_mulai") jamMulai: RequestBody,
        @Part("jam_selesai") jamSelesai: RequestBody,
        @Part("tanggal_mulai") tanggalMulai: RequestBody,
        @Part("tanggal_selesai") tanggalSelesai: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("jumlah_minimum_volunteer") jumlahMinimumVolunteer: Int,
        @Part("jumlah_minimum_donasi") jumlahMinimumDonasi: Long,
        @Part("pembuat_event") pembuatEvent: RequestBody
    ): EventMessageResponse

    @POST("event/donate")
    suspend fun donateEvent(
        @Body request: DonateJson
    ): EventMessageResponse

    @POST("event/join")
    suspend fun joinEvent(
        @Body request: JoinJson
    ): EventMessageResponse

    @GET("profile")
    suspend fun profileUser(): ProfileResponse

    @FormUrlEncoded
    @PUT("profile/update")
    suspend fun updateProfile(
        @Field("nama") nama: String,
        @Field("username") username: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("no_rekening") noRekening: String,
        @Field("nama_bank") namaBank: String,
        @Field("no_telepon") noTelp: String,
        @Field("alamat") alamat: String
    ): UpdateProfileResponse
}