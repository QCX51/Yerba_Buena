package com.example.yerbabuena

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
class CheckoutView2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checkout, container, false)
        val spinner = view.findViewById<Spinner>(R.id.Spinner_Method_Checkout)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, resources.getStringArray(R.array.tipos_de_pago))

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView3)
                when (adapter.getItem(position))
                {
                    "Efectivo" -> {
                        navController.navigate(R.id.cash_Payment_Fragment)
                    }
                    "Transferencia" -> {
                        navController.navigate(R.id.card_Payment_Fragment)
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Nothing selected
            }
        }
        return view
    }
}