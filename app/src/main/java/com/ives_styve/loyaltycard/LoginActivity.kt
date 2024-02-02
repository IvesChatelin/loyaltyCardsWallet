package com.ives_styve.loyaltycard

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Date

class LoginActivity : AppCompatActivity() {

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    public fun goToRegister(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun goToWallet(token: String?){
        val tokenStorage = TokenStorage(this)
        mainScope.launch {
            //le token expire après 24h soit 86400 seconde
            tokenStorage.write(token, Date().time + 86400)
        }
        val intent = Intent(this, WalletActivity::class.java)
        intent.putExtra("token",token)
        startActivity(intent)
        finish()
    }

    private fun openDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Erreur de connexion")
            .setMessage("l'email ou le mot de passe ne correspond pas")

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun loginSuccess(responseCode: Int, token: String?){
        if(responseCode == 200){
            goToWallet(token)
        }else{
            openDialog()
        }
    }

    public fun login(view: View){
        val mail = findViewById<EditText>(R.id.editTextTextEmaillogin).text.toString()
        val  password = findViewById<EditText>(R.id.editTextTextPasswordlogin).text.toString()
        val data = LoginData(mail,password)
        Api().post<LoginData, String>("https://esicards.lesmoulinsdudev.com/auth",data,::loginSuccess)
    }

}