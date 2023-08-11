package com.pratyaksh.healthykingdom

import android.app.Activity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.pratyaksh.healthykingdom.domain.model.Users
import com.pratyaksh.healthykingdom.ui.change_password.ChangePasswordScreen
import com.pratyaksh.healthykingdom.ui.change_password.ForgotPasswordScreen
import com.pratyaksh.healthykingdom.ui.change_phone.ChangePhoneScreen
import com.pratyaksh.healthykingdom.ui.fluid_update.FluidsUpdateNavScreen
import com.pratyaksh.healthykingdom.ui.fluid_update.FluidsUpdationScreen
import com.pratyaksh.healthykingdom.ui.homepage.HomeScreen
import com.pratyaksh.healthykingdom.ui.hospital_details.HospitalDetailsScreen
import com.pratyaksh.healthykingdom.ui.otp_verification.OtpVerificationScreen
import com.pratyaksh.healthykingdom.ui.user_login.LoginScreen
import com.pratyaksh.healthykingdom.ui.user_registration.OtpVerifyScreen
import com.pratyaksh.healthykingdom.ui.user_registration.RegisterHospital
import com.pratyaksh.healthykingdom.utils.LifeFluids
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow

fun NavGraphBuilder.changePasswordOrPhoneNavGraph(
    activity: Activity,
    navController: NavHostController,
    getCurrentLoggedUser: () -> Flow<Resource<String?>>
) {

    navigation(
        route = Routes.CHANGE_PASS_OR_PHONE_NAVGRAPH.route,
        startDestination = Routes.CHANGE_PHONE_SCREEN.route
    ) {

        var onVerify = { /** WILL BE UPDATED BY VERIFICATION REQUESTER SCREEN **/ }
        var resendToken: ForceResendingToken? = null

        composable(route = Routes.CHANGE_PASSWORD_SCREEN.route) {
            ChangePasswordScreen(
                getCurrentUser = getCurrentLoggedUser(),
                navController = navController,
                setSuccessOtpVerification = {
                    onVerify = it
                },
                activity= activity,
                setResendToken = {
                    resendToken = it
                }
            )
        }

        composable(route = Routes.CHANGE_PHONE_SCREEN.route) {
            ChangePhoneScreen(
                activity= activity,
                getCurrentUser = getCurrentLoggedUser(),
                navController = navController,
                setSuccessOtpVerification = {
                    onVerify = it
                },
                setResendToken = {
                    resendToken = it
                }
            )

        }

        composable(route = Routes.FORGOT_PASSWORD_SCREEN.route) {
            ForgotPasswordScreen(
                getCurrentUser = getCurrentLoggedUser(),
                navController = navController
            )

        }

        composable(route = Routes.OTP_VERIFICATION_SCREEN.route + "/{phone}/{verificationId}",
            arguments = listOf(
                navArgument("phone") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument("verificationId") {
                    nullable = false
                    type = NavType.StringType
                }
            )) {
            OtpVerificationScreen(
                activity = activity,
                onVerify = { onVerify() },
                phone = it.arguments?.getString("phone")!!,
                verificationId = it.arguments?.getString("verificationId")!!,
                resendToken = resendToken!!,
                navController = navController
            )
        }

    }

}

fun NavGraphBuilder.fluidsUpdationNavGraph(
    navController: NavHostController,
    getCurrentLoggedUser: () -> Flow<Resource<String?>>
) {

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
                navArgument(name = "fluidType") {
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
) {

    navigation(
        startDestination = startDestination.route,
        route = Routes.SIGNUP_NAVGRAPH.route
    ) {

        var resendToken: PhoneAuthProvider.ForceResendingToken? = null
        var user: Users? = null

        composable(
            route = Routes.LOGIN_SCREEN.route
        ) {
            LoginScreen(
                navController = navController,
                updateCurrentLoggedUser = updateCurrentLoggedUser
            )
        }

        composable(
            route = Routes.HOSPITAL_REGITER_SCREEN.route
        ) {
            RegisterHospital(
                activity = activity,
                navController = navController
            ) { resToken, reqUser ->
                resendToken = resToken
                user = reqUser
            }
        }

        composable(
            route = Routes.REG_OTP_VALIDATION_SCREEN.route + "/{phone}/{verificationId}",
            arguments = listOf(
                navArgument(name = "phone") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            OtpVerifyScreen(
                onVerify = {
                    navController.navigate(Routes.HOME_NAVGRAPH.route) {
                        popUpTo(Routes.SIGNUP_NAVGRAPH.route) { inclusive = true }
                    }
                },
                phone = it.arguments?.getString("phone")!!,
                verificationId = it.arguments?.getString("verificationId")!!,
                resendToken = resendToken!!,
                activity = activity,
                navController = navController,
                user = user!!,
                updateCurrentLoggedUser = updateCurrentLoggedUser
            )
        }
    }
}

fun NavGraphBuilder.homeScreenNavGraph(
    navController: NavHostController,
    currentLoggedUser: () -> Flow<Resource<String?>>,
    updateCurrentLoggedUser: (userId: String?) -> Flow<Resource<Boolean>>
) {

    navigation(
        startDestination = Routes.HOMESCREEN.route,
        route = Routes.HOME_NAVGRAPH.route
    ) {

        composable(
            route = Routes.HOMESCREEN.route
        ) {
            HomeScreen(
                navController = navController,
                logoutUser = { updateCurrentLoggedUser(null) },
                getLoggedUser = { currentLoggedUser() }
            )
        }

        composable(
            route = Routes.HOSPITAL_DETAILS_SCREEN.route + "/{hospitalId}",
            arguments = listOf(
                navArgument(name = "hospitalId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {

            HospitalDetailsScreen(
                navController = navController,
                hospitalId = it.arguments?.getString("hospitalId")!!
            )

        }

    }


}

