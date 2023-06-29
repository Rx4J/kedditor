package ru.lanik.network.extension

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import ru.lanik.network.api.dto.commentsDTO.CommentsDto
import ru.lanik.network.api.dto.commentsDTO.CommentsListingDto
import ru.lanik.network.api.dto.postDTO.PostDto
import ru.lanik.network.api.dto.postDTO.PostListingDto
import ru.lanik.network.api.dto.subredditDTO.SubredditDto
import ru.lanik.network.models.Comments
import ru.lanik.network.models.Post
import ru.lanik.network.models.PostWithComments
import ru.lanik.network.models.Subreddit

fun List<SubredditDto>.toListSubreddit(): List<Subreddit> = this.map { item -> item.toSubreddit() }

fun SubredditDto.toSubreddit(): Subreddit {
    return Subreddit(
        id = data.name ?: "",
        name = data.displayName ?: "",
        namePrefixed = data.displayNamePrefixed ?: "",
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
        saved = data.saved ?: false,
        title = data.title ?: "",
        subredditNamePrefixed = data.subredditNamePrefixed ?: "",
        name = data.subreddit ?: "",
        score = data.score ?: 0,
        created = data.created ?: 1.0,
        id = data.name ?: "",
        author = data.author ?: "",
        subredditId = data.subredditId ?: "",
        numComments = data.numComments ?: 0,
        permalink = data.permalink ?: "",
        url = data.url ?: "",
        fallbackUrl = data.media?.redditVideo?.fallbackUrl,
        isVideo = data.isVideo ?: false,
        likedByUser = data.likes,
        thumbnail = data.thumbnail,
        dir = voteDirection,
    )
}

fun List<CommentsDto>.toListComments(): List<Comments> = this.map { item -> item.toComments() }

fun CommentsDto.toComments(): Comments {
    return Comments(
        id = data.id,
        name = data.subreddit ?: "",
        author = data.author,
        ups = data.ups,
        utcTimeStamp = data.createdUtc ?: 1.0,
        commentsBody = data.bodyComments,
        replies = null,
    )
}

fun List<JsonElement>.toPostWithComments(): PostWithComments {
    val format = Json {
        ignoreUnknownKeys = true
    }

    val postDto = format.decodeFromJsonElement<PostListingDto>(first())
    val commentsDto = format.decodeFromJsonElement<CommentsListingDto>(last())
    return PostWithComments(
        post = postDto.data.children.first().toPost(),
        comments = commentsDto.data.children.toListComments(),
    )
}

fun Double.toFullLong(): Long {
    var number = this
    while ((number - number.toLong()) != 0.0) {
        number *= 10
    }
    return number.toLong()
}