package com.pratyaksh.healthykingdom.ui.user_login

import androidx.lifecycle.ViewModel
import com.pratyaksh.healthykingdom.domain.repository.RemoteFirebaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginScreenVM @Inject constructor(
    private val firebaseRepo: RemoteFirebaseRepo
): ViewModel() {


}