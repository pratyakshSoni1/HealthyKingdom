package com.pratyaksh.healthykingdom.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pratyaksh.healthykingdom.SettingsNavGraph
import com.pratyaksh.healthykingdom.fluidsUpdationNavGraph
import com.pratyaksh.healthykingdom.homeScreenNavGraph
import com.pratyaksh.healthykingdom.registrationNavgraph
import com.pratyaksh.healthykingdom.ui.request_update.RequestUpdationScreen
import com.pratyaksh.healthykingdom.ui.settings.SettingsScreen
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last

@Composable
fun Navigation(
    startDestination: Routes,
    activity: Activity,
    getCurrentLoggedUser:() -> Flow<Resource<String?>>,
    updateCurrentLoggedUser: (userId: String?) -> Flow<Resource<Boolean>>
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ){

        homeScreenNavGraph(
            navController,
            getCurrentLoggedUser,
            updateCurrentLoggedUser
        )

        registrationNavgraph(
            startDestination = Routes.LOGIN_SCREEN,
            activity = activity,
            navController = navController,
            updateCurrentLoggedUser= { updateCurrentLoggedUser(it) }
        )

        fluidsUpdationNavGraph(
            navController,
            getCurrentLoggedUser
        )

        composable(
            route = Routes.REQUESTS_UPDATION_SCREEN.route+"/{hospitalId}",
            arguments = listOf(
                navArgument(
                    name= "hospitalId"
                ) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ){
            RequestUpdationScreen(navController = navController, hospitalId = it.arguments?.getString("hospitalId") )
        }

        composable(
            route = Routes.SETTINGS_SCREEN.route
        ){
            SettingsScreen(
                getCurrentUser = getCurrentLoggedUser,
                logoutUser = { updateCurrentLoggedUser(null) },
                navController = navController
            )
        }

    }



}