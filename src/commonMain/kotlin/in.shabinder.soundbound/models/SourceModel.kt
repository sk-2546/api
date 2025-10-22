package `in`.shabinder.soundbound.models

import androidx.compose.runtime.Immutable


import kotlinx.serialization.Serializable


@Immutable
@Serializable
open class SourceModel(
    open val sourceName: String,
    open val sourceURL: String,
    open val isActive: Boolean
) {

    constructor(
        sourceName: String,
        sourceURL: String
    ) : this(
        sourceName = sourceName,
        sourceURL = sourceURL,
        isActive = true
    )

    @kotlin.jvm.JvmOverloads
    open fun copy(
        sourceName: String = this.sourceName,
        sourceURL: String = this.sourceURL,
        isActive: Boolean = this.isActive
    ): SourceModel {
        return SourceModel(
            sourceName = sourceName,
            sourceURL = sourceURL,
            isActive = isActive
        )
    }

    override fun toString(): String {
        return "SourceModel(sourceName=$sourceName, sourceURL=$sourceURL, isActive=$isActive)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SourceModel) return false
        if (sourceName != other.sourceName) return false
        if (sourceURL != other.sourceURL) return false
        if (isActive != other.isActive) return false
        return true
    }

    override fun hashCode(): Int {
        var result = sourceName.hashCode()
        result = 31 * result + sourceURL.hashCode()
        result = 31 * result + isActive.hashCode()
        return result
    }

    companion object {
        @Suppress("FunctionName") // Factory method
        val LocalSource: SourceModel = SourceModel(
            sourceName = "Local",
            sourceURL = "::Local",
            isActive = true
        )
    }
}
