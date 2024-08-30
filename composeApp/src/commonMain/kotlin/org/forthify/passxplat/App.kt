package org.forthify.passxplat

import InfoScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.forthify.passxplat.components.DrawerContent
import org.forthify.passxplat.screens.AboutScreen
import org.forthify.passxplat.screens.SettingsScreen
import org.forthify.passxplat.ui.NavShape
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.getKoin

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
val snackbarHostState = remember { SnackbarHostState() }
    // Wrap the entire content with ModalNavigationDrawer to handle the drawer state.
    ModalDrawer(
        drawerShape = NavShape(0.dp, 1f),
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState,scope)
        }
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                TopAppBar(
                    contentColor = Color.White
                    ,
                    backgroundColor = Color(0xFF00928F),
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
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                modifier = Modifier.
                padding(paddingValues),

            ) {
                composable("home") { org.forthify.passxplat.screens.HomeScreen(snackbarHostState) }
                composable("profile") { InfoScreen(getKoin().get(),snackbarHostState) }
                composable("settings") { SettingsScreen(getKoin().get())}
                composable("about"){ AboutScreen() }
            }
        }
    }
}
//@Preview
//@Composable
//fun DrawerContent(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
//    Column (
//        modifier = Modifier.background(shape = RoundedCornerShape(10.dp), color = Color.White)
//    ){
//        Box(
//            modifier = Modifier.height(100.dp).fillMaxWidth().background(
//                color = Color(0xFF00928F)
//            )
//        )
//        Box(
//            modifier = Modifier.padding(5.dp).
//            border(1.dp,Color(0xFF00928F),
//                shape = RoundedCornerShape(5.dp)
//            ).fillMaxWidth().clickable {
//                when(navController.currentDestination!!.route
//                ){
//                    "home" -> {
//                        scope.launch { drawerState.close() }
//
//                    }
//                    else->{
//                        navController.navigate("home")
//                        scope.launch { drawerState.close() }
//                    }
//                }
//
//            }
//        ) {
//            Text(
//                style = MaterialTheme.typography.subtitle1// Adjust font size as needed
//,
//                        color = Color(0xFFD59F0F),
//
//                text = "Home",
//                modifier = Modifier
//                    .padding(12.dp),
//
//            )
//        }
//        Box(
//            modifier = Modifier.padding(5.dp).
//            border(1.dp,Color(0xFF00928F),
//                shape = RoundedCornerShape(5.dp)
//            )
//                .fillMaxWidth().clickable {
//                    when(navController.currentDestination!!.route){
//                        "home" -> {
//                            scope.launch { drawerState.close() }
//
//                        }
//                        else ->{
//                            navController.navigate("home")
//                            scope.launch { drawerState.close() }
//
//                        }
//                    }
//            }
//        ) {
//            Text(
//                style = MaterialTheme.typography.subtitle1// Adjust font size as needed
//,
//                        color = Color(0xFFD59F0F),
//                text = "Profile",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                        navController.navigate("profile")
//                        scope.launch { drawerState.close() }
//
//                    }
//                    .padding(12.dp)
//            )
//        }
//        Box(
//            modifier = Modifier.padding(5.dp). border(1.dp,Color(0xFF00928F),
//                shape = RoundedCornerShape(5.dp)
//            ).fillMaxWidth().clickable {
//                navController.navigate("home")
//                scope.launch { drawerState.close() }
//            }
//        ) {
//            Text(
//                style = MaterialTheme.typography.subtitle1// Adjust font size as needed
//                ,
//                color = Color(0xFFD59F0F),
//                text = "Settings",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                        navController.navigate("settings")
//                        scope.launch { drawerState.close() }
//                    }
//                    .padding(12.dp)
//            )
//        }
//    }
//}

//@Composable
//fun SettingsScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text("Work in Progress")
//    }
//}
//
