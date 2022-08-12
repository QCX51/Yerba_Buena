package com.example.yerbabuena
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AlphaAdapters (var context:Context , var arrayList: ArrayList<AlphaChar>):
    RecyclerView.Adapter<AlphaAdapters.ItemHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
      val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.card_products, parent , false)
    return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        var alphaChar:AlphaChar = arrayList.get(position)
        holder.icons.setImageResource(alphaChar.iconsChar!!)
        holder.alphas.text= alphaChar.alphaChar
        holder.delete.setImageResource(alphaChar.iconsDelete!!)

        holder.alphas.setOnClickListener{
            Toast.makeText(context, alphaChar.alphaChar,Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var icons:ImageView = itemView.findViewById<ImageView>(R.id.icons_image)
        var alphas:TextView = itemView.findViewById<TextView>(R.id.icons_price)
        var delete: ImageView = itemView.findViewById<ImageView>(R.id.delete_product)
    }
}