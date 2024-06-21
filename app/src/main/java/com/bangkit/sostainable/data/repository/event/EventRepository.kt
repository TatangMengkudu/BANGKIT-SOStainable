package com.bangkit.sostainable.data.repository.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.sostainable.data.json.DonateJson
import com.bangkit.sostainable.data.json.JoinJson
import com.bangkit.sostainable.data.json.ReportJson
import com.bangkit.sostainable.data.local.room.database.EventDatabase
import com.bangkit.sostainable.data.paging.EventRemoteMediator
import com.bangkit.sostainable.data.remote.api.service.ApiService
import com.bangkit.sostainable.data.remote.response.event.DataItem
import com.bangkit.sostainable.data.remote.response.event.EventMessageResponse
import com.bangkit.sostainable.data.remote.response.event.MyEventResponse
import com.bangkit.sostainable.data.remote.response.event.detail.DetailResponse
import com.bangkit.sostainable.data.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException

class EventRepository(
    private val apiService: ApiService,
    private val eventDatabase: EventDatabase
) {
    // TODO: Get All Event
    fun getEvents(): LiveData<PagingData<DataItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = EventRemoteMediator(apiService, eventDatabase),
            pagingSourceFactory = {
                eventDatabase.eventDao().getAllEvent()
            }
        ).liveData
    }
    // TODO: Get Detail Event
    suspend fun getDetailEvent(id: String): LiveData<Result<DetailResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getDetailEvent(id)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    // TODO: Create Event
    suspend fun createEvent(event: Event):LiveData<Result<EventMessageResponse>>{
        return liveData {
            emit(Result.Loading)
            try {
                val judulEvent = event.judulEvent.toRequestBody("text/plain".toMediaType())
                val tipeLokasi = event.tipeLokasi.toRequestBody("text/plain".toMediaType())
                val desc = event.deskripsiEvent.toRequestBody("text/plain".toMediaType())
                val jamMulai = event.jamMulai.toRequestBody("text/plain".toMediaType())
                val jamSelesai = event.jamSelesai.toRequestBody("text/plain".toMediaType())
                val tanggalMulai = event.tanggalMulai.toRequestBody("text/plain".toMediaType())
                val tanggalSelesai = event.tanggalSelesai.toRequestBody("text/plain".toMediaType())
                val alamat = event.alamat.toRequestBody("text/plain".toMediaType())
                val pembuatEvent = event.pembuatEvent.toRequestBody("text/plain".toMediaType())
                val response = apiService.postEvent(
                    judulEvent,
                    tipeLokasi,
                    event.fileFoto,
                    desc,
                    jamMulai,
                    jamSelesai,
                    tanggalMulai,
                    tanggalSelesai,
                    alamat,
                    event.jumlahMinimumVolunteer,
                    event.jumlahMinimumDonasi,
                    pembuatEvent
                )
                emit(Result.Success(response))
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody).getString("message")
                        emit(Result.Error(errorMessage))
                    } else {
                        emit(Result.Error(e.message ?: "Unknown error"))
                    }
                } else {
                    emit(Result.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    // TODO: Join Event
    suspend fun joinEvent(joinJson: JoinJson): LiveData<Result<EventMessageResponse>> {
        return liveData {
            emit(Result.Loading)
            try{
                val response = apiService.joinEvent(joinJson)
                emit(Result.Success(response))
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody).getString("message")
                        emit(Result.Error(errorMessage))
                    }
                    else {
                        emit(Result.Error(e.message ?: "Unknown error"))
                    }
                } else {
                    emit(Result.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    // TODO: Donate Event
    suspend fun donateEvent(donateJson: DonateJson): LiveData<Result<EventMessageResponse>> {
        return liveData {
            emit(Result.Loading)
            try{
                val response = apiService.donateEvent(donateJson)
                emit(Result.Success(response))
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody).getString("message")
                        emit(Result.Error(errorMessage))
                    }
                    else {
                        emit(Result.Error(e.message ?: "Unknown error"))
                    }
                } else {
                    emit(Result.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    // TODO: Create Report Event
    suspend fun createReport(report: ReportJson): LiveData<Result<EventMessageResponse>> {
        return liveData {
            emit(Result.Loading)
            try{
                val kendala = report.kendala.toRequestBody("text/plain".toMediaType())
                val idEvent = report.id_event.toRequestBody("text/plain".toMediaType())
                val response = apiService.createReport(kendala, report.jumlah_volunteer, idEvent, report.fileFoto)
                emit(Result.Success(response))
            }catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody).getString("message")
                        emit(Result.Error(errorMessage))
                    }
                    else {
                        emit(Result.Error(e.message ?: "Unknown error"))
                    }
                } else {
                    emit(Result.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    // TODO: MY Event
    suspend fun getMyEvent(): LiveData<Result<MyEventResponse>>{
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getMyEvent()
                emit(Result.Success(response))
            } catch (e: Exception) {
                if (e is HttpException) {
                    if (e.code() == 400) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody).getString("message")
                        emit(Result.Error(errorMessage))
                    }
                    else {
                        emit(Result.Error(e.message ?: "Unknown error"))
                    }
                }
                else {
                    emit(Result.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }
}