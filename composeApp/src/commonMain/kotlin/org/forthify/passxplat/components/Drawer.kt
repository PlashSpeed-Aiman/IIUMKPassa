package org.forthify.passxplat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        DrawerHeader()
        DrawerBody(navController, drawerState, scope)
    }
}

@Composable
private fun DrawerHeader() {
    Box(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(color = Color(0xFF00928F))
    )
}

@Composable
private fun DrawerBody(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val menuItems = listOf(
        MenuItem("Home", "home"),
        MenuItem("Profile", "profile"),
        MenuItem("Settings", "settings"),
        MenuItem("About", "about")
    )

    menuItems.forEach { item ->
        DrawerItem(
            text = item.title,
            onClick = {
                if (navController.currentDestination?.route != item.route) {
                    navController.navigate(item.route)
                }
                scope.launch { drawerState.close() }
            }
        )
    }
}

@Composable
private fun DrawerItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFF00928F),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            color = Color(0xFFD59F0F),
            modifier = Modifier.padding(12.dp)
        )
    }
}

private data class MenuItem(val title: String, val route: String)