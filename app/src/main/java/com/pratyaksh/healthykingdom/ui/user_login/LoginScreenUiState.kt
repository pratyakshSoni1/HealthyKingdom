package com.pratyaksh.healthykingdom.ui.user_login

import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow

data class LoginScreenUiState (

    val phone: String = "",
    val password: String = "",
    val accountType: AccountTypes = AccountTypes.PUBLIC_USER,
    val isAccMenuExpanded: Boolean = false,
    val loginStatus: Flow<Resource<String?>>? = null,
    val isLoading: Boolean = false

    )