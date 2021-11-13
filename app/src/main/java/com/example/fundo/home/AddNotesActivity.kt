package com.example.fundo.home

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.fundo.R
import com.example.fundo.ui.HomeActivityNew
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.label_dialog.view.*
import service.DatabaseHelper
import service.DatabaseService

class AddNotesActivity : AppCompatActivity() {

    lateinit var saveNotesButton: FloatingActionButton
    lateinit var backButton: ImageView
    lateinit var addtitle: EditText
    lateinit var addContent: EditText
    lateinit var updateNoteButton: Button
    lateinit var deleteNoteButton: ImageView
    lateinit var labelNoteBtn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        addtitle = findViewById(R.id.inputTitle)
        addContent = findViewById(R.id.inputNote)

        saveNotesButton = findViewById(R.id.saveNoteBtn)
        backButton = findViewById(R.id.backButton)
        updateNoteButton = findViewById(R.id.updateBtn)
        deleteNoteButton = findViewById(R.id.deleteImage)
        labelNoteBtn = findViewById(R.id.createLabelBtn)

        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")

        addtitle.setText(title)
        addContent.setText(content)

        var helper = DatabaseHelper(applicationContext)

        saveNotesButton.setOnClickListener {
            Toast.makeText(this, "save notes button clicked", Toast.LENGTH_SHORT).show()
            var title = addtitle.text.toString()
            var note = addContent.text.toString()

            if (title.isNotEmpty() || note.isNotEmpty()) {
                var databaseService = DatabaseService(this)
                databaseService.addNotesToDB(title,note)

                databaseService.addDataToDB(title,note,helper)
                Toast.makeText(this,"note saved to sqlite",Toast.LENGTH_SHORT).show()

//                val noteInfo = Note(null,title,note)
//                GlobalScope.launch(Dispatchers.IO){
//                    NoteDatabase.getInstance(this@AddNotesActivity).noteDao().insert(noteInfo)
//                }
                Toast.makeText(this,"record saved to sqlite room",Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(applicationContext, "Empty note discarded", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        backButton.setOnClickListener {
            Toast.makeText(this, "back button clicked", Toast.LENGTH_SHORT).show()
            gotoHomePage()
        }

        updateNoteButton.setOnClickListener {
            Log.d("update note", "update button clicked")
            Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show()
            var key = title + content
            Toast.makeText(this, "$key", Toast.LENGTH_SHORT).show()
            var newTitle = addtitle.text.toString()
            var newContent = addContent.text.toString()
            var databaseService = DatabaseService(this)
            databaseService.updateNotesToDB(key,newTitle,newContent)

            if (title != null) {
                databaseService.updateDataToDB(title,newTitle,newContent,helper)
            }


//            val noteInfo = Note(null,newTitle,newContent)
//            GlobalScope.launch(Dispatchers.IO){
//                NoteDatabase.getInstance(this@AddNotesActivity).noteDao().update(noteInfo)
//
//            }
            Toast.makeText(this, "updated in sqlite room", Toast.LENGTH_SHORT).show()

        }

        deleteNoteButton.setOnClickListener {
            var key = title + content
            Toast.makeText(this,"delete button clicked",Toast.LENGTH_SHORT).show()
            var databaseService = DatabaseService(this)
            if (title != null) {
                databaseService.deleteNotesFromDB(key,title)
            }

            if (title != null) {
                databaseService.deleteDataFromDB(title,helper)
            }

//            GlobalScope.launch(Dispatchers.IO){
//                if (title != null) {
//                    NoteDatabase.getInstance(this@AddNotesActivity).noteDao().delete(title)
//                }
//            }
            Toast.makeText(this, "deleted from sqlite", Toast.LENGTH_SHORT).show()
        }

        labelNoteBtn.setOnClickListener {
            Toast.makeText(this,"label button clicked",Toast.LENGTH_SHORT).show()
            //inflate the dialog with custom view
            val dialogView = LayoutInflater.from(this).inflate(R.layout.label_dialog,null)
            //AlertDialogBuilder
            val builder = AlertDialog.Builder(this)
                .setView(dialogView).setTitle("Label")
            //show dialog
            val alertDialog = builder.show()
            //label button click
            dialogView.dialogLabelBtn.setOnClickListener {
                Toast.makeText(this,"add label clicked",Toast.LENGTH_SHORT).show()
                //dismiss dialog
                alertDialog.dismiss()
                //get text from editText
                val labelInput = dialogView.dialogLabelInput.text.toString()
                Toast.makeText(this,"$labelInput",Toast.LENGTH_SHORT).show()
            }
            dialogView.dialogCancelBtn.setOnClickListener {
                //dismiss the dialog
                alertDialog.dismiss()
            }
        }
    }

    private fun gotoHomePage() {
        var intent = Intent(this, HomeActivityNew::class.java)
        startActivity(intent)
    }
}