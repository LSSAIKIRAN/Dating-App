package com.kiran.mydating.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kiran.mydating.Activities.MessageActivity
import com.kiran.mydating.databinding.ItemUserBinding
import com.kiran.mydating.model.UserModel

class DatingAdapter(val context : Context, val list : ArrayList<UserModel>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding : ItemUserBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return DatingViewHolder(ItemUserBinding.inflate(LayoutInflater.from(context), parent, false))

    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {

        holder.binding.userName.text = list[position].name
        holder.binding.userEmail.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.userImage)


        holder.binding.chat.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userId", list[position].number)
            context.startActivity(intent)
        }




    }
}