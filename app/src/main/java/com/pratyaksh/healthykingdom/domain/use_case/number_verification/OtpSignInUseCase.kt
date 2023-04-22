package com.pratyaksh.healthykingdom.domain.use_case.number_verification

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import java.lang.Exception
import javax.inject.Inject

class OtpSignInUseCase @Inject constructor(
    private val fbAuth: FirebaseAuth
) {

    operator fun invoke(
        activity: Activity, credential: PhoneAuthCredential,
        onVerifySuccess: () -> Unit,
        onVerificationFailed: (exception: Exception) -> Unit,
        onInvalidCoe: () -> Unit
    ) {
        fbAuth.signInWithCredential(credential)
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
                        onInvalidCoe()
                    }else{
                        onVerificationFailed(task.exception!!)
                    }

                }
            }
    }

}