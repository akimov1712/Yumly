package ru.topbun.assistant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.koin.compose.viewmodel.koinViewModel
import ru.topbun.assistant.AssistantState.AssistantUiState.NEED_AUTH
import ru.topbun.assistant.AssistantState.AssistantUiState.SUCCESS
import ru.topbun.assistant.components.AssistantInputBar
import ru.topbun.assistant.components.AssistantPlaceholder
import ru.topbun.assistant.components.ChatLoadError
import ru.topbun.assistant.components.ChatLoadingList
import ru.topbun.assistant.components.Header
import ru.topbun.assistant.components.HistoryDialog
import ru.topbun.assistant.components.MessageLimitBlock
import ru.topbun.assistant.components.MessageList
import ru.topbun.core.ui.components.UnauthorizedSection
import ru.topbun.domain.ScreenUiState
import ru.topbun.navigation.auth.AuthScreenProvider

object AssistantScreen: Tab {


    override val options @Composable get() = TabOptions(
        index = 2U,
        title = "Ассистент",
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
            SUCCESS -> AssistantContent()
            NEED_AUTH -> UnauthorizedSection {
                val screen = ScreenRegistry.get(AuthScreenProvider.Login)
                navigator?.push(screen)
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssistantContent(
    viewModel: AssistantViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedChat = state.selectedChat
    val visibleMessages = state.visibleMessages
    val messageListState = rememberLazyListState()
    val density = LocalDensity.current
    val insets = WindowInsets.systemBars.asPaddingValues()
    var topBarHeight by remember { mutableStateOf(0.dp) }
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val chatContentPadding = PaddingValues(
        start = 12.dp,
        end = 12.dp,
        top = 20.dp + topBarHeight + insets.calculateTopPadding(),
        bottom = 20.dp + bottomBarHeight + 20.dp
    )

    LaunchedEffect(visibleMessages.size, visibleMessages.lastOrNull()?.id) {
        if (visibleMessages.isNotEmpty()) {
            messageListState.animateScrollToItem(visibleMessages.lastIndex)
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.selectedChatStatus.isLoading -> ChatLoadingList(contentPadding = chatContentPadding)
                state.selectedChatStatus.isError -> ChatLoadError(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = topBarHeight + insets.calculateTopPadding(),
                            bottom = bottomBarHeight
                        ),
                    onRetry = { viewModel.sendIntent(AssistantIntent.RetryLoadSelectedChat) }
                )
                visibleMessages.isNotEmpty() -> {
                    MessageList(
                        messages = visibleMessages,
                        state = messageListState,
                        contentPadding = chatContentPadding
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding()
        ){
            Header(
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    topBarHeight = with(density) { coordinates.size.height.toDp() }
                },
                onClickHistory = { viewModel.sendIntent(AssistantIntent.ChangeShowHistoryDialog(true)) },
                onClickNewChat = { viewModel.sendIntent(AssistantIntent.StartNewChat) }
            )

            if (
                selectedChat == null &&
                visibleMessages.isEmpty() &&
                state.selectedChatStatus != ScreenUiState.Loading &&
                state.selectedChatStatus != ScreenUiState.Error
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                        .fillMaxWidth()
                        .padding(bottom = 20.dp + bottomBarHeight),
                    contentAlignment = Alignment.Center
                ){
                    AssistantPlaceholder{ viewModel.sendIntent(AssistantIntent.ChangeShowHistoryDialog(true)) }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            if (state.isMessageLimitReached) {
                MessageLimitBlock()
            }
            if (
                !state.isMessageLimitReached &&
                !state.selectedChatStatus.isLoading &&
                !state.selectedChatStatus.isError
            ){
                AssistantInputBar(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        bottomBarHeight = with(density) { coordinates.size.height.toDp() }
                    },
                    text = state.messageText,
                    enabled = state.canSendMessage,
                    isLoading = state.sendMessageStatus.isLoading,
                    onValueChange = { viewModel.sendIntent(AssistantIntent.ChangeMessageText(it)) },
                    onSend = { viewModel.sendIntent(AssistantIntent.SendMessage) }
                )
            }
        }

    }

    if (state.showHistoryDialog) {
        HistoryDialog(
            state = state,
            onIntent = { viewModel.sendIntent(it) },
            onDismissRequest = { viewModel.sendIntent(AssistantIntent.ChangeShowHistoryDialog(false)) }
        )
    }
}
