package com.example.fundo.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundo.MainActivity2
import com.example.fundo.R
import com.example.fundo.model.UserDetails
import com.example.fundo.viewmodels.LoginViewModel
import com.example.fundo.viewmodels.LoginViewModelFactory
import com.example.fundo.viewmodels.SharedViewModel
import com.example.fundo.viewmodels.SharedViewModelFactory

open class LoginFragment : Fragment(R.layout.loginfragment) {

    lateinit var signUpText: TextView
    lateinit var inputEmail: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var loadingIcon:ProgressBar
    lateinit var forgotPass:TextView

   lateinit var facebookButton:ImageView
  private lateinit var sharedViewModel: SharedViewModel
  private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.loginfragment, container, false)
        facebookButton = view.findViewById(R.id.facebookbtn)
        forgotPass = view.findViewById(R.id.forgotPassword)
        signUpText = view.findViewById(R.id.textViewSignUp)
        inputEmail = view.findViewById(R.id.emailID)
        password = view.findViewById(R.id.inputPassword)
        login = view.findViewById(R.id.buttonLogin)


        loadingIcon = view.findViewById(R.id.progressBar)

        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory())[SharedViewModel::class.java]
        loginViewModel = ViewModelProvider(this,LoginViewModelFactory())[LoginViewModel::class.java]
        loginObservers()

        login.setOnClickListener {

            if (Validation.checkCrendentialsForLogin(inputEmail, password)) {
                loadingIcon.visibility = View.VISIBLE
                var email = inputEmail.editableText.toString()
                var password = password.editableText.toString()
                var profileFragment = ProfileFragment()
                val bundle = Bundle()
                bundle.putString("email",email)
                profileFragment.arguments = bundle



                var status = loginViewModel.loginUser(email,password)
                Toast.makeText(context,"Status : $status",Toast.LENGTH_SHORT).show()

                if(!status){
//                    var database = Database()
//                    var fullName = database.getData()
                    //var fullName = loginViewModel.readData()

                    var newUser = UserDetails("Anusha",email,true)
                    loginViewModel.setLoginStatus(newUser)

                    Toast.makeText(context, "login success", Toast.LENGTH_SHORT).show()
                    //sharedViewModel.setGotoHomePageStatus(newUser)
                }
                else {
                    Toast.makeText(context,"login unsucess",Toast.LENGTH_SHORT).show()
                }




            }
        }


        signUpText.setOnClickListener {
            Toast.makeText(context, "sign clicked", Toast.LENGTH_SHORT).show()
            sharedViewModel.setGotoRegisterPageStatus(true)
        }


        forgotPass.setOnClickListener {

            Toast.makeText(context, "forgot password clicked", Toast.LENGTH_SHORT).show()
            sharedViewModel.setGotoForgotPageStatus(true)

        }

        facebookButton.setOnClickListener {

            loadingIcon.visibility = View.VISIBLE
            Toast.makeText(context,"facebook button clicked",Toast.LENGTH_SHORT).show()

            var intent = Intent(context,MainActivity2::class.java)
            startActivity(intent)


        }


            return view
        }

     private fun loginObservers() {
         loginViewModel.loginStatus.observe(viewLifecycleOwner){
             if(it.loginStatus) {
                 Toast.makeText(requireContext(),"User logged",Toast.LENGTH_SHORT).show()
                 sharedViewModel.setGotoHomePageStatus(it)
             }
             else {
                 Toast.makeText(requireContext(),"lggin failed",Toast.LENGTH_SHORT).show()
             }
         }
     }



}