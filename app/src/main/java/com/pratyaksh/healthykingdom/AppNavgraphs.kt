package com.pratyaksh.healthykingdom

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.firebase.auth.PhoneAuthProvider
import com.pratyaksh.healthykingdom.data.dto.HospitalsDto
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreen
import com.pratyaksh.healthykingdom.ui.hospital_details.HospitalDetailsScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.OtpVerifyScreen
import com.pratyaksh.healthykingdom.ui.hospital_registration.RegisterHospital
import com.pratyaksh.healthykingdom.ui.user_login.LoginScreen
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow

fun NavGraphBuilder.registrationNavgraph(
    startDestination: Routes,
    activity: Activity,
    navController: NavHostController,
    updateCurrentLoggedUser: (userId: String) -> Flow<Resource<Boolean>>
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
            LoginScreen(
                navController = navController,
                activity = activity,
                updateCurrentLoggedUser= updateCurrentLoggedUser
            )
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
    navController: NavHostController,
    currentLoggedUser: Flow<Resource<String?>>
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

