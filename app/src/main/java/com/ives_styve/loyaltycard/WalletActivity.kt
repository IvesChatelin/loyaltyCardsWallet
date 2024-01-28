package com.ives_styve.loyaltycard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class WalletActivity : AppCompatActivity() {
    private var listOfCard = ArrayList<String>()
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
}