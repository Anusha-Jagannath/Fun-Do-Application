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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.fundo.R
import com.example.fundo.home.*
import com.example.fundo.label.AddLabel
import com.example.fundo.label.ArchivedNotes
import com.example.fundo.label.ReminderNotes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_new.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main_new.*
import com.example.fundo.service.AuthenticationService
import com.example.fundo.service.Database
import com.example.fundo.service.Storage
import com.example.fundo.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_grid.*
import java.util.*
import kotlin.collections.ArrayList

class HomeActivityNew : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>
    private lateinit var tempArrayList: ArrayList<Notes>
    private lateinit var preferences: SharedPreferences
    private lateinit var alertDialog: AlertDialog
    lateinit var profile: ImageView
    lateinit var imageUri: Uri
    private lateinit var homeViewModel: HomeViewModel
    private var menu: Menu? = null
    lateinit var addNoteButton: FloatingActionButton

    //grid image
    lateinit var gridImageView: ImageView


    //lateinit var adapter: NotesAdapter
    //
    // added night
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var key: String
    lateinit var database: Database
    lateinit var adapter: NotesAdapter
    var isLoading = false

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
        }
        gridImageView.setOnClickListener {
            Toast.makeText(applicationContext, "grid image clicked", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, HomeGridActivity::class.java)
            startActivity(intent)
        }
        swipeRefreshLayout = findViewById(R.id.swipe)
        noteRecyclerView = findViewById(R.id.noteList)
        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()
        tempArrayList = arrayListOf<Notes>()
        //adapter = NotesAdapter(noteArrayList)
        //noteRecyclerView.adapter = adapter
        //adapter = NotesAdapter(tempArrayList)
        //noteRecyclerView.adapter = adapter
        key = " "
        database = Database()
        loadData()
        //getNotesAllData()
        noteRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.i("grid","increment page test")
                val visibleItemCount = (noteRecyclerView.layoutManager as LinearLayoutManager).childCount
                val lastVisibleItem =
                    (noteRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                Log.d("RECORD",visibleItemCount.toString())
                Log.d("RECORD",lastVisibleItem.toString())
                val total = adapter.itemCount
                Log.d("TOTAL",total.toString())
                if(total < lastVisibleItem+3) {
                    if(!isLoading) {
                        isLoading = true
                        loadData()
                    }
                }



            }
        })
    }


    //added nw which helped me
    private fun loadData() {
        swipeRefreshLayout.isRefreshing = true
        database.get(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val notes = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(notes!!)
                        key = noteSnapshot.key.toString()
                        if (key != null) {
                            Log.d("key", key)
                        }
                    }

                    //added mulpa
                    Log.d("HOME", noteArrayList.toString())
                    tempArrayList.addAll(noteArrayList)
                    adapter = NotesAdapter(tempArrayList)
                    noteRecyclerView.adapter = adapter

                    adapter.notifyDataSetChanged()
                    isLoading = false
                    swipeRefreshLayout.isRefreshing = false

                    adapter.setOnItemClickListener(object : NotesAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            Toast.makeText(applicationContext, "clickinggg on $position", Toast.LENGTH_SHORT).show()
                            var note = noteArrayList[position]
                            var title = note.title.toString()
                            var content = note.content.toString()
                            var label1 = note.label1.toString()
                            var label2 = note.label2.toString()
                            Log.d("test", title)
                            Log.d("test", content)
                            Toast.makeText(applicationContext, "$title", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, "$content", Toast.LENGTH_SHORT)
                                .show()
                            var intent = Intent(this@HomeActivityNew, AddNotesActivity::class.java)
                            intent.putExtra("title", title)
                            intent.putExtra("content", content)
                            intent.putExtra("label1", label1)
                            intent.putExtra("label2", label2)
                            startActivity(intent)

                        }

                    })


                    //mulpa


                }
                //adapter.notifyDataSetChanged()
                //isLoading = false
                //swipeRefreshLayout.isRefreshing = false

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }




    private fun getNotesAllData() {
        lateinit var dbref: DatabaseReference
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(note!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            Toast.makeText(applicationContext, "home clicked", Toast.LENGTH_SHORT).show()
        }
        if (item.itemId == R.id.about) {
            Toast.makeText(applicationContext, "about clicked", Toast.LENGTH_SHORT).show()
            gotoArchivedNotesPage()
        }
        if (item.itemId == R.id.contact) {
            Toast.makeText(applicationContext, "contact clicked", Toast.LENGTH_SHORT).show()
            gotoDeletePage()
        }

        if (item.itemId == R.id.settings) {
            Toast.makeText(applicationContext, "settings clicked", Toast.LENGTH_SHORT).show()
            gotoReminderPage()
        }

        if (item.itemId == R.id.addLabel) {
            Toast.makeText(applicationContext, "add label clicked", Toast.LENGTH_SHORT).show()
            gotoAddLabelPage()
        }

        if (item.itemId == R.id.endless) {
            Toast.makeText(applicationContext, "add label clicked", Toast.LENGTH_SHORT).show()

        }
        drawerLayoutNew.closeDrawer(GravityCompat.START)
        return true
    }

    

    private fun gotoReminderPage() {
        var intent = Intent(this,ReminderNotes::class.java)
        startActivity(intent)
    }

    private fun gotoArchivedNotesPage() {
       var intent = Intent(this,ArchivedNotes::class.java)
        startActivity(intent)
    }

    private fun gotoAddLabelPage() {
        var intent = Intent(this, AddLabel::class.java)
        startActivity(intent)
    }

    private fun gotoDeletePage() {
        var intent = Intent(this, DeletedNotes::class.java)
        startActivity(intent)
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
                //adapter.filter.filter(newText)

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
                profile.setImageBitmap(image)
                val item = menu?.findItem(R.id.profileIcon)
                item?.icon = BitmapDrawable(image)
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
                    Log.d("HOME", noteArrayList.toString())
                    tempArrayList.addAll(noteArrayList)
                    var adapter = NotesAdapter(tempArrayList)
                    noteRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : NotesAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            Toast.makeText(
                                applicationContext,
                                "clicking on $position",
                                Toast.LENGTH_SHORT
                            ).show()
                            var note = noteArrayList[position]
                            var title = note.title.toString()
                            var content = note.content.toString()
                            var label1 = note.label1.toString()
                            var label2 = note.label2.toString()
                            Log.d("test", title)
                            Log.d("test", content)
                            Toast.makeText(applicationContext, "$title", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, "$content", Toast.LENGTH_SHORT)
                                .show()
                            var intent = Intent(this@HomeActivityNew, AddNotesActivity::class.java)
                            intent.putExtra("title", title)
                            intent.putExtra("content", content)
                            intent.putExtra("label1", label1)
                            intent.putExtra("label2", label2)
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