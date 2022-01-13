package com.example.presentation_animation_button.extension

import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

fun Fragment.fragmentComposable(
    fragmentId: Int,
    content: @Composable () -> Unit
): View = ComposeView(requireContext()).apply {
    id = fragmentId
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    setContent(content)
}