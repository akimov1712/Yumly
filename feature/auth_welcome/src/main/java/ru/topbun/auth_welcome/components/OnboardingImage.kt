package ru.topbun.auth_welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
internal fun OnboardingImage() {
    Image(
        modifier = Modifier.fillMaxWidth(),
        painter = painterResource(ru.topbun.core.ui.R.drawable.img_onboarding),
        contentDescription = "онбординг",
        contentScale = ContentScale.FillWidth
    )
}