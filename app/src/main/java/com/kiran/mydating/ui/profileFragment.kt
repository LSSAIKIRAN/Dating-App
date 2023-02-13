package com.kiran.mydating.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kiran.mydating.Activities.EditProfileActivity
import com.kiran.mydating.R
import com.kiran.mydating.Utils.Config
import com.kiran.mydating.auth.SignInActivity
import com.kiran.mydating.databinding.FragmentProfileBinding
import com.kiran.mydating.model.UserModel


class profileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Config.showDialog(requireContext())
        
       binding = FragmentProfileBinding.inflate(layoutInflater)


        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if (it.exists()){
                    val data = it.getValue(UserModel::class.java)

                    binding.name.setText(data!!.name.toString())
                    binding.email.setText(data.email.toString())
                    binding.city.setText(data.city.toString())
                    binding.number.setText(data.number.toString())


                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.profile).into(binding.userImg)

                    Config.hideDialog()
                }
            }

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }



        return binding.root
    }


}