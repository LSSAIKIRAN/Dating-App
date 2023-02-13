package com.kiran.mydating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.kiran.mydating.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle : ActionBarDrawerToggle ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        binding.navigationView.setNavigationItemSelectedListener(this)




        val navController = findNavController(R.id.fragment)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.favorite -> {
               Toast.makeText(this, "rating", Toast.LENGTH_SHORT).show()
           }
           R.id.rating -> {
               Toast.makeText(this, "rating", Toast.LENGTH_SHORT).show()
           }
           R.id.share -> {
               Toast.makeText(this, "rating", Toast.LENGTH_SHORT).show()
           }
           R.id.termsCondition -> {
               Toast.makeText(this, "rating", Toast.LENGTH_SHORT).show()
           }
           R.id.privacy -> {
               Toast.makeText(this, "rating", Toast.LENGTH_SHORT).show()
           }

       }
        return true
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
        }
        else
            super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.close()
        }
        else
            super.onBackPressed()
    }

}