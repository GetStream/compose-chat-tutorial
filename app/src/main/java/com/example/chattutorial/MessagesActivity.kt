package com.example.chattutorial

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.state.messages.Thread
import io.getstream.chat.android.compose.ui.messages.attachments.AttachmentsPicker
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.composer.components.MessageInput
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.messages.overlay.SelectedMessageOverlay
import io.getstream.chat.android.compose.ui.messages.overlay.defaultMessageOptions
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import io.getstream.chat.android.offline.ChatDomain

class MessagesActivity : AppCompatActivity() {

    // Build the ViewModel factory
    private val factory by lazy {
        MessagesViewModelFactory(
            this,
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager,
            ChatClient.instance(),
            ChatDomain.instance(),
            intent.getStringExtra(KEY_CHANNEL_ID) ?: "",
            30
        )
    }

    // Build the required ViewModels, using the 'factory'
    private val listViewModel: MessageListViewModel by viewModels { factory }
    private val attachmentsPickerViewModel: AttachmentsPickerViewModel by viewModels { factory }
    private val composerViewModel: MessageComposerViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1 - Load the ID of the selected channel
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)

        if (channelId == null) {
            finish()
            return
        }

        // 2 - Add the MessagesScreen to your UI
        setContent {
            ChatTheme(
                shapes = StreamShapes(
                    avatar = RoundedCornerShape(8.dp),
                    attachment = RoundedCornerShape(16.dp),
                    myMessageBubble = RoundedCornerShape(16.dp),
                    otherMessageBubble = RoundedCornerShape(16.dp),
                    inputField = RectangleShape
                )
            ) {
                MyCustomUi()
            }
        }
    }

    @Composable
    fun MyCustomUi() {
        // load the data
        val isShowingAttachments = attachmentsPickerViewModel.isShowingAttachments
        val selectedMessage = listViewModel.currentMessagesState.selectedMessage
        val user by listViewModel.user.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    MyCustomComposer()
                }
            ) {
                MessageList( // build the MessageList and connect the actions
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    viewModel = listViewModel,
                    onThreadClick = { message ->
                        composerViewModel.setMessageMode(Thread(message))
                        listViewModel.openMessageThread(message)
                    }
                )
            }

            // show attachments picker when necessary
            if (isShowingAttachments) {
                AttachmentsPicker(
                    attachmentsPickerViewModel = attachmentsPickerViewModel,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(350.dp),
                    onAttachmentsSelected = { attachments ->
                        attachmentsPickerViewModel.changeAttachmentState(false)
                        composerViewModel.addSelectedAttachments(attachments)
                    },
                    onDismiss = {
                        attachmentsPickerViewModel.changeAttachmentState(false)
                        attachmentsPickerViewModel.dismissAttachments()
                    }
                )
            }

            // Show the overlay if we've selected a message
            if (selectedMessage != null) {
                SelectedMessageOverlay(
                    messageOptions = defaultMessageOptions(selectedMessage, user, listViewModel.isInThread),
                    message = selectedMessage,
                    onMessageAction = { action ->
                        composerViewModel.performMessageAction(action)
                        listViewModel.performMessageAction(action)
                    },
                    onDismiss = { listViewModel.removeOverlay() }
                )
            }
        }
    }

    @Composable
    fun MyCustomComposer() {
        MessageComposer(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            viewModel = composerViewModel,
            integrations = {},
            input = {
                MessageInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f)
                        .padding(start = 8.dp),
                    value = composerViewModel.input,
                    attachments = composerViewModel.selectedAttachments,
                    activeAction = composerViewModel.activeAction,
                    onValueChange = { composerViewModel.setMessageInput(it) },
                    onAttachmentRemoved = { composerViewModel.removeSelectedAttachment(it) },
                    label = {
                        Row(
                            Modifier.wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Keyboard,
                                contentDescription = null,
                                tint = ChatTheme.colors.textLowEmphasis
                            )

                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = "Type something",
                                color = ChatTheme.colors.textLowEmphasis
                            )
                        }
                    }
                )
            }
        )
    }

    // 3 - Create an intent to start this Activity, with a given channelId
    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, MessagesActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}