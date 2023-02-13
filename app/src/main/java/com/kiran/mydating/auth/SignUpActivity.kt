package com.kiran.mydating.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kiran.mydating.MainActivity
import com.kiran.mydating.R
import com.kiran.mydating.Utils.Config
import com.kiran.mydating.Utils.Config.hideDialog
import com.kiran.mydating.databinding.ActivitySignUpBinding
import com.kiran.mydating.model.UserModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private var imageUri: Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it

        binding.profileImage.setImageURI(imageUri)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.profileImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.toContinue.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        if (binding.userName.text.toString().isEmpty() || binding.email.text.toString().isEmpty()
            || binding.city.text.toString().isEmpty() || imageUri == null
        ) {
            Toast.makeText(this, "Correct the fields", Toast.LENGTH_SHORT).show()
        } else if (!binding.termsConditions.isChecked) {
            Toast.makeText(this, "Agree the terms and conditions", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener {
                    hideDialog()
                    Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                hideDialog()
                Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?) {

        val data = UserModel(
            name = binding.userName.text.toString(),
            image = imageUrl.toString(),
            email = binding.email.text.toString(),
            city = binding.city.text.toString(),
        )
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener {
                hideDialog()
                if(it.isSuccessful){
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                    Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show()
                }
            }

    }


}