package com.swi.admincafe.api.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.swi.admincafe.api.repository.AppRepository
import com.swi.admincafe.api.viewModel.LoginViewModel

class AuthViewModelFactory(val app: Application, private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(app,repository) as T
    }
}