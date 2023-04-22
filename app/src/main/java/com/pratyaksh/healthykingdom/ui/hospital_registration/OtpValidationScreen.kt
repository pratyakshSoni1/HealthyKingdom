package com.pratyaksh.healthykingdom.ui.hospital_registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.ui.utils.OtpTextField

@Composable
fun OtpVerifyScreen(
    onVerify: ()->Unit,
    phone:String,
    verificationId: String,
    resendToken: PhoneAuthProvider.ForceResendingToken,
    viewModel: OtpValidationVM = hiltViewModel()
){

    LaunchedEffect(Unit){
        viewModel.initScreen( verificationId, phone, resendToken )
    }

    Column{

        Text(
            text= "Otp Sent to $viewModel."
        )

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){

            Box{
                OtpTextField(
                    text = viewModel.code.value
                )

                TextField(
                    value = viewModel.code.value,
                    onValueChange = { viewModel.onCodeChange(it) },
                    modifier= Modifier.matchParentSize()
                        .alpha(0f),
                )
            }

            Box(
                modifier= Modifier
                    .fillMaxSize()
                    .padding(bottom = 48.dp),
                contentAlignment = Alignment.BottomCenter
            ){

                Button(
                    onClick={
                        onVerify()
                    },
                    modifier = Modifier.fillMaxWidth(0.75f)
                ){
                    Text(
                        text = "Verify"
                    )
                }

            }

        }
    }

}
