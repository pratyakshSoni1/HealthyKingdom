package com.pratyaksh.healthykingdom.ui.change_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.ui.utils.SimpleTopBar
import com.pratyaksh.healthykingdom.utils.Resource
import kotlinx.coroutines.flow.Flow

@Composable
fun ForgotPasswordScreen(
    getCurrentUser: Flow<Resource<String?>>,
    navController: NavHostController,
) {

    val viewModel: ForgotPasswordVM = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = true, block = {
        viewModel.initScreen(getCurrentUser)
    })

    Scaffold(
        topBar = {
            SimpleTopBar(onBackPress = { navController.popBackStack() }, title = "Change Password")
        }
    ) {
        Box(
            Modifier
                .padding(it)
                .padding(14.dp, 8.dp), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp, 14.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                AppTextField(
                    value = uiState.newPassTxt,
                    onValueChange = { viewModel.updateNewPassTxt(it) },
                    hint = "New Password",
                    keyboard = KeyboardType.Password
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "cancel",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = {
                                viewModel.onChangePassword().invokeOnCompletion {
                                    navController.popBackStack()
                                    navController.popBackStack()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF0027FF)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(bottom = 14.dp),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Text(
                                "Change",
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }

            }
            if (uiState.isLoading) {
                LoadingComponent(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1f)
                )
            }

            if (uiState.isError) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = uiState.errorTxt, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }


}