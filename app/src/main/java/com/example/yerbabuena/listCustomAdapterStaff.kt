package com.example.yerbabuena

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_personal.view.*
import android.content.Context


class listCustomAdapterStaff (private val StaffContext: Context, private val listCustom: ArrayList<objListCustomStaff>): ArrayAdapter<objListCustomStaff>(StaffContext,0, listCustom){
    override fun getView(position: Int,convertView: View?, parent: ViewGroup): View{
        val layout = LayoutInflater.from(StaffContext).inflate(R.layout.fragment_personal, parent, false)

        val staffs = listCustom[position]

        layout.StaffName.text = staffs.StaffName
        layout.StaffPhone.text = staffs.StaffPhone
        layout.StaffAddress.text = staffs.StaffAddress
        layout.profile_staff_pic.setImageResource(staffs.profile_staff_pic)

        return layout
    }
}