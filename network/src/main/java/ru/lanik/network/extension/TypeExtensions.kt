package ru.lanik.network.extension

import ru.lanik.network.api.dto.subredditDTO.SubredditDto
import ru.lanik.network.api.dto.subredditDTO.SubredditSearchDto
import ru.lanik.network.models.Subreddit
import ru.lanik.network.models.SubredditSearch

fun List<SubredditDto>.toListSubreddit(): List<Subreddit> = this.map { item -> item.toSubreddit() }
fun List<SubredditSearchDto.SubredditSearchDataDto>.toListSubredditSearch(): List<SubredditSearch> =
    this.map { item -> item.toSubredditSearch() }

fun SubredditDto.toSubreddit(): Subreddit {
    return Subreddit(
        id = data.name,
        name = data.displayName,
        namePrefixed = data.displayNamePrefixed,
        url = data.url,
        imageUrl = data.iconImg ?: data.iconCommunity,
        isSubscriber = data.isSubscriber,
        description = data.description,
        subscribers = data.subscribers,
        created = data.created,
    )
}

fun SubredditSearchDto.SubredditSearchDataDto.toSubredditSearch(): SubredditSearch {
    return SubredditSearch(
        id = id,
        name = name,
        numSubscribers = numSubscribers,
        communityIcon = communityIcon,
    )
}