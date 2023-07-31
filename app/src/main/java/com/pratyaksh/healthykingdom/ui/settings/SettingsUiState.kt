package com.pratyaksh.healthykingdom.ui.settings

data class SettingsUiState (
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val userId: String? = null,
    val goLive: Boolean = false,
    val verifyDialogPassTxt: String ="",
    val showLiveLocPermit: Boolean = false,
    val showVerifyPassDialog: Boolean = false
)