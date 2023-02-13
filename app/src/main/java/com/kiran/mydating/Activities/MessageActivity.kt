package com.kiran.mydating.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kiran.mydating.Adapter.MessageAdapter
import com.kiran.mydating.R
import com.kiran.mydating.databinding.ActivityMessageBinding
import com.kiran.mydating.model.MessageModel
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        
        getData(intent.getStringExtra("chat_id"))

        binding.sendBtn.setOnClickListener {
            if (binding.yourMessage.text!!.isEmpty()){
                Toast.makeText(this, "Start chatting here", Toast.LENGTH_SHORT).show()
            }
            else{
                sendMessage(binding.yourMessage.text.toString())
            }
        }
    }

    private fun getData(chatId: String?) {
        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = arrayListOf<MessageModel>()

                    for (show in snapshot.children){
                        list.add(show.getValue(MessageModel::class.java)!!)
                    }

                    binding.recyclerView2.adapter = MessageAdapter(this@MessageActivity,list)
                }



                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MessageActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun sendMessage(msg:String) {

        val receiverId = intent.getStringExtra("userId")
        val senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        val chatId = senderId+receiverId
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentTime"] =currentTime
        map["currentDate"] =currentDate


        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId)

        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
                    if (it.isSuccessful){
                        binding.yourMessage.text = null
                        Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }


    }















}