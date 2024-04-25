package com.example.chattutorial.data

import com.example.chattutorial.data.services.StreamService
import kotlinx.coroutines.runBlocking

object Auth {
    private val uniqueSuffix = (0..9999).random()

    private val adminUser = UserCredentials(
        id = "tutorial-droid",
        name = "Tutorial Droid",
        image = "https://bit.ly/2TIt8NR",
    )

    val callerUser = UserCredentials(
        id = "tutorial-outgoing",
        name = "Tutorial Outgoing",
        image = "https://cdn1.iconfinder.com/data/icons/zlat-communication-vol-1/25/outgoing_call_calls_phone-512.png",
        token = runBlocking {
            StreamService.instance.getAuthData(
                environment = "demo",
                userId = "tutorial-outgoing",
            )
        }.token
    )

    private val receiverUser = UserCredentials(
        id = "tutorial-incoming",
        name = "Tutorial Incoming",
        image = "https://cdn1.iconfinder.com/data/icons/zlat-communication-vol-1/25/incoming_call_calls_phone-512.png",
        token = runBlocking {
            StreamService.instance.getAuthData(
                environment = "demo",
                userId = "tutorial-incoming",
            )
        }.token
    )

//    val currentUser = adminUser
//    val currentUser = callerUser
    val currentUser = receiverUser

    var apiKey: String

    init {
        val authData = runBlocking {
            StreamService.instance.getAuthData(
                environment = "demo",
                userId = currentUser.id,
            )
        }

        currentUser.token = authData.token
        apiKey = authData.apiKey
    }
}

data class UserCredentials(
    val id: String,
    val name: String,
    val image: String,
    var token: String = "",
)

fun UserCredentials.toChatUser() = io.getstream.chat.android.models.User(
    id = id,
    name = name,
    image = image,
)

fun UserCredentials.toVideoUser() = io.getstream.video.android.model.User(
    id = id,
    name = name,
    image = image,
)
