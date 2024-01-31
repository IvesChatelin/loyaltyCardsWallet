package com.ives_styve.loyaltycard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
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
}