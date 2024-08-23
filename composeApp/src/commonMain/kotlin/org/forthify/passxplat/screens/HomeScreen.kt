package org.forthify.passxplat.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.forthify.passxplat.logic.CredentialStorage
import org.forthify.passxplat.logic.LoginService
import org.forthify.passxplat.model.StudentCredentials
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun HomeScreen() {
    val loginService : LoginService = getKoin().get()
    val credStore : CredentialStorage = getKoin().get()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoginForm(loginService,credStore)
    }
}

@Composable
fun LoginForm(loginService: LoginService, credStore: CredentialStorage) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoginVisible by remember { mutableStateOf(false) } // State for collapsing
    var isClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box (modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Clickable "Login" text to show/hide the login form
            Button(
                onClick = {
                    isLoginVisible = !isLoginVisible
                    isClicked = !isClicked
                },
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 1.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFFD59F0F)
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Setup Credentials",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }


            // Collapsible login form
            AnimatedVisibility(visible = isLoginVisible) {
                Card (
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    elevation = 2.dp,
                ) {
                    Column(
                        modifier = Modifier.background(Color(0xECF0F1)).padding(12.dp),

                    ) {
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Matric Number") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(2.dp))

                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(2.dp)),
                            singleLine = true,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                isLoading = true
                                CoroutineScope(Dispatchers.IO).launch {
                                    delay(500)
                                    credStore.save(StudentCredentials(username, password))
                                    isLoading = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF00928F),
                                contentColor = Color(0xFFFFFFFF)
                            ),
                            modifier = Modifier.fillMaxWidth().background(
                                color = Color(0xFF00928F)
                            ),
                            contentPadding = PaddingValues(15.dp),
                            shape = MaterialTheme.shapes.medium,
                            enabled = !isLoading // Disable button while loading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White, // Or any color that contrasts with your button
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(text = "Save")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {CoroutineScope(Dispatchers.IO).launch {
                    delay(500)
                    loginService.LoginToWifi()
                    isLoading = false
                } },
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(15.dp),
                shape = MaterialTheme.shapes.medium ,// Add padding to give the button some space around the text
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF00928F),
                    contentColor = Color.White // Text color
                )
            ) {
                Text(
                    text = "Click To Login To Wifi",
                    textAlign = TextAlign.Center, // Center align text
                    style = MaterialTheme.typography.h6// Adjust font size as needed
                )
            }


        }
    }
}

@Composable
fun SnackbarDemo() {
    val (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }

    Column {
        Button(onClick = {
            setSnackBarState(!snackbarVisibleState)
        }) {
            Text(
                text = if (snackbarVisibleState) "Hide Snackbar" else "Show Snackbar"
            )
        }

        // Display Snackbar only when snackbarVisibleState is true
        AnimatedVisibility(visible = snackbarVisibleState) {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                action = {
                    Button(onClick = {
                        // Handle action click
                        setSnackBarState(false) // Optionally hide Snackbar on action
                    }) {
                        Text("MyAction")
                    }
                }
            ) {
                Text(text = "This is a snackbar!")
            }
        }
    }


}
