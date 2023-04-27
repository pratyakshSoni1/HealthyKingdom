package com.pratyaksh.healthykingdom

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreen
import com.pratyaksh.healthykingdom.ui.hospital_details.HospitalDetailsScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.OtpVerifyScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.RegisterHospital
import com.pratyaksh.healthykingdom.ui.user_login.LoginScreen
import com.pratyaksh.healthykingdom.utils.Routes

fun NavGraphBuilder.registrationNavgraph(
    startDestination: Routes,
    activity: Activity,
    navController: NavHostController
){

    navigation(
        startDestination = startDestination.route,
        route = Routes.SIGNUP_NAVGRAPH.route
    ){

        var resendToken: PhoneAuthProvider.ForceResendingToken? = null
        var hospitalDto: HospitalsDto? = null

        composable(
            route = Routes.LOGIN_SCREEN.route
        ){
            LoginScreen(navController = navController, activity = activity)
        }

        composable(
            route = Routes.HOSPITAL_REGITER_SCREEN.route
        ){
            RegisterHospital(
                activity = activity,
                navController= navController
            ){ resToken, hospital ->
                resendToken = resToken
                hospitalDto = hospital
            }
        }

        composable(
            route = Routes.OTP_VALIDATION_SCREEN.route+"/{phone}/{verificationId}",
            arguments = listOf(
                navArgument( name = "phone"){
                    type = NavType.StringType
                    nullable = false
                }
            )
        ){
            OtpVerifyScreen(
                onVerify = {
//                    navController.clearBackStack()
                    navController.navigate(Routes.HOMESCREEN.route){
                        popUpTo(Routes.SIGNUP_NAVGRAPH.route){ inclusive = true }
                    }
                },
                phone= it.arguments?.getString("phone")!!,
                verificationId = it.arguments?.getString("verificationId")!!,
                resendToken = resendToken!!,
                activity = activity,
                hospitalDto = hospitalDto!!
            )
        }
    }
}

fun NavGraphBuilder.homeScreenNavGraph(
    navController: NavHostController
){

    navigation(
        startDestination = Routes.HOMESCREEN.route,
        route= Routes.HOME_NAVGRAPH.route
    ){

        composable(
            route= Routes.HOMESCREEN.route
        ){
            HomeScreen(
                navController = navController
            )
        }

        composable(
            route = Routes.HOSPITAL_DETAILS_SCREEN.route+"/{hospitalId}",
            arguments = listOf(
                navArgument(name= "hospitalId"){
                    type = NavType.StringType
                    nullable = false
                }
            )
        ){

            HospitalDetailsScreen(
                navController = navController,
                hospitalId = it.arguments?.getString("hospitalId")!!
            )

        }

    }


}

@Composable
fun NavGraphBuilder.SettingsNavGraph(){

}

