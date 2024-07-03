// This file defines a custom activity for handling audio calls with additional UI features specific to Stream's video calling SDK.
package com.example.chattutorial

import android.content.Intent
import io.getstream.video.android.compose.ui.ComposeStreamCallActivity
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.notifications.NotificationHandler

// Extends the ComposeStreamCallActivity class to provide a custom UI for the calling screen.
class CustomCallActivity : ComposeStreamCallActivity() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NotificationHandler.ACTION_ACCEPT_CALL) {
            val activeCall = StreamVideo.instance().state.activeCall.value
            if (activeCall != null) {
                end(activeCall)
                finish()
                startActivity(intent)
            }
        }
    }
}
