package com.ives_styve.loyaltycard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    public fun goToLogin(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerSuccess(responseCode: Int){
        if (responseCode == 200){
            finish()
        }
    }

    public fun register(view: View){
        val name = findViewById<EditText>(R.id.editTextName).text.toString()
        val email = findViewById<EditText>(R.id.editTextEmailRegister).text.toString()
        val password = findViewById<EditText>(R.id.editTextPasswordRegister).text.toString()
        val data = RegisterData(name,email,password)
        Api().post<RegisterData>("https://esicards.lesmoulinsdudev.com/register",data,::registerSuccess)
    }
}