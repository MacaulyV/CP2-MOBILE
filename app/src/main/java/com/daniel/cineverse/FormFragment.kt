package com.daniel.cineverse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class FormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)

        val btnSubmit: Button = view.findViewById(R.id.btnSubmit)
        val nameInput: EditText = view.findViewById(R.id.editTextName)
        val emailInput: EditText = view.findViewById(R.id.editTextEmail)

        btnSubmit.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                Toast.makeText(requireContext(), "Nome: $name, Email: $email", Toast.LENGTH_SHORT).show()

                sendDataToDisplayFragment(name, email)
            } else {

                Toast.makeText(requireContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendDataToDisplayFragment(name: String, email: String) {

        val bundle = Bundle().apply {
            putString("name", name)
            putString("email", email)
        }

        val displayFragment = DisplayFragment().apply {
            arguments = bundle
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, displayFragment)
            .addToBackStack(null)
            .commit()
    }
}
