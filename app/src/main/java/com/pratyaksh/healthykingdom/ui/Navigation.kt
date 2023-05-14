package com.pratyaksh.healthykingdom.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pratyaksh.healthykingdom.fluidsUpdationNavGraph
import com.pratyaksh.healthykingdom.homeScreenNavGraph
import com.pratyaksh.healthykingdom.registrationNavgraph
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.flow.Flow

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


    }



}