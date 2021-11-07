package com.example.fundo.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.home.Adapter
import com.example.fundo.home.AddNotesActivity
import com.example.fundo.home.GridActivity
import com.example.fundo.home.Notes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.drawerLayout
import kotlinx.android.synthetic.main.activity_home_new.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main_new.*
import service.AuthenticationService
import service.Storage
import viewmodels.HomeViewModel
import java.util.*
import kotlin.collections.ArrayList

class HomeActivityNew : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>

    private lateinit var tempArrayList: ArrayList<Notes>


    lateinit var preferences: SharedPreferences
    //lateinit var logoutBtn: Button


    private lateinit var alertDialog: AlertDialog
    lateinit var profile: ImageView
    lateinit var imageUri: Uri

    private lateinit var homeViewModel: HomeViewModel

    //lateinit var profileIcon:ImageView

    private var menu: Menu? = null
    lateinit var addNoteButton: FloatingActionButton

    //grid image
    lateinit var gridImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_new)

        setSupportActionBar(toolbar3)

        val toggle =
            ActionBarDrawerToggle(this, drawerLayoutNew, toolbar3, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true  //enable handburger sign
        drawerLayoutNew.addDrawerListener(toggle)
        toggle.syncState()

        //handling clicks
        nav_menu_new.setNavigationItemSelectedListener(this)

        addNoteButton = findViewById(R.id.createNoteBtn) //add note button

        //add grid image
        gridImageView = findViewById(R.id.imageView)

        preferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val name = preferences.getString("NAME", "")
        val email = preferences.getString("EMAIL", "")
        //Toast.makeText(applicationContext,"$name $email",Toast.LENGTH_SHORT).show()


        if (name != null) {
            if (email != null) {
                createProfileOverlay(name, email)
            }
        }

        displayIcon()



        addNoteButton.setOnClickListener {
            Toast.makeText(this, "add not fab clicked", Toast.LENGTH_SHORT).show()


            var intent = Intent(this, AddNotesActivity::class.java)
            startActivity(intent)

//            val fragmentManager = supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragmentContainerHome, AddNoteFragment())
//            fragmentTransaction.commit()

        }

        gridImageView.setOnClickListener {
            Toast.makeText(applicationContext, "grid image clicked", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, GridActivity::class.java)
            startActivity(intent)
        }


        noteRecyclerView = findViewById(R.id.noteList)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()
        tempArrayList = arrayListOf<Notes>()
        getNotesData()


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

        drawerLayoutNew.closeDrawer(GravityCompat.START)

        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(applicationContext, "$newText", Toast.LENGTH_SHORT).show()
                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                Log.d("text", searchText)
                if (searchText.isNotEmpty()) {
                    noteArrayList.forEach {
                        if (it.title!!.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            Log.d("text match", it.title!!)
                            tempArrayList.add(it)
                        }
                    }

                    noteRecyclerView.adapter!!.notifyDataSetChanged()
                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(noteArrayList)
                    noteRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })

        //displayIcon()


        // return true

        return super.onCreateOptionsMenu(menu)
    }


    private fun displayIcon() {
        val storage = Storage()
        val uid = AuthenticationService().getUid()
        storage.retrieveImage(uid) { status, image ->

            if (status) {
                //Toast.makeText(applicationContext,"shud work",Toast.LENGTH_SHORT).show()

                //profile.setImageURI(imageUri)

                profile.setImageBitmap(image)

                val item = menu?.findItem(R.id.profileIcon)
                //item?.icon = BitmapDrawable(image)

                item?.icon = BitmapDrawable(image)
                // Toast.makeText(applicationContext,"didn't work",Toast.LENGTH_SHORT).show()


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


    private fun createProfileOverlay(name: String, email: String) {
        val profileView =
            LayoutInflater.from(this@HomeActivityNew).inflate(R.layout.dialog_box, null)
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
            //open gallery
//            var openGalleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(openGalleryIntent,1000)

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


    private fun getNotesData() {

        lateinit var dbref: DatabaseReference
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val notes = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(notes!!)
                    }
                    tempArrayList.addAll(noteArrayList)
                    var adapter = Adapter(tempArrayList)
                    noteRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : Adapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            Toast.makeText(
                                applicationContext,
                                "clicking on $position",
                                Toast.LENGTH_SHORT
                            ).show()
                            var note = noteArrayList[position]
                            var title = note.title.toString()
                            var content = note.content.toString()
                            Log.d("test", title)
                            Log.d("test", content)
                            Toast.makeText(applicationContext, "$title", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, "$content", Toast.LENGTH_SHORT)
                                .show()
                            var intent = Intent(this@HomeActivityNew, AddNotesActivity::class.java)
                            intent.putExtra("title", title)
                            intent.putExtra("content", content)
                            startActivity(intent)

                        }

                    })
                    //noteRecyclerView.adapter = Adapter(tempArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


}