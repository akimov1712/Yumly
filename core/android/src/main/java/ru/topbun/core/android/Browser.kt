package ru.topbun.core.android

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

object LegalLinks {
    const val PRIVACY_POLICY_URL = "${BuildConfig.BASE_URL}/privacy-policy.html"
    const val USER_AGREEMENT_URL = "${BuildConfig.BASE_URL}/user-agreement.html"
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}
