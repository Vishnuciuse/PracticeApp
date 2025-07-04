package com.example.practiceapp.api

import com.example.practiceapp.data.UserListResponse

class UserRepository {
    suspend fun fetchUsers(page: Int): UserListResponse {
        return RetrofitInstance.api.getUsers(page)
    }
}