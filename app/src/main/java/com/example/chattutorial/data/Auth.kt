package com.example.chattutorial.data

object Auth {
    private val uniqueSuffix = (0..9999).random()

    private val adminUser = UserCredentials(
        id = "tutorial-droid",
        name = "Tutorial Droid",
        image = "https://bit.ly/2TIt8NR",
    )

    private val callerUser = UserCredentials(
        id = "tutorial-outgoing",
        name = "Tutorial Outgoing",
        image = "https://cdn1.iconfinder.com/data/icons/zlat-communication-vol-1/25/outgoing_call_calls_phone-512.png",
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtb3V0Z29pbmcifQ.WFr8dRxB8RHkuO2v3RttxrCQKeFgXa04AUymWMmgGOE"
    )

    private val receiverUser = UserCredentials(
        id = "tutorial-incoming",
        name = "Tutorial Incoming",
        image = "https://cdn1.iconfinder.com/data/icons/zlat-communication-vol-1/25/incoming_call_calls_phone-512.png",
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtaW5jb21pbmcifQ.QuCY9BSE1VVTYTc33uf0gBmy7zp7P8X1nU0S_pJcnps"
    )

//    val currentUser = adminUser
//    val currentUser = callerUser
    val currentUser = receiverUser
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
