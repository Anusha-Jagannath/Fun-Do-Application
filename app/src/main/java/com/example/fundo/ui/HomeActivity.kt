package com.example.fundo.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.home.NotesAdapter
import com.example.fundo.home.AddNotesActivity
import com.example.fundo.home.Notes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_main.*
import com.example.fundo.service.AuthenticationService
import com.example.fundo.service.Storage
import com.example.fundo.viewmodels.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>
    lateinit var preferences: SharedPreferences
    private lateinit var alertDialog: AlertDialog
    lateinit var profile: ImageView
    lateinit var imageUri: Uri
    private lateinit var homeViewModel: HomeViewModel
    private var menu: Menu? = null
    lateinit var addNoteButton: FloatingActionButton
    lateinit var gridImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        val toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true  //enable handburger sign
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //handling clicks
        nav_menu.setNavigationItemSelectedListener(this)
        addNoteButton = findViewById(R.id.createNoteBtn) //add note button
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory())[HomeViewModel::class.java]
        preferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val name = preferences.getString("NAME", "")
        val email = preferences.getString("EMAIL", "")
        Toast.makeText(applicationContext, "$name $email", Toast.LENGTH_SHORT).show()

        noteRecyclerView = findViewById(R.id.noteList)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()
        getNotesData()

        addNoteButton.setOnClickListener {
            Toast.makeText(this, "add not fab clicked", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, AddNotesActivity::class.java)
            startActivity(intent)
        }

        if (name != null) {
            if (email != null) {
                createProfileOverlay(name, email)
            }
        }

        displayIcon()
    }

    private fun getNotesData() {

        lateinit var dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Notes")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val notes = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(notes!!)
                    }

                    noteRecyclerView.adapter = NotesAdapter(noteArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        //displayIcon()
        return true
    }

    private fun displayIcon() {
        val storage = Storage()
        val uid = AuthenticationService().getUid()
        storage.retrieveImage(uid) { status, image ->
            if (status) {
                Toast.makeText(applicationContext, "shud work", Toast.LENGTH_SHORT).show()
                //profile.setImageURI(imageUri)
                profile.setImageBitmap(image)
                val item = menu?.findItem(R.id.profileIcon)
                //item?.icon = BitmapDrawable(image)
                item?.icon = BitmapDrawable(image)
                Toast.makeText(applicationContext, "didn't work", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when (itemView) {
            R.id.profileIcon -> {
                Toast.makeText(applicationContext, "profile icon clicked", Toast.LENGTH_SHORT)
                    .show()
                showProfile()
            }
        }
        return false
    }

    private fun showProfile() {
        alertDialog.show()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            Toast.makeText(applicationContext, "home clicked", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.about) {
            Toast.makeText(applicationContext, "about clicked", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.contact) {
            Toast.makeText(applicationContext, "contact clicked", Toast.LENGTH_SHORT).show()
        }

        if (item.itemId == R.id.settings) {
            Toast.makeText(applicationContext, "settings clicked", Toast.LENGTH_SHORT).show()
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun createProfileOverlay(name: String, email: String) {
        val profileView = LayoutInflater.from(this@HomeActivity).inflate(R.layout.dialog_box, null)
        alertDialog = AlertDialog.Builder(this)
            .setView(profileView)
            .create()

        val dialogLogoutButton: Button = profileView.findViewById(R.id.dailogueLogout)
        dialogLogoutButton.setOnClickListener {
            homeViewModel.logout()
            Toast.makeText(applicationContext, "logout success", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        profile = profileView.findViewById(R.id.dialogProfile)
        profile.setOnClickListener {
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 100)
        }
        val userNameText: TextView = profileView.findViewById(R.id.dailogueuserName)
        val userEmailText: TextView = profileView.findViewById(R.id.dailogueEmail)
        userNameText.text = "Name: $name"
        userEmailText.text = "EmailId: $email"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            profile.setImageURI(imageUri)
            val storage = Storage()
            val uid = homeViewModel.getUid()
            storage.uploadImage(uid, imageUri)
            displayIcon()

        }
    }

}



