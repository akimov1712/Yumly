package ru.topbun.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.assistant.AssistantState.AssistantUiState.NEED_AUTH
import ru.topbun.assistant.AssistantState.AssistantUiState.SUCCESS
import ru.topbun.assistant.components.AssistantHistorySheet
import ru.topbun.assistant.components.AssistantInputBar
import ru.topbun.assistant.components.AssistantPlaceholder
import ru.topbun.assistant.components.AssistantTopBar
import ru.topbun.assistant.components.MessageLimitBlock
import ru.topbun.assistant.components.MessageList
import ru.topbun.assistant.components.RecentChatsSection
import ru.topbun.core.ui.components.UnauthorizedSection
import ru.topbun.core.ui.theme.Colors
import ru.topbun.navigation.auth.AuthScreenProvider

object AssistantScreen: Tab {


    override val options @Composable get() = TabOptions(
        index = 2U,
        title = "Assistant",
        icon = painterResource(ru.topbun.core.ui.R.drawable.ic_tabs_assistant)
    )

    @Composable
    override fun Content() {
        val viewModel: AssistantViewModel = koinViewModel()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow.parent

        LaunchedEffect(Unit) {
            viewModel.sendIntent(AssistantIntent.CheckSession)
        }

        when(state.assistantUiState) {
            SUCCESS -> AssistantContent(
                state = state,
                onIntent = viewModel::sendIntent
            )
            NEED_AUTH -> UnauthorizedSection {
                val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                navigator?.push(screen)
            }
            else -> Box(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxSize()
                    .background(Colors.BACKGROUND)
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssistantContent(
    state: AssistantState,
    onIntent: (AssistantIntent) -> Unit
) {
    var showHistorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showHistorySheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = Colors.WHITE,
            onDismissRequest = { showHistorySheet = false }
        ) {
            AssistantHistorySheet(
                state = state,
                onIntent = onIntent,
                onClose = { showHistorySheet = false }
            )
        }
    }

    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(Colors.BACKGROUND)
            .statusBarsPadding()
            .imePadding()
    ) {
        AssistantTopBar(
            onClickHistory = { showHistorySheet = true },
            onClickNewChat = { onIntent(AssistantIntent.StartNewChat) }
        )
        if (state.selectedChat == null && state.chatList.chats.isNotEmpty()) {
            RecentChatsSection(
                chats = state.chatList.chats.take(6),
                onClickChat = { onIntent(AssistantIntent.SelectChat(it)) },
                onClickAll = { showHistorySheet = true }
            )
        }
        Box(
            modifier = androidx.compose.ui.Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            val selectedChat = state.selectedChat
            if (selectedChat == null) {
                AssistantPlaceholder(
                    hasChats = state.chatList.chats.isNotEmpty(),
                    onOpenHistory = { showHistorySheet = true }
                )
            } else {
                MessageList(messages = selectedChat.messages)
            }
        }
        if (state.isMessageLimitReached) {
            MessageLimitBlock()
        } else {
            AssistantInputBar(
                text = state.messageText,
                enabled = state.canSendMessage,
                isLoading = state.sendMessageStatus.isLoading,
                onValueChange = { onIntent(AssistantIntent.ChangeMessageText(it)) },
                onSend = { onIntent(AssistantIntent.SendMessage) }
            )
        }
    }
}
