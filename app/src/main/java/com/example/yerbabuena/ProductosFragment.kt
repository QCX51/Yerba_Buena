package com.example.yerbabuena

import android.app.Application
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductosFragment : Fragment() {

    private var recyclerView:RecyclerView? = null
    private var gridLayoutManager:GridLayoutManager? = null
    private var arrayList:ArrayList<AlphaChar>? = null
    private var alphaAdapters:AlphaAdapters? = null


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {

       recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        arrayList = ArrayList()
        arrayList = setDataInList()
        recyclerView?.adapter = alphaAdapters
        val view: View = inflater.inflate(R.layout.fragment_productos, container, false)

        return view
    }

    private fun setDataInList() :ArrayList<AlphaChar>{
        var items :ArrayList<AlphaChar> =ArrayList()

        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))
        items.add(AlphaChar(R.drawable.itemensalda , "ensalada" , R.drawable.delete_icon))

return items
    }

}