package com.kiran.mydating.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kiran.mydating.MainActivity
import com.kiran.mydating.R
import com.kiran.mydating.databinding.ActivitySignInBinding
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    val auth = FirebaseAuth.getInstance()
    private var verificationId: String? = null
    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()


        binding.otpBtn.setOnClickListener {
            if (binding.phoneNumber.text!!.isEmpty()) {
                binding.phoneNumber.error = "Enter your number"
            } else {
                sendOtp(binding.phoneNumber.text.toString())
            }
        }

        binding.submitBtn.setOnClickListener {
            if (binding.otpNumber.text!!.isEmpty()) {
                binding.otpNumber.error = "Enter OTP"
            } else {
                submitOtp(binding.otpNumber.text.toString())
            }
        }


    }

    private fun submitOtp(Otp: String) {
        //binding.otpBtn.showLoadingButton()
        dialog.show()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, Otp)
        signInWithPhoneAuthCredential(credential)

    }

    private fun sendOtp(number: String) {
        dialog.show()

       // binding.otpBtn.showLoadingButton()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

               // binding.otpBtn.showNormalButton()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@SignInActivity.verificationId = verificationId

                dialog.dismiss()
                //binding.otpBtn.showNormalButton()

                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                //binding.otpBtn.showNormalButton()
                if (task.isSuccessful) {


                    checkUserExist(binding.phoneNumber.text.toString())

//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
                } else {
                    dialog.dismiss()
                   Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child("+91$number")
            .addValueEventListener(object :ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@SignInActivity,p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        dialog.dismiss()
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    }
                    else{
                        startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                        finish()
                    }
                }
            })


    }


}