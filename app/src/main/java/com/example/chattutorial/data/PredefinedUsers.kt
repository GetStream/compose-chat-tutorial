package com.example.chattutorial.data

import com.example.chattutorial.data.services.StreamService
import kotlinx.coroutines.runBlocking

val callerUser = UserCredentials(
    id = "tutorial-outgoing",
    name = "Tutorial Outgoing",
    image = "https://cdn1.iconfinder.com/data/icons/zlat-communication-vol-1/25/outgoing_call_calls_phone-512.png",
    chatToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtb3V0Z29pbmcifQ.uVxZaRsqvTEr2OacZdGa145wk7Nydyfk60TrmcHf-ZE",
)

val receiverUser = UserCredentials(
    id = "tutorial-incoming",
    name = "Tutorial Incoming",
    image = "https://cdn1.iconfinder.com/data/icons/zlat-communication-vol-1/25/incoming_call_calls_phone-512.png",
    chatToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtaW5jb21pbmcifQ.VgM_Qzf1eCnhurvvu1tvTH_MW_3A4cjZXEL02lgbbj8",
)

private val adminUser = UserCredentials(
    id = "tutorial-droid",
    name = "Tutorial Droid",
    image = "https://bit.ly/2TIt8NR",
    chatToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.WwfBzU1GZr0brt_fXnqKdKhz3oj0rbDUm2DqJO_SS5U"
)

//val currentUser = adminUser
val currentUser = callerUser
//val currentUser = receiverUser

data class UserCredentials(
    val id: String,
    val name: String,
    val image: String,
    val chatToken: String,
    val videoToken: String = runBlocking {
        StreamService.instance.getAuthData(
            environment = "demo",
            userId = id,
        ).token
    }
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
