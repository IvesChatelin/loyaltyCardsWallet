package com.ives_styve.loyaltycard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
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
    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_ALL_FORMATS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        adapter = CardAdapter(this,listOfCard)
        getCards()
        initListCardView()

        val listCard = findViewById<ListView>(R.id.listcard)

        listCard.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getInfo(position)
            }
        }

        search()
    }

    private fun initListCardView(){
        val listCard = findViewById<ListView>(R.id.listcard)
        listCard.adapter = adapter
    }

    private fun search(){
        val tmpListOfCard = ArrayList<ResponseCardData>()
        val textFieldSearch = findViewById<EditText>(R.id.editTextSearch)
        textFieldSearch.addTextChangedListener ( object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                tmpListOfCard.addAll(listOfCard)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == ""){
                    getCards()
                }
                val searchList = tmpListOfCard.filter { it.name == s.toString() } as ArrayList<ResponseCardData>
                listOfCard.clear()
                listOfCard.addAll(searchList)
                updateListOfCard()
            }

        } )
    }

    private fun getSuccess(responseCode: Int, responseCardData: List<ResponseCardData>?){
        if(responseCode == 200 && !responseCardData.isNullOrEmpty()){
            listOfCard.addAll(responseCardData)
            updateListOfCard()
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
        finish();
    }

    private fun getInfoSuccess(responseCode: Int, responseInfoCard: ResponseInfoCard?){
        if(responseCode == 200){
            val intent = Intent(this, CardInformationActivity::class.java)
            intent.putExtra("name", responseInfoCard?.name)
            intent.putExtra("date", responseInfoCard?.createdAt)
            intent.putExtra("lastuse", responseInfoCard?.lastUsedAt)
            intent.putExtra("data", responseInfoCard?.data)
            intent.putExtra("id",responseInfoCard?.id)
            startActivity(intent)
        }
    }

    private fun getInfo(position: Int){
        val tokenStorage = TokenStorage(this)
        mainScope.launch {
            Api().get<ResponseInfoCard>("https://esicards.lesmoulinsdudev.com/cards/"+adapter.getItemId(position),::getInfoSuccess,tokenStorage.read())
        }
    }

    public fun addCard(view: View) {
        val scanner = GmsBarcodeScanning.getClient(this, options)
        // on téléchage le module neccessaire pour le lecteur de code
        val moduleInstallRequest = ModuleInstallRequest.newBuilder().addApi(scanner).build()
        val moduleInstallClient = ModuleInstall.getClient(this)
            .installModules(moduleInstallRequest)
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
            .addOnFailureListener{e ->
                Log.d("FailureModuleInstallScanner", e.toString())
            }

    }
}