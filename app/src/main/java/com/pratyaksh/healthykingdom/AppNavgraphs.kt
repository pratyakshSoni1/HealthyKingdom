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
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreen
import com.pratyaksh.healthykingdom.ui.hospital_details.HospitalDetailsScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.OtpVerifyScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.RegisterHospital
import com.pratyaksh.healthykingdom.utils.Routes

@Composable
fun NavGraphBuilder.RegistrationNavgraph(
    startDestination: Routes,
    activity: Activity,
    navController: NavHostController
){

    navigation(
        startDestination = Routes.HOSPITAL_REGITER_SCREEN.route,
        route = Routes.SIGNUP_NAVGRAPH.route
    ){

        val resendToken: MutableState<PhoneAuthProvider.ForceResendingToken?> = remember{ mutableStateOf(null) }

        composable(
            route = Routes.HOSPITAL_REGITER_SCREEN.route
        ){
            RegisterHospital(
                activity = activity,
                navController= navController
            ){
                resendToken.value = it
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
                    navController.navigate(Routes.HOMESCREEN.route)
                },
                phone= it.arguments?.getString("phone")!!,
                verificationId = it.arguments?.getString("verificationId")!!,
                resendToken = resendToken.value!!
            )
        }
    }
}

@Composable
fun NavGraphBuilder.HomeScreenNavGraph(){

}

@Composable
fun NavGraphBuilder.SettingsNavGraph(){

}

