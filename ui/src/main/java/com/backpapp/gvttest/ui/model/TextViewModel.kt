package com.backpapp.gvttest.ui.model

import androidx.annotation.StringRes

sealed class TextViewModel {
    data class Text(val value: String) : TextViewModel()
    class Resource(@StringRes val resId: Int, vararg val args: Any) : TextViewModel() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Resource

            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }

        override fun toString(): String {
            return "TextViewModel.Resource(" +
                    "resId = $resId," +
                    "args = " + args.joinToString(separator = ",") { it.toString() } +
                    ")"
        }
    }
}