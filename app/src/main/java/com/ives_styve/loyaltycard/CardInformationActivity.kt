package com.ives_styve.loyaltycard

import android.content.Intent
import android.database.DataSetObserver
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CardInformationActivity : AppCompatActivity() {

    private val mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_information)
        getValues()
    }
    private fun getValues(){
        val name = findViewById<TextView>(R.id.textViewCard)
        name.text = intent.extras?.getString("name")
        val createdAt = findViewById<TextView>(R.id.lbDate)
        createdAt.text = intent.extras?.getString("date")
        val lastUse = findViewById<TextView>(R.id.lbLastUse)
        lastUse.text = intent.extras?.getString("lastuse")
        val info = findViewById<TextView>(R.id.lbInfo)
        info.text = intent.extras?.getString("data")
        generateQRCode()
    }
    private fun onDeleteSuccess(responseCode: Int){
        if(responseCode == 200){
            val intent = Intent(this,WalletActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public fun delete(view: View){
        val tokenStorage = TokenStorage(this)
        val id = intent.extras?.getInt("id")
        mainScope.launch {
            Api().delete("https://esicards.lesmoulinsdudev.com/cards/"+id.toString(),::onDeleteSuccess,tokenStorage.read())
        }

    }
    public fun cancel(view: View){
        finish()
    }

    private fun generateQRCode(){
        val info = findViewById<TextView>(R.id.lbInfo)
        val bitMatrix = MultiFormatWriter().encode(info.text.toString(), BarcodeFormat.QR_CODE,250,250)
        val bitmap = Bitmap.createBitmap(bitMatrix.width,bitMatrix.height,Bitmap.Config.RGB_565)
        val imageView = findViewById<ImageView>(R.id.imageView2)
        for (w in 0 until bitMatrix.width){
            for (h in 0 until bitMatrix.height){
                if(bitMatrix.get(w,h)){
                    bitmap.setPixel(w,h,Color.BLACK)
                }else{
                    bitmap.setPixel(w,h,Color.WHITE)
                }
            }
        }
        imageView.setImageBitmap(bitmap)
    }
}