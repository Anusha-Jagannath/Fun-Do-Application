package com.example.fundo.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.service.Database
import com.example.fundo.ui.HomeActivityNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_grid.*

class HomeGridActivity : AppCompatActivity() {
    private lateinit var backImage: ImageView
    private lateinit var noteRecyclerView: RecyclerView
    private lateinit var noteArrayList: ArrayList<Notes>
    var page = 1
    var isLoading = false
    val limit = 10
    lateinit var adapter: NotesAdapter
    lateinit var database:Database
    lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        backImage = findViewById(R.id.backImage)
        backImage.setOnClickListener {
            Log.d("GridActivity", "back button clicked")
            var intent = Intent(this, HomeActivityNew::class.java)
            startActivity(intent)
        }
        noteRecyclerView = findViewById(R.id.noteList)
        noteRecyclerView.layoutManager = GridLayoutManager(this, 2)
        noteRecyclerView.setHasFixedSize(true)
        noteArrayList = arrayListOf<Notes>()
        adapter = NotesAdapter(noteArrayList)
        noteRecyclerView.adapter = adapter

        key = " "
        database = Database()
        loadData()

        adapter.setOnItemClickListener(object : NotesAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    applicationContext,
                    "clicked on $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
        noteRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.i("grid","increment page test")
                    val visibleItemCount =
                        (noteRecyclerView.layoutManager as GridLayoutManager).childCount
                    val pastVisibleItem =
                        (noteRecyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                    val total = adapter.itemCount
                    if(total < pastVisibleItem + 3) {
                        if(!isLoading) {
                            isLoading = true
                            loadData()
                        }
                    }
            }
        })
    }

    //added method to load data
    private fun loadData() {
        swipeRefresh.isRefreshing = true
        database.get(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (noteSnapshot in snapshot.children) {
                        val notes = noteSnapshot.getValue(Notes::class.java)
                        noteArrayList.add(notes!!)
                        key = noteSnapshot.key.toString()
                        if (key != null) {
                            Log.d("key",key)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
                isLoading = false
                swipeRefresh.isRefreshing = false
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun loadFirstPage() {
        val start = (page - 1) * limit
        val end = page * limit
        Log.i("Grid","Start: $start $end")
        lateinit var dbref: DatabaseReference
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("user").child(uid).child("Notes")
        for (i in start..end) {
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (noteSnapshot in snapshot.children) {
                            val notes = noteSnapshot.getValue(Notes::class.java)
                            noteArrayList.add(notes!!)

                        }
                    }
                    adapter.notifyDataSetChanged()
                    isLoading = false

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

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
                    var adapter = NotesAdapter(noteArrayList)
                    noteRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : NotesAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            Toast.makeText(
                                applicationContext,
                                "clicked on $position",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}