package com.pratyaksh.healthykingdom.ui.user_login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.LoadingComponent
import com.pratyaksh.healthykingdom.utils.AccountTypes
import com.pratyaksh.healthykingdom.utils.Resource
import com.pratyaksh.healthykingdom.utils.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    navController: NavController,
    activity: Activity,
    viewModel: LoginScreenVM = hiltViewModel(),
    updateCurrentLoggedUser: (userId: String) -> Flow<Resource<Boolean>>
) {
    val context = LocalContext.current
    Box{
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(12.dp))
            Text(
                "Login",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(12.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "Account Type: ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.width(4.dp))

                Box(Modifier.clickable {
                    viewModel.toggleAccMenu()
                }) {
                    AccTypeMenuItem(
                        name = viewModel.uiState.accountType.type,
                        painterResource(id = viewModel.uiState.accountType.img)
                    )
                }

                DropdownMenu(
                    expanded = viewModel.uiState.isAccMenuExpanded,
                    onDismissRequest = { viewModel.toggleAccMenu(false) }
                ) {
                    DropdownMenuItem(onClick = {
                        viewModel.onAccChange(AccountTypes.AMBULANCE)
                    }) {
                        AccTypeMenuItem(
                            name = "AMBULANCE",
                            img = painterResource(id = R.drawable.ambulance)
                        )
                    }

                    DropdownMenuItem(onClick = {
                        viewModel.onAccChange(AccountTypes.HOSPITAL)
                    }) {
                        AccTypeMenuItem(
                            name = "HOSPITAL",
                            img = painterResource(id = R.drawable.hospital)
                        )
                    }

                    DropdownMenuItem(onClick = {
                        viewModel.onAccChange(AccountTypes.HOSPITAL)
                    }) {
                        AccTypeMenuItem(
                            name = "Public User",
                            img = painterResource(id = R.drawable.ic_person)
                        )
                    }
                }
            }


            AppTextField(
                value = viewModel.uiState.phone,
                onValueChange = viewModel::onPhoneChange,
                hint = "Phone: +1 12345..."
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                value = viewModel.uiState.password,
                onValueChange = viewModel::onPassChange,
                hint = "Password"
            )
            Spacer(Modifier.height(8.dp))

            Text(
                buildAnnotatedString {
                    append("New here ? ")
                    append("Register")
                    addStyle(SpanStyle(color = Color.Blue), 10, 19)
                    addStyle(SpanStyle(color = Color.LightGray), 0, 10)
                },
                modifier = Modifier.clickable {
                    navController.navigate(Routes.HOSPITAL_REGITER_SCREEN.route)
                }
            )
            Spacer(Modifier.height(8.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF007BFF)
                ),
                onClick = {
                    viewModel.onLogin()
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.uiState.loginStatus?.collectLatest {
                            when (it) {
                                is Resource.Error -> withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "No user found, Login Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.toggleLoadingCmp(false)
                                }

                                is Resource.Loading -> withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Processing...", Toast.LENGTH_SHORT).show()
                                    viewModel.toggleLoadingCmp(true)
                                }

                                is Resource.Success -> {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Login Successfull",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        updateCurrentLoggedUser(it.data!!)
                                        navController.navigate(Routes.HOME_NAVGRAPH.route) {
                                            popUpTo(Routes.SIGNUP_NAVGRAPH.route) { inclusive = true }
                                        }
                                    }
                                    viewModel.toggleLoadingCmp(false)
                                }
                            }
                        }
                    }
                }
            ) {
                Text("Login", color = Color.White)
            }
        }

        if( viewModel.uiState.isLoading ){
            LoadingComponent(modifier = Modifier.fillMaxSize().background(Color(0x37000000)) )
        }

    }

}

@Composable
private fun AccTypeMenuItem(
    name: String,
    img: Painter
){
    Row(
        Modifier
            .wrapContentSize()
            .padding(vertical = 4.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){

        Image(
            painter = img, contentDescription = name,
            modifier= Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(8.dp))
        Text(name, fontSize = 16.sp)

    }
}