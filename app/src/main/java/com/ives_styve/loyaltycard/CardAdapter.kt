package com.ives_styve.loyaltycard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class CardAdapter(
    private val context: Context,
    private val dataSource: ArrayList<ResponseCardData>,
):BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): ResponseCardData {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return dataSource[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.item,parent,false)
        val btndelete = rowView.findViewById<Button>(R.id.btnDelete)
        btndelete.setOnClickListener{
            (context as WalletActivity).deleteCard(position)
        }
        val name = rowView.findViewById<TextView>(R.id.itemName)
        name.text = getItem(position).name
        name.setOnClickListener{
            (context as WalletActivity).getInfo(position)
        }
        return rowView
    }
}