package com.pratyaksh.healthykingdom.ui.user_login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pratyaksh.healthykingdom.R
import com.pratyaksh.healthykingdom.ui.utils.AccTypeMenuItem
import com.pratyaksh.healthykingdom.ui.utils.AccountTypeChooser
import com.pratyaksh.healthykingdom.ui.utils.AppTextField
import com.pratyaksh.healthykingdom.ui.utils.ErrorDialog
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
    viewModel: LoginScreenVM = hiltViewModel(),
    updateCurrentLoggedUser: (userId: String) -> Flow<Resource<Boolean>>
) {
    val context = LocalContext.current
    Box(
        contentAlignment= Alignment.Center
    ){
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(12.dp))
            Text(
                "Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            )
            Spacer(Modifier.height(32.dp))

            AccountTypeChooser(
                    isExpanded = viewModel.uiState.isAccMenuExpanded,
                    accountType = viewModel.uiState.accountType,
                    onToggleExpand = viewModel::toggleAccMenu,
                    onAccChange = viewModel::onAccChange,
                    onToggle = viewModel::toggleAccMenu
                )
            Spacer(Modifier.height(12.dp))

            AppTextField(
                value = viewModel.uiState.phone,
                onValueChange = viewModel::onPhoneChange,
                hint = "Phone: +1 12345...",
                keyboard = KeyboardType.Number
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                value = viewModel.uiState.password,
                onValueChange = viewModel::onPassChange,
                hint = "Password",
                keyboard = KeyboardType.Password
            )
            Spacer(Modifier.height(8.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF007BFF)
                ),
                onClick = {
                    if(viewModel.uiState.phone.contains(Regex("^[0-9]")) || viewModel.uiState.phone.length == 12){
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
                                                .collectLatest { resp ->
                                                    viewModel.toggleLoadingCmp(true)
                                                    when(resp){
                                                        is Resource.Success -> {
                                                            viewModel.addLocalSettings(it.data)
                                                            viewModel.toggleLoadingCmp(false)
                                                            navController.navigate(Routes.HOME_NAVGRAPH.route) {
                                                                popUpTo(Routes.SIGNUP_NAVGRAPH.route) { inclusive = true }
                                                            }
                                                        }
                                                        is Resource.Error -> {
                                                            viewModel.toggleErrorDialog(true, resp.msg!!){
                                                                viewModel.toggleErrorDialog(false)
                                                            }
                                                        }
                                                        is Resource.Loading -> {
                                                            viewModel.toggleLoadingCmp(true)
                                                        }
                                                    }
                                                }
                                        }
                                        viewModel.toggleLoadingCmp(false)
                                    }
                                }
                            }
                        }
                    }else{
                        viewModel.toggleErrorDialog(true, "Enter a valid number with country code ( like 91<Your Phone> for +91"){
                            viewModel.toggleErrorDialog(false)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape= RoundedCornerShape(100.dp)
            ) {
                Text("Login", color = Color.White, fontSize= 16.sp)
            }
            Spacer(Modifier.height(8.dp))

            Text(
                buildAnnotatedString {
                    append("New here ? ")
                    append("Register")
                    addStyle(SpanStyle(color = Color.Blue), 10, 19)
                    addStyle(SpanStyle(color = Color.LightGray), 0, 10)
                },
                modifier = Modifier.clickable(
                    indication= null,
                    interactionSource = MutableInteractionSource()
                ) {
                    navController.navigate(Routes.HOSPITAL_REGITER_SCREEN.route){
                        launchSingleTop = true
                    }
                }
            )
        }

        if( viewModel.uiState.isLoading ){
            LoadingComponent(
                modifier = Modifier
                    .fillMaxWidth(0.9f).aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                text= "Please Wait"
            )
        }

        if(viewModel.uiState.showError) {
            ErrorDialog(
                text = viewModel.uiState.errorText,
                onClose= {
                    viewModel.uiState.onErrorCloseAction()
                }
            )
        }
    }

}

