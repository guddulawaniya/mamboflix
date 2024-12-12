package com.mamboflix.ui.activity.payment.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mamboflix.api.ApiService
import com.mamboflix.utils.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepository: ApiService): ViewModel() {
    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.callGetPackageAPI("","","")))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}