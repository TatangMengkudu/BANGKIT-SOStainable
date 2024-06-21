package com.bangkit.sostainable.view.main.createReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sostainable.data.json.ReportJson
import com.bangkit.sostainable.data.remote.response.event.EventMessageResponse
import com.bangkit.sostainable.data.repository.event.EventRepository
import com.bangkit.sostainable.data.utils.Result

class CreateReportViewModel(private val eventRepository: EventRepository): ViewModel() {

    suspend fun createReportEvent(reportJson: ReportJson): LiveData<Result<EventMessageResponse>> {
        return eventRepository.createReport(reportJson)
    }
}