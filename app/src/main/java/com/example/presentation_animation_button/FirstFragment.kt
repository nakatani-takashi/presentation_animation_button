package com.example.presentation_animation_button

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.presentation_animation_button.extension.animationClickable
import com.example.presentation_animation_button.extension.fragmentComposable
import kotlinx.coroutines.runBlocking

class FirstFragment : Fragment() {
    private val t = MutableLiveData("初期値だよ！！")
    private fun postTextUpdate(postText: String) {
        t.postValue(postText)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = fragmentComposable(R.id.firstFragment) {

        val postText = t.observeAsState().value
        val interactionSource = remember { MutableInteractionSource() }
        fun route(argText: String) = findNavController().navigate(
            FirstFragmentDirections.actionFirstFragmentToSecondFragment(
                argText
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (postText != null) {
                TextField(
                    value = postText,
                    onValueChange = {
                        postTextUpdate(it)
                    },
                )
            }
            Button(
                onClick = {
                    route(postText ?: "")
                }
            ) {
                Text(
                    text = "普通のボタン",
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            CustomButton(text = "自作のっぺりボタン", modifier = Modifier
                .clickable {
                    route(postText ?: "")
                }
            )
            CustomButton(text = "自作ボタン", modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true),
                ) {
                    route(postText ?: "")
                }
            )
            CustomButton(
                text = "自作アニメーションボタン",
                modifier = Modifier.animationClickable(interactionSource, {
                    route(postText ?: "")
                })
            )
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier.background(color = Color.Gray, shape = RoundedCornerShape(3.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            color = Color.White
        )
    }
}