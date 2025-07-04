package com.example.practiceapp.stripe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import com.example.practiceapp.VideoPlayerApp
import com.example.practiceapp.ui.theme.PracticeAppTheme
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.view.CardInputWidget

class StripeActivity : ComponentActivity() {

    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PaymentViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PracticeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StripePaymentScreen(viewModel, modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }

    @Composable
    fun StripePaymentScreen(
        paymentViewModel: PaymentViewModel,
        modifier: Modifier
    ) {
        val context = LocalContext.current
        val cardInputWidget = remember { CardInputWidget(context) }

        val stripe = remember {
            Stripe(context, PaymentConfiguration.getInstance(context).publishableKey)
        }

        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AndroidView(
                factory = { cardInputWidget },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(onClick = {
                val params = cardInputWidget.paymentMethodCreateParams
                if (params != null) {
                    paymentViewModel.confirmPayment(params, stripe, context as ComponentActivity)
                } else {
                    Toast.makeText(context, "Invalid card input", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Pay")
            }
        }
    }


}




@Preview(showBackground = true,)
@Composable
fun GreetingPreview() {
    PracticeAppTheme {
        VideoPlayerApp()
    }
}
