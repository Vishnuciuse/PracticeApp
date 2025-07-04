package com.example.practiceapp.aa

import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.practiceapp.R
import com.example.practiceapp.aa.ui.theme.PracticeAppTheme
import com.example.practiceapp.domain.UserViewModel

class TestActivity : ComponentActivity() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PracticeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: UserViewModel = UserViewModel()) {
    val name1 = remember { mutableStateOf("JOHN deo") }
    val users by viewModel.userState.collectAsState()
    val counter by viewModel.counter.collectAsState()
    Column (modifier = modifier
        .padding(16.dp)
        .fillMaxSize()){
        Text(
            text = "Hello ${name1.value}!",
            modifier = modifier
        )

        Text(
            text = "Change User Name",
            modifier = modifier
                .padding(8.dp)
                .background(color = androidx.compose.ui.graphics.Color.Blue)
                .clickable {
                    name1.value = "Alex"
                },

        )

        users.forEach {
            Text(
                text = "Users: ${it.first_name}",
            )
        }

        Row {
            Text(text = counter.toString(), modifier.testTag(stringResource(R.string.countertext)))
            Button(modifier = Modifier.padding(8.dp), onClick = {viewModel.incrementCounter()}) {
                Text(text = "increment")
            }

        }

    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeAppTheme {
        Greeting("Android",)
    }
}