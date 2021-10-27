package com.example.fundo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.fundo.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_main.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true  //enable handburger sign
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //handling clicks
        nav_menu.setNavigationItemSelectedListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when(itemView) {
            R.id.profileIcon -> Toast.makeText(applicationContext,"profile icon clicked",Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.home) {
            Toast.makeText(applicationContext,"home clicked",Toast.LENGTH_SHORT).show()
        }
        if(item.itemId == R.id.about) {
            Toast.makeText(applicationContext,"about clicked",Toast.LENGTH_SHORT).show()
        }
        if(item.itemId == R.id.contact) {
            Toast.makeText(applicationContext,"contact clicked",Toast.LENGTH_SHORT).show()
        }

        if(item.itemId == R.id.settings) {
            Toast.makeText(applicationContext,"settings clicked",Toast.LENGTH_SHORT).show()
        }

        return true
    }
}