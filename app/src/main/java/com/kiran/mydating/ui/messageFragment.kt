package com.kiran.mydating.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kiran.mydating.Adapter.MessageUserAdapter
import com.kiran.mydating.Utils.Config
import com.kiran.mydating.databinding.FragmentMessageBinding


class messageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(layoutInflater)

       getData()


        return binding.root
    }

    private fun getData() {
        Config.showDialog(requireContext())
        var list = arrayListOf<String>()
        var newlist = arrayListOf<String>()
        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children){
                        if (data.key!!.contains(currentId!!)){
                            list.add(data.key!!.replace(currentId, ""))
                            newlist.add(data.key!!)
                        }
                    }

                    binding.recyclerView.adapter =
                        MessageUserAdapter(requireContext(), list, newlist)
                    Config.hideDialog()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })

    }


}