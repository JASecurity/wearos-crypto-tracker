package com.example.quantpricetracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QntPriceApp()
        }
    }
}

@Composable
fun QntPriceApp(viewModel: QntViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "QNT",
                    fontSize = 16.sp,
                    color = Color(0xFF00D9FF),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                when {
                    uiState.isLoading && uiState.price == null -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Loading...",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    uiState.error != null -> {
                        Text(
                            text = "Error",
                            fontSize = 14.sp,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(onClick = { viewModel.fetchPrice() }) {
                            Text("Retry", fontSize = 12.sp)
                        }
                    }
                    uiState.price != null -> {
                        Text(
                            text = "$${String.format("%.2f", uiState.price)}",
                            fontSize = 28.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "CAD",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        if (uiState.lastUpdated.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Updated: ${uiState.lastUpdated}",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!uiState.isLoading) {
                    Button(
                        onClick = { viewModel.fetchPrice() },
                        modifier = Modifier.size(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Text("↻", fontSize = 20.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
