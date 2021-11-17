package com.example.fundo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.R
import com.example.fundo.viewmodels.*

class ForgotPasswordFragment : Fragment() {

    lateinit var inputEmail: EditText
    lateinit var resetButton: Button
    lateinit var progress: ProgressBar
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var forgotpasswordViewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.forgotpasswordfragment, container, false)
        inputEmail = view.findViewById(R.id.inputEmailId)
        resetButton = view.findViewById(R.id.resetBtn)
        progress = view.findViewById(R.id.resetProgress)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        forgotpasswordViewModel = ViewModelProvider(this,ForgotPasswordViewModelFactory())[ForgotPasswordViewModel::class.java]

        forgotPasswordObservers()

        resetButton.setOnClickListener {
            if (Validation.validateEmail(inputEmail)) {
                progress.visibility = View.VISIBLE
                var emailId = inputEmail.editableText.toString()
                var result = forgotpasswordViewModel.forgotPassword(emailId)
                if(!result) {
                    forgotpasswordViewModel.setResetPasswordStatus(true)
                    Toast.makeText(context,"check your mail for reset password",Toast.LENGTH_SHORT).show()
                    progress.visibility = View.GONE
                }

            }
        }
        return view
    }




    private fun forgotPasswordObservers() {
        forgotpasswordViewModel.resetPasswordStatus.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "mail sent", Toast.LENGTH_SHORT).show()
                //sharedViewModel.setGotoHomePageStatus(it)
            } else {
                Toast.makeText(requireContext(), "mail not sent", Toast.LENGTH_SHORT).show()
            }
        }
    }
}