package com.ives_styve.loyaltycard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class WalletActivity : AppCompatActivity() {

    private var listOfCard = ArrayList<ResponseCardData>()
    private val mainScope = MainScope()
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        adapter = CardAdapter(this,listOfCard)
        getCards()
        initListCardView()
    }

    private fun initListCardView(){
        val listCard = findViewById<ListView>(R.id.listcard)
        listCard.adapter = adapter
    }

    public fun goToInfoCard(view: View){
       val intent = Intent(this, CardInformationActivity::class.java)
        startActivity(intent)
    }

    private fun getSuccess(responseCode: Int, responseCardData: List<ResponseCardData>?){
        if(responseCode == 200 && !responseCardData.isNullOrEmpty()){
            listOfCard.addAll(responseCardData)
            updateListOfCard()
            Log.d("TAG", responseCardData.toString())
        }
    }

    private fun updateListOfCard(){
        runOnUiThread(){
            adapter.notifyDataSetChanged()
        }
    }

    private fun getCards(){
        val tokenStorage = TokenStorage(this)
        mainScope.launch {
            val token = tokenStorage.read()
            Api().get<List<ResponseCardData>>("https://esicards.lesmoulinsdudev.com/cards",::getSuccess,token)
        }

    }

    public fun logout(view: View){
        val tokenStorage = TokenStorage(this)
        mainScope.launch {
            tokenStorage.write(null,0)
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    public fun addCard(view: View) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS)
            .build()
        val scanner = GmsBarcodeScanning.getClient(this, options)
        // on demande à l'appareil d'installer le service scanner
        val moduleInstallRequest = ModuleInstallRequest.newBuilder().addApi(scanner).build()
        val moduleInstallClient = ModuleInstall.getClient(this)
        moduleInstallClient.installModules(moduleInstallRequest)
            .addOnSuccessListener {
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        val rawValue: String? = barcode.rawValue
                        val valueType = barcode.valueType
                        val intent = Intent(this,AddCardActivity::class.java)
                        intent.putExtra("data", rawValue)
                        intent.putExtra("type", valueType)
                        startActivity(intent)
                    }
                    .addOnCanceledListener {
                        Log.d("CanceledScanner", "CANCELED")
                    }
                    .addOnFailureListener { e ->
                        Log.d("FailureScanner", e.toString())
                    }
            }
            .addOnFailureListener { e ->
                Log.d("FailureModuleInstallScanner", e.toString())
            }

    }
}