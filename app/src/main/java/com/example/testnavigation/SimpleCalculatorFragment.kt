package com.example.testnavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.testnavigation.databinding.FragmentSimpleCalculatorBinding


class SimpleCalculatorFragment : Fragment() {
    private var number1: Double = 0.0
    private var number2: Double = 0.0
    private var result: Double = 0.0

    private var _binding: FragmentSimpleCalculatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSimpleCalculatorBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events(view)
    }

    private fun events(view: View) {
        view.findViewById<Button>(R.id.btnAdd).setOnClickListener {
            cal(view,"+")

        }

        view.findViewById<Button>(R.id.btnSub).setOnClickListener{
            cal(view,"-")
        }

        view.findViewById<Button>(R.id.btnMul).setOnClickListener{
            cal(view, "*")
        }

        view.findViewById<Button>(R.id.btnDiv).setOnClickListener{
            cal(view, "/")
        }
    }

    private fun cal( view: View, type: String) {
        val context = activity as AppCompatActivity
        val etNumber1: EditText = view.findViewById(R.id.etNumber1) as EditText
        val etNumber2: EditText = view.findViewById(R.id.etNumber2) as EditText
        val tvResult: TextView = view.findViewById(R.id.tvResult) as TextView

        tvResult.text = ""

        if (etNumber1.text.isEmpty()) {
            etNumber1.requestFocus()
            Toast.makeText(context, "Please enter the first number!", Toast.LENGTH_SHORT).show()
            tvResult.text = ""
            return
        }

        if (etNumber2.text.isEmpty()) {
            etNumber2.requestFocus()
            Toast.makeText(context, "Please enter the second number!", Toast.LENGTH_SHORT).show()
            tvResult.text = ""
            return
        }

        number1 = etNumber1.text.toString().toDouble()
        number2 = etNumber2.text.toString().toDouble()

        result = when (type) {
            "+" -> number1 + number2
            "-" -> number1 - number2
            "*" -> number1 * number2
            "/" -> number1 / number2
            else -> 0.0
        }

        if (number2 == 0.0 && type == "/") {
            tvResult.text = "Result: Infinity"
            etNumber2.requestFocus()
            Toast.makeText(context, "In division, the second number must be different from 0!", Toast.LENGTH_SHORT).show()
            return
        } else {
            tvResult.text = "Result: ${result.toString()}"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}