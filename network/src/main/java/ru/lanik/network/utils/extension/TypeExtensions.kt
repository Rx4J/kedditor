package ru.lanik.network.utils.extension

import ru.lanik.network.api.dto.subredditDTO.SubredditDto
import ru.lanik.network.models.Subreddit

fun List<SubredditDto>.toListSubreddit(): List<Subreddit> = this.map { item -> item.toSubreddit() }

fun SubredditDto.toSubreddit(): Subreddit {
    return Subreddit(
        id = data.id,
        name = data.name,
        namePrefixed = data.displayNamePrefixed,
        nameWithoutPrefix = data.displayName,
        url = data.url,
        imageUrl = data.iconImg ?: data.iconCommunity,
        isSubscriber = data.isSubscriber,
        description = data.description,
        subscribers = data.subscribers,
        created = data.created,
    )
}