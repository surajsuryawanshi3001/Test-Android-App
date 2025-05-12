package com.example.testapplication
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.testapplication.ui.theme.TestApplicationTheme

import android.widget.Toast
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import org.json.JSONObject
import com.example.testapplication.ui.theme.model.GeneratedString
import com.example.testapplication.ui.theme.viewmodel.RandomStringViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                TestApplicationTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        RandomStringApp()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Something went wrong: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomStringApp(viewModel: RandomStringViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current
    var lengthInput by remember { mutableStateOf("") }
    val strings by viewModel.generatedStrings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var generatedString by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Random String Generator Application ",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Blue,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )


        TextField(
            value = lengthInput,
            onValueChange = { lengthInput = it },
            label = { Text("Enter String Length", color = Color.Black) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                disabledIndicatorColor = Color.LightGray,
                errorIndicatorColor = Color.Red
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val length = lengthInput.toIntOrNull()
                if (length != null && length > 0) {
                    viewModel.generateString(length)
                    lengthInput = ""
                } else {
                    Toast.makeText(context, "Please Enter a Valid Number", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 3.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Generate String", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        /*  Clear All button click */
        if (strings.isNotEmpty()) {
            Button(
                onClick = { viewModel.clearStrings() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Clear All", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
            )
        }
        GeneratedStringsList(strings, onDelete = { index -> viewModel.deleteString(index) })
    }
}

@Composable
fun GeneratedStringsList(strings: List<GeneratedString>, onDelete: (Int) -> Unit) {
    LazyColumn {
        itemsIndexed(strings) { index, string ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Generated String: ${string.value}", color = Color.Black)  // Text color black
                        Text(text = "Length: ${string.length}", color = Color.Black)  // Text color black
                        Text(text = "Created: ${string.created}", color = Color.Black)  // Text color black
                    }
                    IconButton(
                        onClick = { onDelete(index) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Blue
                        )
                    }
                }
                Divider(color = Color.Black, thickness = 1.dp)
            }
        }
    }
}

fun generateRandomString(length: Int): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    return (1..length)
        .map { characters.random() }
        .joinToString("")
}

fun getCurrentTime(): String {
    val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(Date())
    return currentDate
}

