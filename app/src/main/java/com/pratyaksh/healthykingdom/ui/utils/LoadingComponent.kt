package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingComponent(modifier: Modifier, text: String? = null){

    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_W5Sk67.json")
    )

    Dialog(
        onDismissRequest = { Unit }
    ){
        Box(
            modifier= modifier,
            contentAlignment = Alignment.Center,
        ){
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                LottieAnimation(
                    composition = lottieComposition, iterations = LottieConstants.IterateForever,
                    modifier= Modifier.fillMaxSize(0.4f)
                )

                text?.let{
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text= it,
                        modifier=Modifier.fillMaxWidth(0.8f),
                        textAlign = TextAlign.Center,
                        color= Color.Black
                    )
                }

            }
        }
    }

}