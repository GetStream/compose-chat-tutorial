package com.example.chattutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.chattutorial.data.currentUser
import com.example.chattutorial.data.toVideoUser
import io.getstream.android.push.firebase.FirebasePushDeviceGenerator
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.call.state.AcceptCall
import io.getstream.video.android.core.call.state.CancelCall
import io.getstream.video.android.core.call.state.DeclineCall
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.notifications.NotificationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Authentication
        val apiKey = "mmhfdzb5evj2"
        val user = currentUser.toVideoUser()
        val token = currentUser.videoToken
        val callId = "chat-and-video-calls-integration"

        // Stream Video client init
        // In a production app we recommend initializing the client in your Application class or DI module
        val videoClient = if (StreamVideo.isInstalled) {
            StreamVideo.instance()
        } else {
            StreamVideoBuilder(
                context = applicationContext,
                apiKey = apiKey,
                user = user,
                token = token,
                notificationConfig = NotificationConfig(
                    pushDeviceGenerators = listOf(
                        FirebasePushDeviceGenerator(providerName = "firebase"),
                    ),
                ),
            ).build()
        }

        // Create a call
        val call = videoClient.call("default", callId)

        lifecycleScope.launch {
            // We get the chat channel members as an intent extra and use them as call members
            val callMembers = intent.getStringArrayExtra(KEY_MEMBERS_ARRAY)?.asList() ?: emptyList()
            // Passing ring = true will cause a push notification to appear for the receivers
            val result = call.create(ring = true, memberIds = callMembers)
            result.onError {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                Log.e("VideoCallActivity", it.message)
                finish()
            }
        }

        setContent {
            // Request runtime permissions for camera and microphone
            LaunchCallPermissions(call = call)

            VideoTheme {
                RingingCallContent(
                    call = call,
                    onAcceptedContent = {
                        CallContent(
                            call = call,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    onRejectedContent = {
                        rejectCall(call)
                    },
                    onNoAnswerContent = {
                        finish()
                    },
                    onBackPressed = {
                        call.leave()
                        finish()
                    },
                    onCallAction = { callAction ->
                        when (callAction) {
                            is LeaveCall -> {
                                call.leave()
                                finish()
                            }
                            is DeclineCall -> {
                                lifecycleScope.launch {
                                    call.reject()
                                    call.leave()
                                    finish()
                                }
                            }
                            is AcceptCall -> {
                                lifecycleScope.launch {
                                    call.accept()
                                    call.join()
                                }
                            }
                            is CancelCall -> {
                                lifecycleScope.launch {
                                    call.reject()
                                    call.leave()
                                    finish()
                                }
                            }
                            else -> Unit
                        }
                    }
                )
            }
        }
    }

    private fun rejectCall(call: Call) {
        lifecycleScope.launch {
            call.reject()
            withContext(Dispatchers.Main) { finish() }
        }
    }

    companion object {
        const val KEY_MEMBERS_ARRAY: String = "callMembers"

        fun getIntent(context: Context, callMembers: List<String>): Intent {
            return Intent(context, VideoCallActivity::class.java).apply {
                putExtra(KEY_MEMBERS_ARRAY, callMembers.toTypedArray())
            }
        }
    }
}