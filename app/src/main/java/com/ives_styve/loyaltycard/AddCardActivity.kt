package com.ives_styve.loyaltycard


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
class AddCardActivity : AppCompatActivity() {

    private lateinit var card: CardData
    private val mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        getValue()
    }

    private fun getValue(){
        val data = intent.extras?.getString("data").toString()
        val type = intent.extras?.getInt("type") ?: 0
        findViewById<TextView>(R.id.textViewData).text = data
        findViewById<TextView>(R.id.textViewType).text = type.toString()
    }

    private fun addSuccess(responseCode: Int){
        if(responseCode == 200){
            val intent = Intent(this, WalletActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public fun saveCard(view: View){
        val name = findViewById<EditText>(R.id.editTextaddName).text.toString()
        val data = intent.extras?.getString("data").toString()
        val type = intent.extras?.getInt("type") ?: 0
        card = CardData(name,data,type)
        val tokenStorage = TokenStorage(this)
        mainScope.launch {
            val token = tokenStorage.read()
            Api().post<CardData>("https://esicards.lesmoulinsdudev.com/cards",card,::addSuccess,token)
        }

    }

    public fun canceled(view: View){
        finish();
    }
}