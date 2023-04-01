package com.sefatombul.gcase.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sefatombul.gcase.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    application: Application, private val apiRepository: ApiRepository
) : AndroidViewModel(application) {



}