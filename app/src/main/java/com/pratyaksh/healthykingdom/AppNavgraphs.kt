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
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.ui.fluid_update.FluidsUpdateNavScreen
import com.pratyaksh.healthykingdom.ui.fluid_update.FluidsUpdationScreen
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreen
import com.pratyaksh.healthykingdom.ui.hospital_details.HospitalDetailsScreen
import com.pratyaksh.healthykingdom.ui.user_registration.OtpVerifyScreen
import com.pratyaksh.healthykingdom.ui.user_registration.RegisterHospital
import com.pratyaksh.healthykingdom.ui.user_login.LoginScreen
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow

fun NavGraphBuilder.fluidsUpdationNavGraph(
    navController: NavHostController,
    getCurrentLoggedUser:() -> Flow<Resource<String?>>
){

    navigation(
        route = Routes.FLUIDS_UPDATION_NAVGRAPH.route,
        startDestination = Routes.FLUIDS_SELECTION_SCREEN.route,

    ) {
        composable(
            route = Routes.FLUIDS_SELECTION_SCREEN.route
        ) {
            FluidsUpdateNavScreen(navController)
        }

        composable(
            route = Routes.FLUIDS_UPDATION_SCREEN.route + "/{fluidType}",
            arguments = listOf(
                navArgument(name="fluidType"){
                    type = NavType.StringType
                }
            )
        ) {
            FluidsUpdationScreen(
                navController,
                fluidType = when (it.arguments?.getString("fluidType")!!) {
                    LifeFluids.BLOOD.name -> LifeFluids.BLOOD
                    LifeFluids.PLASMA.name -> LifeFluids.PLASMA
                    LifeFluids.PLATELETS.name -> LifeFluids.PLATELETS
                    else -> null
                },
                getCurrentUser = getCurrentLoggedUser
            )
        }
    }
}

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
        var user: Users? = null

        composable(
            route = Routes.LOGIN_SCREEN.route
        ){
            LoginScreen(
                navController = navController,
                updateCurrentLoggedUser= updateCurrentLoggedUser
            )
        }

        composable(
            route = Routes.HOSPITAL_REGITER_SCREEN.route
        ){
            RegisterHospital(
                activity = activity,
                navController= navController
            ){ resToken, reqUser ->
                resendToken = resToken
                user = reqUser
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
                    navController.navigate(Routes.HOMESCREEN.route){
                        popUpTo(Routes.SIGNUP_NAVGRAPH.route){ inclusive = true }
                    }
                },
                phone= it.arguments?.getString("phone")!!,
                verificationId = it.arguments?.getString("verificationId")!!,
                resendToken = resendToken!!,
                activity = activity,
                user = user!!
            )
        }
    }
}

fun NavGraphBuilder.homeScreenNavGraph(
    navController: NavHostController,
    currentLoggedUser:() -> Flow<Resource<String?>>,
    updateCurrentLoggedUser: (userId: String?) -> Flow<Resource<Boolean>>
){

    navigation(
        startDestination = Routes.HOMESCREEN.route,
        route= Routes.HOME_NAVGRAPH.route
    ){

        composable(
            route= Routes.HOMESCREEN.route
        ){
            HomeScreen(
                navController = navController,
                logoutUser = { updateCurrentLoggedUser(null) },
                getLoggedUser = { currentLoggedUser() }
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

