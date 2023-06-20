package ru.lanik.network.extension

import ru.lanik.network.api.dto.postDTO.PostDto
import ru.lanik.network.api.dto.subredditDTO.SubredditDto
import ru.lanik.network.models.Post
import ru.lanik.network.models.Subreddit

fun List<SubredditDto>.toListSubreddit(): List<Subreddit> = this.map { item -> item.toSubreddit() }

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

fun List<PostDto>.toListPost(): List<Post> = this.map { item -> item.toPost() }

fun PostDto.toPost(): Post {
    val voteDirection = when (data.likes) {
        null -> 0
        true -> 1
        false -> -1
    }
    return Post(
        selfText = data.selfText,
        saved = data.saved,
        title = data.title,
        subredditNamePrefixed = data.subredditNamePrefixed,
        name = data.subreddit,
        score = data.score,
        created = data.created,
        id = data.name,
        author = data.author,
        subredditId = data.subredditId,
        numComments = data.numComments,
        permalink = data.permalink,
        url = data.url,
        fallbackUrl = data.media?.redditVideo?.fallbackUrl,
        isVideo = data.isVideo,
        likedByUser = data.likes,
        dir = voteDirection,
    )
}

fun Double.toFullLong(): Long {
    var number = this
    while ((number - number.toLong()) != 0.0) {
        number *= 10
    }
    return number.toLong()
}