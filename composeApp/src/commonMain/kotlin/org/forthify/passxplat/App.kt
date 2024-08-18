package org.forthify.passxplat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.forthify.passxplat.screens.InfoScreen
import org.forthify.passxplat.ui.NavShape
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MainScreen()
}
@Composable
@Preview
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Wrap the entire content with ModalNavigationDrawer to handle the drawer state.
    ModalDrawer(
        drawerShape = NavShape(0.dp, 1f),
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState,scope)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(70.dp),
                    elevation = 0.dp,
                    title = { Text("IIUMKPassa") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home") { org.forthify.passxplat.screens.HomeScreen() }
                composable("profile") { InfoScreen() }
                composable("settings") { SettingsScreen() }
            }
        }
    }
}
@Preview
@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    Column (modifier = Modifier.padding(5.dp)) {
        Box(
            modifier = Modifier.padding(5.dp).background(
                color = Color(0xFFBB86FC),
                shape = RoundedCornerShape(5.dp)
            ).fillMaxWidth().clickable {
                navController.navigate("home")
                scope.launch { drawerState.close() }
            }
        ) {
            Text(
                fontSize = 18.sp,
                color = Color.White,

                text = "Home",
                modifier = Modifier
                    .padding(12.dp),

            )
        }
        Box(
            modifier = Modifier.padding(5.dp).background(
                color = Color(0xFFBB86FC),
                shape = RoundedCornerShape(5.dp)
            ).fillMaxWidth().clickable {
                navController.navigate("home")
                scope.launch { drawerState.close() }
            }
        ) {
            Text(
                fontSize = 18.sp,
                color = Color.White,
                text = "Profile",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("profile")
                        scope.launch { drawerState.close() }

                    }
                    .padding(12.dp)
            )
        }
        Box(
            modifier = Modifier.padding(5.dp).background(
                color = Color(0xFFBB86FC),
                shape = RoundedCornerShape(5.dp)
            ).fillMaxWidth().clickable {
                navController.navigate("home")
                scope.launch { drawerState.close() }
            }
        ) {
            Text(
                fontSize = 18.sp,
                color = Color.White,
                text = "Settings",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("settings")
                        scope.launch { drawerState.close() }
                    }
                    .padding(12.dp)
            )
        }
    }
}
@Preview
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Home Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Profile Screen")
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Settings Screen")
    }
}