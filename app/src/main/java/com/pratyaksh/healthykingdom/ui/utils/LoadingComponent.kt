package com.pratyaksh.healthykingdom.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingComponent(modifier: Modifier){

    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_W5Sk67.json")
    )

    Box(
        modifier= modifier,
        contentAlignment = Alignment.Center
    ){
        LottieAnimation(
            composition = lottieComposition, iterations = LottieConstants.IterateForever,
            modifier= Modifier.fillMaxSize(0.4f)
        )
    }

}