package com.example.practiceapp.stripe

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(application: Application) : AndroidViewModel(application) {

    // Replace with your backend API to get clientSecret
    private suspend fun fetchPaymentIntentClientSecret(): String {
        // Example only: Normally you'd call your backend to create a PaymentIntent
        val dummyClientSecret = "pi_1234567890_secret_abcdefg" // replace with actual from backend
        return dummyClientSecret
    }

    fun confirmPayment(paymentMethodParams: PaymentMethodCreateParams, stripe: Stripe,activity: ComponentActivity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val clientSecret = fetchPaymentIntentClientSecret()
                val confirmParams =
                    ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                        paymentMethodParams,
                        clientSecret
                    )

                launch(Dispatchers.Main) {
                    stripe.confirmPayment(activity, confirmParams)
                }
            } catch (e: Exception) {
                Log.e("StripePayment", "Payment error: ${e.localizedMessage}")
            }
        }
    }
}
