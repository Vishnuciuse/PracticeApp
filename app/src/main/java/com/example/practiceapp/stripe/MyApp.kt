package com.example.practiceapp.stripe

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51L3eQY..." // 🔑 Your Stripe publishable key
        )
    }
}
