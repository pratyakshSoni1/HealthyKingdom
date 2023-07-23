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


}