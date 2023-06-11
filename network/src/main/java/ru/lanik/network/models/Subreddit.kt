package ru.lanik.network.models

data class Subreddit(
    override val id: String,
    override val name: String,
    val namePrefixed: String,
    val nameWithoutPrefix: String,
    val url: String?,
    val imageUrl: String?,
    val isSubscriber: Boolean?,
    val description: String?,
    val subscribers: Int?,
    val created: Double?,
) : ListItem