package org.forthify.passxplat.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoginForm()
    }
}

@Composable
fun LoginForm() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoginVisible by remember { mutableStateOf(false) } // State for collapsing
    var isClicked by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Box (modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Clickable "Login" text to show/hide the login form
            Text(
                text = "Setup Credentials",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable {
                        isLoginVisible = !isLoginVisible
                        isClicked = !isClicked
                    }
                    .background(
                        color = if (isClicked) Color.LightGray else Color.Transparent, // Change background color when clicked
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Gray, // Border color
                        shape = MaterialTheme.shapes.medium // Same shape for border
                    )
                    .padding(16.dp) // Larger padding for clickable area
            )


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
                            onClick = { /* Handle login */ },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(15.dp),
                            shape = AbsoluteCutCornerShape(10.dp)
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(15.dp),
                shape = AbsoluteCutCornerShape(bottomRight = 10.dp),// Add padding to give the button some space around the text
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White // Text color
                )
            ) {
                Text(
                    text = "Click To Login To Wifi",
                    textAlign = TextAlign.Center, // Center align text
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold // Make text bold
                    ) // Adjust font size as needed
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

