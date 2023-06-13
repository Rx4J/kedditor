package ru.lanik.network.constants

enum class ApiBaseConst(
    val notAuth: String,
    val auth: String,
) {
    BASE_URL("https://reddit.com/", "https://oauth.reddit.com/"),
    SEARCH_SUBREDDIT("subreddit_autocomplete_v2.json", "subreddit_autocomplete_v2"),
}