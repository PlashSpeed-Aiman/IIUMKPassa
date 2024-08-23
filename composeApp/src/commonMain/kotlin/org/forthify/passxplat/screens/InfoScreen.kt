import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.BackgroundOpacity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.forthify.passxplat.logic.ImaalumService
import org.koin.java.KoinJavaComponent.getKoin

data class ImaalumInfo(
    val heading: String,
    val body: String,
    val action: suspend () -> Unit  // Changed to suspend function
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
fun InfoScreen(snackbarHostState: SnackbarHostState) {
    val imaalumService : ImaalumService = getKoin().get()
    var sessionVal : String by remember { mutableStateOf("") }
    var semesterVal : String by remember { mutableStateOf("1") }
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ){
        TextField(
            value = sessionVal,
            onValueChange = { sessionVal = it },
            label = { Text("Session e.g 2021/2022") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.5.dp),
            shape = TextFieldDefaults.TextFieldShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                placeholderColor = Color(0xFF00928F),
                focusedLabelColor = Color(0xFF00928F),
                cursorColor =  Color(0xFF00928F),
                focusedBorderColor = Color(0xFF00928F),
                backgroundColor =  MaterialTheme. colors. onSurface. copy(alpha = BackgroundOpacity)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = semesterVal,
            onValueChange = { semesterVal = it },
            label = { Text("Semester e.g 1 or 2 or 3") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.5.dp),
                    shape = TextFieldDefaults.TextFieldShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                placeholderColor = Color(0xFF00928F),
                focusedLabelColor = Color(0xFF00928F),
                cursorColor =  Color(0xFF00928F),
                focusedBorderColor = Color(0xFF00928F),
                backgroundColor =  MaterialTheme. colors. onSurface. copy(alpha = BackgroundOpacity)
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(CardData(imaalumService,sessionVal,semesterVal)) { info ->
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
fun InfoCard(
    title: String,
    body: String,
    backgroundColor: Color,
    snackbar: SnackbarHostState,
    onClick: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // Top section remains the same
            Box(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth()
                    .background(backgroundColor)
            )

            // Bottom section
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = body,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
                IconButton(
                    onClick = {
                        var job = coroutineScope.launch {
                            isLoading = true
                            try {
                                onClick()


                            } finally {
                                isLoading = false
                            }
                        }
                        job.invokeOnCompletion {
                            coroutineScope.launch {
                                var res = snackbar.showSnackbar("Download Done","VIEW")
                                when(res){
                                    SnackbarResult.ActionPerformed -> {
                                        /* Handle snackbar action performed */
                                    }
                                    SnackbarResult.Dismissed -> {
                                        /* Handle snackbar dismissed */
                                    }
                                }
                            }
                        }
                        }
                        ,
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Download")
                    }
                }
            }
        }
    }
}