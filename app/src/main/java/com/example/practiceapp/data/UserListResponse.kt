package com.example.practiceapp.data


data class UserListResponse(
    val page: Int,
    val data: List<DataItem>
)

data class DataItem(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)