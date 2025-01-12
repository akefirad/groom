package com.akefirad.groom

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.Bundle"

object Bundle : DynamicBundle(BUNDLE) {

    @JvmStatic
    @Suppress("unused")
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)

    @JvmStatic
    @Suppress("unused")
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getLazyMessage(key, *params)

}
