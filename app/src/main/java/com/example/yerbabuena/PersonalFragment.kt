package com.example.yerbabuena

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_staffa_dministration.*

class PersonalFragment : AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_personal)

        val staff1 = objListCustomStaff (
            "Fernanda",
        "7721096090",
        "Tasquillo, Hidalgo, Mexico",
        R.drawable.img_fernanda_staff_background
                )
        val staff2 = objListCustomStaff (
            "Ana",
            "7721599870",
            "Ixmiquilpan, Hidalgo, Mexico",
            R.drawable.ana
        )
        val staff3 = objListCustomStaff (
            "Clara",
            "7721111090",
            "Tasquillo, Hidalgo, Mexico",
            R.drawable.alara_alvarez
        )
        val staff4 = objListCustomStaff (
            "Griselda",
            "7721111090",
            "Tasquillo, Hidalgo, Mexico",
            R.drawable.griselda
        )

         val staffList = arrayListOf(staff1,staff2,staff3,staff4)
         val adapterStaff = listCustomAdapterStaff (this,staffList)
         listview_staff.adapter = adapterStaff

    }


}