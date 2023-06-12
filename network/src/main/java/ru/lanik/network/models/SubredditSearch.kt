package ru.lanik.network.models

data class SubredditSearch(
    override val id: String,
    override val name: String,
    val numSubscribers: Int,
    val communityIcon: String?,
) : ListItem
