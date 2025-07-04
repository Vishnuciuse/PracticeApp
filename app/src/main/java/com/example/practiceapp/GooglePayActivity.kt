package com.example.practiceapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult
import org.json.JSONArray
import org.json.JSONObject

class GooglePayActivity : AppCompatActivity() {

    val paymentsClient: PaymentsClient by lazy {
        Wallet.getPaymentsClient(
            this,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // Use ENVIRONMENT_PRODUCTION in production
                .build()
        )
    }

    private val paymentDataLauncher = registerForActivityResult(GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                    // Handle the result
                }
            }
            CommonStatusCodes.CANCELED -> {}// The user canceled
                AutoResolveHelper.RESULT_ERROR -> {}// The API returned an error (it.status: Status)
            CommonStatusCodes.INTERNAL_ERROR -> {}// Handle other unexpected errors
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_google_pay)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val payButton = findViewById<Button>(R.id.googlePayButton)

        // Step 1: Initialize PayButton with options
        val options = ButtonOptions.newBuilder()
            .setButtonType(ButtonConstants.ButtonType.PAY)
            .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
            .setCornerRadius(16)
            .build()

//        payButton.initialize(options)

        // Step 2: Only show button if Google Pay is available
//        possiblyShowGooglePayButton(payButton)

        findViewById<Button>(R.id.googlePayButton).setOnClickListener {
            val request = PaymentDataRequest.fromJson(getPaymentDataRequest().toString())
            val task = paymentsClient.loadPaymentData(request)

            AutoResolveHelper.resolveTask(task, this, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        data?.let {
                            val paymentData = PaymentData.getFromIntent(it)
                            val info = paymentData?.toJson()
                            Log.d("GooglePay", "PaymentData: $info")
                            // TODO: Send payment token to your server
                        }
                    }

                    RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        Log.e("GooglePay", "Error: ${status?.statusMessage}")
                    }
                }
            }
        }
    }

    fun possiblyShowGooglePayButton(payButton: PayButton) {
        val isReadyToPayJson = JSONObject().apply {
            put("apiVersion", 2)
            put("apiVersionMinor", 0)
            put("allowedPaymentMethods", JSONArray().put(JSONObject().apply {
                put("type", "CARD")
                put("parameters", JSONObject().apply {
                    put("allowedAuthMethods", JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS"))
                    put("allowedCardNetworks", JSONArray().put("VISA").put("MASTERCARD"))
                })
            }))
        }

        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString())
        val paymentsClient = Wallet.getPaymentsClient(
            this,
            Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        )

        paymentsClient.isReadyToPay(request)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result == true) {
                    payButton.visibility = View.VISIBLE
                } else {
                    Log.d("GooglePay", "Google Pay is not available on this device.")
                }
            }
    }


    fun getPaymentDataRequest(): JSONObject {
        val paymentDataRequestJson = JSONObject().apply {
            put("apiVersion", 2)
            put("apiVersionMinor", 0)

            put("allowedPaymentMethods", JSONArray().put(
                JSONObject().apply {
                    put("type", "CARD")
                    put("parameters", JSONObject().apply {
                        put("allowedAuthMethods", JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS"))
                        put("allowedCardNetworks", JSONArray().put("MASTERCARD").put("VISA"))
                    })
                    put("tokenizationSpecification", JSONObject().apply {
                        put("type", "PAYMENT_GATEWAY")
                        put("parameters", JSONObject().apply {
                            put("gateway", "example") // Use your real gateway name
                            put("gatewayMerchantId", "exampleMerchantId")
                        })
                    })
                }
            ))

            put("transactionInfo", JSONObject().apply {
                put("totalPrice", "10.00")
                put("totalPriceStatus", "FINAL")
                put("currencyCode", "USD")
            })

            put("merchantInfo", JSONObject().apply {
                put("merchantName", "Example Merchant")
            })
        }
        return paymentDataRequestJson
    }


    companion object {
        private const val LOAD_PAYMENT_DATA_REQUEST_CODE = 991
    }
}