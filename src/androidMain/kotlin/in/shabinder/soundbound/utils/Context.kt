package `in`.shabinder.soundbound.utils

import android.content.Context
import androidx.compose.runtime.Immutable

actual typealias Context = AppContextProvider

@Immutable
open class AppContextProvider(
    val value: Context,
    val activityContext: () -> Context? = { null }
)
// additional activity context, since we operate in single activity mode