package com.pratyaksh.healthykingdom.ui.hospital_registration

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

@Composable
fun RegisterHospital(activity: Activity){

    var verificationId by remember { mutableStateOf("") }
    var resendToken: PhoneAuthProvider.ForceResendingToken? by remember { mutableStateOf(null) }
    val fbAuth = remember{ mutableStateOf( FirebaseAuth.getInstance() ) }
    val isRegStep = remember{ mutableStateOf( true ) }
    val context = LocalContext.current

    val callBacks = remember{ mutableStateOf(
        object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("FBVerification_Completed_LOGS", "${p0.smsCode}")
                signInWithPhoneAuthCredential(
                    activity, p0,
                    onVerifySuccess = { Toast.makeText( context, "Auto Auth Success", Toast.LENGTH_SHORT ).show() },
                    auth = fbAuth.value
                )
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                p0.printStackTrace()
                Log.d("FBVerification_Completed_LOGS", "${p0.message}")
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                verificationId = p0
                resendToken = p1
                isRegStep.value = false
                Log.d("CodeSent_LOGS", p0)
            }
        }
    ) }

    if(isRegStep.value){

        Box(
            Modifier.fillMaxSize()
        ){
            Button(
                onClick= {

                    val options = PhoneAuthOptions.newBuilder(fbAuth.value)
                        .setPhoneNumber("+916393205531")       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(callBacks.value)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                }
            ){
                Text(
                    text= "Register"
                )
            }

        }
    }else{
        val context = LocalContext.current
        OtpVerify(onVerifySuccess = {
            Toast.makeText( context, "Auth Success", Toast.LENGTH_SHORT ).show()
        }, verificationId = verificationId, fbAuth = fbAuth, activity = activity)
    }

}

@Composable
fun OtpVerify(
    onVerifySuccess:()->Unit,
    verificationId: String,
    fbAuth: State<FirebaseAuth>,
    activity: Activity

    ){

    Box(
        Modifier.fillMaxSize()
    ){
        Button(
            onClick= {
                val credentials = PhoneAuthProvider.getCredential(verificationId, "111111")
                signInWithPhoneAuthCredential(activity, credentials, fbAuth.value, onVerifySuccess)
            }
        ){
            Text(
                text= "Verify"
            )
        }

    }

}

private fun signInWithPhoneAuthCredential(activity: Activity, credential: PhoneAuthCredential, auth: FirebaseAuth, onVerifySuccess: () -> Unit) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("FB_AUTH_LOGS", "signInWithCredential:success")
                onVerifySuccess()
//                val user = task.result?.user
            } else {
                // Sign in failed, display a message and update the UI
                Log.w("FB_AUTH_LOGS", "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Log.w("FB_AUTH_LOGS", "sThe verification code entered was invalid")
                }
            }
        }
}




