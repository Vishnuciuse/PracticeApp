package com.example.practiceapp.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practiceapp.api.UserRepository
import com.example.practiceapp.data.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _userState = MutableStateFlow<List<DataItem>>(emptyList())
    val userState: StateFlow<List<DataItem>> = _userState

    private val _counter = MutableStateFlow<Int>(0)
    val counter: StateFlow<Int> = _counter

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        Log.d("api@#$","Api call initiated")
        getUsers()
    }

    private fun getUsers(page: Int = 2) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.fetchUsers(page)
                _userState.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

     fun incrementCounter() {
        _counter.value++
    }
}
