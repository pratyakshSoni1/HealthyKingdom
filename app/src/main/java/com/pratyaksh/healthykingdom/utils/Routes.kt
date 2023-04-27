package com.pratyaksh.healthykingdom.utils

sealed class Routes(val route: String) {

    object HOMESCREEN: Routes("homescreen_route")
    object LOGIN_SCREEN: Routes("login_route")
    object HOME_NAVGRAPH: Routes("home_navgraph")
    object SIGNUP_NAVGRAPH : Routes("signup_navgraph")
    object HOSPITAL_REGITER_SCREEN : Routes("hospital_regitration_screen")
    object HOSPITAL_DETAILS_SCREEN : Routes("hospital_details_screen")
    object OTP_VALIDATION_SCREEN : Routes("otp_validation_screen")


}