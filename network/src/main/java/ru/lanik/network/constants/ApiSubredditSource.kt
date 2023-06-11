package ru.lanik.network.constants

enum class ApiSubredditSource(
    val notAuth: String,
    val auth: String,
) {
    DEFAULT("default.json", "default"),
    POPULAR("popular.json", "popular"),
    GOLD("gold.json", "gold"),
    NEW("new.json", "new"),
}