package `in`.shabinder.soundbound.models

import androidx.compose.runtime.Immutable


import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Immutable
open class ThrowableWrapper(
    override val message: String,
    val stackTrace: String = "",
    override val cause: ThrowableWrapper? = null,
) : Throwable(message, cause) {

    @Transient
    var reference: Throwable? = null

    constructor(throwable: Throwable) : this(
        message = throwable.message ?: "",
        stackTrace = throwable.stackTraceToString(),
        cause = throwable.cause?.toThrowableWrapper()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is ThrowableWrapper && reference != null && other.reference == reference) return true
        if (other is Throwable && reference != null && reference == other) return true

        if (other !is ThrowableWrapper) return false
        if (message != other.message) return false
        if (stackTrace != other.stackTrace) return false
        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + stackTrace.hashCode()
        return result
    }

    open fun copy(
        message: String = this.message,
        stackTrace: String = this.stackTrace,
        cause: ThrowableWrapper? = this.cause,
    ): ThrowableWrapper {
        return ThrowableWrapper(message, stackTrace, cause)
    }

    override fun toString(): String {
        return reference?.toString()
            ?: (message + "\nStacktrace:\n" + stackTrace + "\nCause:\n" + cause?.toString())
    }
}

fun Throwable.toThrowableWrapper() =
    if (this is ThrowableWrapper) this else ThrowableWrapper(this).apply {
        reference = this@toThrowableWrapper
    }