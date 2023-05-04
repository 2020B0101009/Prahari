package com.smart.hbalert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.smart.hbalert.databinding.ActivityChangePasswordBinding


class ChangePassword : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private var visible=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.submit.setOnClickListener{
            val pass= binding.password.text.toString()
            val passCon=binding.confirmPassword.text.toString()
            changePassword(pass,passCon)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }


        binding.passImg.setOnClickListener{
            visible = if(visible==0){
                binding.password.transformationMethod= HideReturnsTransformationMethod.getInstance()
                binding.passImg.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
                1
            } else{
                binding.password.transformationMethod= PasswordTransformationMethod.getInstance()
                binding.passImg.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                0
            }
        }
    }


    private fun changePassword(pass:String, conPass:String){
        if (pass.length>5){
            if (conPass.length>5 && pass==conPass){

                //Change the Password in Database
                val user = FirebaseAuth.getInstance().currentUser

                user?.updatePassword(pass)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Password updated successfully
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        // An error occurred
                        Toast.makeText(this,"Error Occurred",Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                binding.warning.text=getString(R.string.password_not_match)
            }
        }
        else{
            binding.warning.text=getString(R.string.short_password)
        }
    }
}