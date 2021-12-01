package com.example.fundo.label

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundo.R
import com.example.fundo.home.LabelsAdapter
import com.example.fundo.home.NotesAdapter
import com.example.fundo.model.Labels
import com.example.fundo.service.Database
import com.example.fundo.service.DatabaseService
import com.example.fundo.ui.HomeActivityNew
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_label.*

class AddLabel : AppCompatActivity() {
    private lateinit var backHome: ImageView
    private lateinit var labelInput: EditText
    private lateinit var addLabel: ImageView
    private lateinit var labelRecyclerView: RecyclerView
    private lateinit var labelArrayList: ArrayList<Labels>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_label)
        backHome = findViewById(R.id.backToHome)
        labelInput = findViewById(R.id.label)
        addLabel = findViewById(R.id.addLabelButton)
        labelRecyclerView = findViewById(R.id.labelList)
        labelRecyclerView.layoutManager = LinearLayoutManager(this)
        labelRecyclerView.setHasFixedSize(true)
        labelArrayList = arrayListOf<Labels>()

        backHome.setOnClickListener {
            Toast.makeText(this, "back button clicked", Toast.LENGTH_SHORT).show()
            goToHomePage()
        }
        addLabel.setOnClickListener {
            Toast.makeText(this, "add button clicked", Toast.LENGTH_SHORT).show()
            var inputLabel = labelInput.text.toString()
            Toast.makeText(this, "$inputLabel", Toast.LENGTH_SHORT).show()
            var databaseService = DatabaseService(this)
            databaseService.addLabelToDB(inputLabel)
        }

        getLabels()
    }

    private fun goToHomePage() {
        var intent = Intent(this, HomeActivityNew::class.java)
        startActivity(intent)
    }

    private fun getLabels() {
        lateinit var dbref: DatabaseReference
        var uid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Label").child(uid)
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (labelSnapshot in snapshot.children) {
                        val labels = labelSnapshot.getValue(Labels::class.java)
                        labelArrayList.add(labels!!)
                    }
                    Log.d("ARRAy",labelArrayList.toString())
                    var adapter = LabelsAdapter(labelArrayList)
                    labelRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : LabelsAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            Toast.makeText(
                                this@AddLabel,
                                "clicked on delete $position",
                                Toast.LENGTH_SHORT
                            ).show()
                            var label = labelArrayList[position]
                            var key = label.labelId
                            Log.d("KEY", key.toString())
                            var database = Database()
                            if (key != null) {
                                database.deleteLabelFromDB(key)
                            }
                            labelArrayList.remove(label) //added
                            adapter.notifyDataSetChanged()

                        }

                        override fun editClick(position: Int,updatedLabel: String) {
                            Toast.makeText(
                                this@AddLabel,
                                "clicked on update $position",
                                Toast.LENGTH_SHORT
                            ).show()
                            var label = labelArrayList[position]
                            var key = label.labelId
                            Log.d("UP", key.toString())
                            Log.d("UP", updatedLabel)
                            var database = Database()
                            if (key != null) {
                                if (updatedLabel != null) {
                                    database.updateLabel(key, updatedLabel)
                                }
                            }

                        }

                    })
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}