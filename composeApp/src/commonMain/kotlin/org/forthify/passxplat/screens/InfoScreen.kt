import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.BackgroundOpacity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import arrow.core.Either
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.forthify.passxplat.logic.ImaalumService
import org.koin.java.KoinJavaComponent.getKoin

data class ImaalumInfo(
    val heading: String,
    val body: String,
    val action: suspend () -> Either<Boolean,Error>  // Changed to suspend function
)

fun CardData(imaalumService: ImaalumService, sessionVal: String, semesterVal: String): List<ImaalumInfo> {
    val data = listOf(
        ImaalumInfo("Course Confirmation Slip", "Get your timetable") {
            imaalumService.downloadCourseConfirmationSlip(sessionVal,semesterVal)
        },
        ImaalumInfo("Financial Statements", "View your financial details") {
            imaalumService.downloadFinancialStatement()
        },
        ImaalumInfo("Exam Slip", "Download your exam slip") {
            imaalumService.downloadExamSlip()

        },
        ImaalumInfo("Results", "Check your results") {
            imaalumService.downloadResult(sessionVal,semesterVal)

        }
    )
    return data
}

@Composable
fun InfoScreen(imaalumService: ImaalumService,snackbarHostState: SnackbarHostState) {

    var sessionVal : String by remember { mutableStateOf("2023/2024") }
    var semesterVal : String by remember { mutableStateOf("1") }
    val primaryColor = Color(0xFF00928F)
    val backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.1f)
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ){
        InfoScreenInfo()
        Spacer(
            modifier = Modifier.height(15.dp)
        )

        OutlinedTextField(
            value = sessionVal,
            onValueChange = { sessionVal = it },
            label = { Text("Session") },
            placeholder = { Text("e.g., 2021/2022") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = primaryColor.copy(alpha = 0.5f),
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor,
                backgroundColor = backgroundColor
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Calendar",
                    tint = primaryColor
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )
                Spacer(modifier = Modifier.height(15.dp))


        OutlinedTextField(
            value = semesterVal,
            onValueChange = { semesterVal = it },
            label = { Text("Semester") },
            placeholder = { Text("e.g., 1 or 2 or 3") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = primaryColor.copy(alpha = 0.5f),
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor,
                backgroundColor = backgroundColor
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "School",
                    tint = primaryColor
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(CardData(imaalumService,sessionVal, semesterVal)) { info ->
                InfoCard(
                    title = info.heading,
                    body = info.body,
                    backgroundColor =  Color(0xFFD59F0F),
                    onClick = info.action,
                    snackbar = snackbarHostState
                      // Passing the suspend function
                )
            }
        }
    }
}
@Composable
fun InfoScreenInfo(){
    Column (

    ){
        Text(
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            text = "Profile",
        )
    }
}
@Composable
fun InfoCard(
    title: String,
    body: String,
    backgroundColor: Color,
    snackbar: SnackbarHostState,
    onClick: suspend () -> Either<Boolean, Error>
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top section
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth()
                    .background(backgroundColor)
            )

            // Bottom section
            Row(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = body,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                val result = onClick()
                                isLoading = false
                                try {
                                    when (result) {
                                        is Either.Left -> handleSuccess(snackbar)
                                        is Either.Right -> handleError(snackbar, result.value)
                                    }
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        enabled = !isLoading
                    ) {
                        this@Row.AnimatedVisibility(
                            visible = !isLoading,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Icon(
                                Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Download",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        this@Row.AnimatedVisibility(
                            visible = isLoading,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

private suspend fun handleSuccess(snackbar: SnackbarHostState) {
    val snackbarResult = snackbar.showSnackbar(
        message = "Files have been downloaded to the Downloads folder",
        actionLabel = "DISMISS"
    )
    if (snackbarResult == SnackbarResult.ActionPerformed) {
        snackbar.currentSnackbarData?.dismiss()
    }
}

private suspend fun handleError(snackbar: SnackbarHostState, error: Error) {
    snackbar.showSnackbar("Error: ${error.message}")
}