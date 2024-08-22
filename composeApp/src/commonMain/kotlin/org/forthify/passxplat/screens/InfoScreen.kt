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
import androidx.compose.material.Text
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ImaalumInfo(
    val heading: String,
    val body: String,
    val action: suspend () -> Unit  // Changed to suspend function
)

fun CardData(): List<ImaalumInfo> {
    val data = listOf(
        ImaalumInfo("Course Confirmation Slip", "Get your timetable") {
            // Implement your suspend function here
        },
        ImaalumInfo("Financial Statements", "View your financial details") {
            // Implement your suspend function here
        },
        ImaalumInfo("Exam Slip", "Download your exam slip") {
            println("Async operation started")
            delay(2000)  // Simulating some work
            println("Async operation completed")
        },
        ImaalumInfo("Results", "Check your results") {
            println("Async operation started")
            delay(20000)  // Simulating longer operation
            println("Async operation completed")
        }
    )
    return data
}

@Composable
fun InfoScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(CardData()) { info ->
            InfoCard(
                title = info.heading,
                body = info.body,
                backgroundColor = Color(0xFFBB86FC),
                onClick = info.action  // Passing the suspend function
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    body: String,
    backgroundColor: Color,
    onClick: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
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
                    .weight(1f)
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
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                onClick()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
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