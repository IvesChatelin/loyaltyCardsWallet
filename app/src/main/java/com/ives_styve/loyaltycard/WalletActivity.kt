package com.ives_styve.loyaltycard

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ListView
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WalletActivity : AppCompatActivity() {
    private var listOfCard = ArrayList<String>()
    private val mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        listOfCard.add("toto")
        initListCardView()
    }

    private fun initListCardView(){
        val listCard = findViewById<ListView>(R.id.listcard)
        listCard.adapter = CardAdapter(this,listOfCard)
    }

    public fun logout(view: View){
        val tokenStore = TokenStore(this)
        mainScope.launch {
            tokenStore.write(null,0)
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    public fun addCard(view: View){
        Log.d("tag", "rawValue.toString()")
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()
        val scanner = GmsBarcodeScanning.getClient(this, options)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue: String? = barcode.rawValue
                Log.d("tag", rawValue.toString())
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
            }
    }
}