package com.bangkit.sostainable.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sostainable.data.injection.InjectionEvent
import com.bangkit.sostainable.data.repository.event.EventRepository
import com.bangkit.sostainable.view.main.create.CreateViewModel
import com.bangkit.sostainable.view.main.createReport.CreateReportViewModel
import com.bangkit.sostainable.view.main.detail.DetailViewModel
import com.bangkit.sostainable.view.main.donate.DonateViewModel
import com.bangkit.sostainable.view.main.home.HomeViewModel
import com.bangkit.sostainable.view.main.myEvent.MyEventViewModel

class EventModelFactory(
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // TODO: HomeViewModel
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository) as T
            }
            // TODO: DetailViewModel
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository) as T
            }
            // TODO: CreateViewModel
            modelClass.isAssignableFrom(CreateViewModel::class.java) -> {
                CreateViewModel(eventRepository) as T
            }

            // TODO: DonateViewModel
            modelClass.isAssignableFrom(DonateViewModel::class.java) -> {
                DonateViewModel(eventRepository) as T
            }

            // TODO: CreateReportViewModel
            modelClass.isAssignableFrom(CreateReportViewModel::class.java) -> {
                CreateReportViewModel(eventRepository) as T
            }

            // TODO: MyEventViewModel
            modelClass.isAssignableFrom(MyEventViewModel::class.java) -> {
                MyEventViewModel(eventRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = EventModelFactory(InjectionEvent.provideRepository(context))
    }
}