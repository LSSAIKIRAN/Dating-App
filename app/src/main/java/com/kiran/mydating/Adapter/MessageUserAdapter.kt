package com.kiran.mydating.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kiran.mydating.Activities.MessageActivity
import com.kiran.mydating.databinding.UserItemMessageBinding
import com.kiran.mydating.model.UserModel

class MessageUserAdapter(val context: Context, val list: ArrayList<String>, val chatKey : List<String>) :
    RecyclerView.Adapter<MessageUserAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val binding: UserItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(UserItemMessageBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position]).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val data = snapshot.getValue(UserModel::class.java)

                        Glide.with(context).load(data!!.image).into(holder.binding.userImg)
                        holder.binding.userName.text = data.name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }

            })


        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("chat_id", chatKey[position])
            context.startActivity(intent)
        }



    }
}