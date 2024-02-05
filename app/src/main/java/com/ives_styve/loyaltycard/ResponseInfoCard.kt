package com.ives_styve.loyaltycard

data class ResponseInfoCard(
    val id: Int,
    val name: String,
    val data: String,
    val type: Int,
    val createdAt: String,
    val lastUsedAt: String
)
