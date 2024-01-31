package com.ives_styve.loyaltycard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //pour masquer la bar d'action sur l'écran de démarrage
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val tokenStore = TokenStore(this)
            mainScope.launch {
                var token: String? = null
                var expiration: Long? = 0
                token = tokenStore.read()
                expiration = tokenStore.readExpiration()
                if(token.toString() != "null" && Date().time <= expiration.toLong()){
                    //Log.d("TAG", token.toString())
                    //Log.d("TAG", "date : "+ expiration.toLong())
                    //Log.d("TAG", "date : "+ Date().time)
                    val intent = Intent(this@MainActivity, WalletActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        },3000)
    }
}