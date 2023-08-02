package com.pratyaksh.healthykingdom.utils

sealed class Routes(val route: String) {

    object HOMESCREEN: Routes("homescreen_route")
    object LOGIN_SCREEN: Routes("login_route")
    object HOME_NAVGRAPH: Routes("home_navgraph")
    object SIGNUP_NAVGRAPH : Routes("signup_navgraph")
    object HOSPITAL_REGITER_SCREEN : Routes("hospital_regitration_screen")
    object HOSPITAL_DETAILS_SCREEN : Routes("hospital_details_screen")
    object OTP_VALIDATION_SCREEN : Routes("otp_validation_screen")

    object FLUIDS_UPDATION_NAVGRAPH: Routes("fluids_updation_navgraph")
    object FLUIDS_SELECTION_SCREEN: Routes("fluids_selection_screen")
    object FLUIDS_UPDATION_SCREEN: Routes("fluids_updation_screen"){
        fun withArgs(
            fluidType: LifeFluids
        ): String{
            return "$route/${fluidType.name}"
        }
    }

    object REQUESTS_UPDATION_SCREEN: Routes("requests_updation_screen"){
        fun withArgs(
            hospitalId: String
        ) = "$route/${hospitalId}"
    }

    object CHANGE_PHONE_SCREEN: Routes("change_phone_screen")
    object CHANGE_PASSWORD_SCREEN: Routes("change_password_screen")

    object OTP_VERIFICATION_SCREEN: Routes("opt_verification_screen")
    object FORGOT_PASSWORD_SCREEN: Routes("forgot_password_screen")

    object CHANGE_PASS_OR_PHONE_NAVGRAPH: Routes("change_password_or_phone_navgraph")


    object SETTINGS_SCREEN: Routes("settings_screen")
    object PROFILE_SCREEN: Routes("profile_screen")


}