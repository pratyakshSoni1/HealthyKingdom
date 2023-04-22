package com.pratyaksh.healthykingdom.domain.use_case.number_verification

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OtpSendUseCase @Inject constructor(
    private val fbAuth: FirebaseAuth
) {

    operator fun invoke(
        phone:String,
        activity: Activity,
        onVerificationComplete:(credential: PhoneAuthCredential) -> Unit,
        onVerificationFailed:(exception: FirebaseException) -> Unit,
        onCodeSent:(verId: String, resendToken: PhoneAuthProvider.ForceResendingToken) -> Unit,
    ) {
        val callBacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("FBVerification_Completed_LOGS", "${p0.smsCode}")
                    onVerificationComplete(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    onVerificationFailed(p0)
                    p0.printStackTrace()
                    Log.d("FBVerification_Completed_LOGS", "${p0.message}")
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    onCodeSent(
                        p0,
                        p1
                    )
                    Log.d("CodeSent_LOGS", p0)
                }
            }

            val options = PhoneAuthOptions.newBuilder(fbAuth)
                .setPhoneNumber("+$phone")       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity)                 // Activity (for callback binding)
                .setCallbacks(callBacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
}