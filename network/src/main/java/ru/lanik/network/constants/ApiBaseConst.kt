package ru.lanik.network.constants

enum class ApiBaseConst(
    val notAuth: String,
    val auth: String,
) {
    BASE_URL("https://reddit.com/", "https://oauth.reddit.com/"),
}