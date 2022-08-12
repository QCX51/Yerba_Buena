package com.example.yerbabuena

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutView2.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutView2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checkout, container, false)
        val spinner = view.findViewById<Spinner>(R.id.Spinner_Method_Checkout)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item,
            resources.getStringArray(R.array.tipos_de_pago))

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
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

        val btnToPay = view.findViewById<Button>(R.id.btnToPay)
        btnToPay.setOnClickListener {
            val fragment = MapsFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.home_content, fragment, "MAPAS")
                .commit()
        }

        val btnBack = view.findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val fragment = PedidosFragment()
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.home_content, fragment, "PEDIDOS")
                .commit()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutView2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckoutView2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}