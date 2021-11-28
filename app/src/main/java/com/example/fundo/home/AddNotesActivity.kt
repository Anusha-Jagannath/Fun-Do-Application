package com.example.fundo.home

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.fundo.*
import com.example.fundo.R
import com.example.fundo.room.note.Label
import com.example.fundo.room.note.NoteDatabase
import com.example.fundo.room.note.NoteLabelRef
import com.example.fundo.ui.HomeActivityNew
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.label_dialog.view.*
import com.example.fundo.service.Database
import com.example.fundo.service.DatabaseHelper
import com.example.fundo.service.DatabaseService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AddNotesActivity : AppCompatActivity() {

    lateinit var saveNotesButton: FloatingActionButton
    lateinit var backButton: ImageView
    lateinit var addtitle: EditText
    lateinit var addContent: EditText
    lateinit var updateNoteButton: Button
    lateinit var deleteNoteButton: ImageView
    lateinit var labelNoteBtn: ImageView
    private lateinit var inputDate: EditText
    private lateinit var inputTime: EditText
    private lateinit var remindButton: Button
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var reminder: String
    var HOUR: Int = 0
    var MIN: Int = 0
    var YEAR: Int = 0
    var MM: Int = 0
    var DAY: Int = 0
    var timeFormat = SimpleDateFormat("hh:mm:a", Locale.US)
    var count = 0

    //@RequiresApi(Build.VERSION_CODES.O)
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
        inputDate = findViewById(R.id.date)
        inputTime = findViewById(R.id.time)
        remindButton = findViewById(R.id.setReminder)

        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")
        var label1 = intent.getStringExtra("label1")
        var label2 = intent.getStringExtra("label2")

        addtitle.setText(title)
        addContent.setText(content)

        var helper = DatabaseHelper(applicationContext)

        saveNotesButton.setOnClickListener {
            Toast.makeText(this, "save notes button clicked", Toast.LENGTH_SHORT).show()
            var title = addtitle.text.toString()
            var note = addContent.text.toString()

            if (title.isNotEmpty() || note.isNotEmpty()) {
                var databaseService = DatabaseService(this)
                databaseService.addNotesToDB(title, note)

                databaseService.addDataToDB(title, note, helper)
                Toast.makeText(this, "note saved to sqlite", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "record saved to sqlite room", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(applicationContext, "Empty note discarded", Toast.LENGTH_SHORT)
                    .show()
            }

            //val label = Label(null,"outing")
            //NoteDatabase.getInstance(this@AddNotesActivity).noteDao().insertLabel(label)

            //val ref = NoteLabelRef(1,1)
            //NoteDatabase.getInstance(this@AddNotesActivity).noteDao().insertNoteLabelRef(ref)

            //NoteDatabase.getInstance(this@AddNotesActivity).noteDao().getNotesWithLabels()
            //NoteDatabase.getInstance(this@AddNotesActivity).noteDao().getNotesWithLabelsById(1)

            //NoteDatabase.getInstance(this@AddNotesActivity).noteDao().getLabelsWithNotes()
            //NoteDatabase.getInstance(this@AddNotesActivity).noteDao().getLabelsWithNotesById(1)


        }

        backButton.setOnClickListener {
            Toast.makeText(this, "back button clicked", Toast.LENGTH_SHORT).show()
            gotoHomePage()
        }

        inputDate.setOnClickListener {
            var cal = Calendar.getInstance()
            var datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker, yy, mm, dd ->
                    date = "$dd-${mm + 1}-$yy"
                    inputDate.setText(date)
                    reminder = date
                    YEAR = yy
                    MM = mm
                    DAY = dd

                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
        inputTime.setOnClickListener {
            var cal = Calendar.getInstance()
            var timePicker =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hh, mi ->

                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hh)
                    selectedTime.set(Calendar.MINUTE, mi)
                    Toast.makeText(this, timeFormat.format(selectedTime.time), Toast.LENGTH_SHORT)
                        .show()
                    time = " $hh:$mi"
                    inputTime.setText(time)
                    reminder += time
                    Log.d("TIME", reminder)
                    HOUR = hh
                    MIN = mi

                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false)
            timePicker.show()
        }

        remindButton.setOnClickListener {
            Toast.makeText(this, "clicked on reminder", Toast.LENGTH_SHORT).show()
            var database = Database()
            var cal = Calendar.getInstance()
            var current = cal.time.toString()
            var note = Notes(title, content, current, null, null, reminder)
            database.saveReminderNotes(note)
            Toast.makeText(this, "reminder set", Toast.LENGTH_SHORT).show()
            database.saveNotes(note)
            Toast.makeText(this, "added in realtime", Toast.LENGTH_SHORT).show()
            Log.d("TIMER", YEAR.toString())
            Log.d("TIMER", MM.toString())
            Log.d("TIMER", DAY.toString())

            Log.d("TIMER", HOUR.toString())
            Log.d("TIMER", MIN.toString())
            //scheduleNotification()
            //myWorkManager()

        }

        updateNoteButton.setOnClickListener {
            Log.d("update note", "update button clicked")
            Toast.makeText(this, "update clicked", Toast.LENGTH_SHORT).show()
            var key = title + content
            Toast.makeText(this, "$key", Toast.LENGTH_SHORT).show()
            var newTitle = addtitle.text.toString()
            var newContent = addContent.text.toString()
            var databaseService = DatabaseService(this)
            databaseService.updateNotesToDB(key, newTitle, newContent, this)

            if (title != null) {
                databaseService.updateDataToDB(title, newTitle, newContent, helper)
            }
            Toast.makeText(this, "updated in sqlite room", Toast.LENGTH_SHORT).show()

        }

        deleteNoteButton.setOnClickListener {
            var key = title + content
            Toast.makeText(this, "delete button clicked", Toast.LENGTH_SHORT).show()
            var database = Database()
            val cal = Calendar.getInstance()
            var date = cal.time.toString()
            var notes = Notes(title, content, date)
            database.addDeletedNotesToDB(notes)
            var databaseService = DatabaseService(this)
            if (title != null) {
                databaseService.deleteNotesFromDB(key, title, this)
            }

            if (title != null) {
                databaseService.deleteDataFromDB(title, helper)
            }
            Toast.makeText(this, "deleted from sqlite", Toast.LENGTH_SHORT).show()
        }

        labelNoteBtn.setOnClickListener {
            Toast.makeText(this, "label button clicked", Toast.LENGTH_SHORT).show()
            //inflate the dialog with custom view
            val dialogView = LayoutInflater.from(this).inflate(R.layout.label_dialog, null)
            //AlertDialogBuilder
            val builder = AlertDialog.Builder(this)
                .setView(dialogView).setTitle("Label")
            //show dialog
            val alertDialog = builder.show()
            //label button click
            dialogView.dialogLabelBtn.setOnClickListener {
                Toast.makeText(this, "add label clicked", Toast.LENGTH_SHORT).show()
                //dismiss dialog
                alertDialog.dismiss()
                //get text from editText
                val labelInput = dialogView.dialogLabelInput.text.toString()
                Toast.makeText(this, "$labelInput", Toast.LENGTH_SHORT).show()
                var key = title + content
                var database = Database()
                //database.saveLabel(key,labelInput)
                var noteLabel = Notes(title, content, label1, label2)
                if (count == 0) {
                    Log.d("IM", count.toString())
                    database.saveLabelToNotesOne(key, noteLabel, labelInput)
                    count++
                } else {
                    Log.d("CM", count.toString())
                    database.saveLabelToNotesSecond(key, noteLabel, labelInput)
                }

            }
            dialogView.dialogCancelBtn.setOnClickListener {
                //dismiss the dialog
                alertDialog.dismiss()
            }
        }

        myWorkManager()
    }

    private fun myWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        //added
        val calendar = Calendar.getInstance()
         calendar.set(YEAR, MM, DAY, HOUR, MIN)
        var time = calendar.timeInMillis
        Log.d("YTime",time.toString())
        //added

        val myRequest = PeriodicWorkRequest.Builder(MyWorker::class.java,
            2, TimeUnit.MINUTES).setConstraints(constraints).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("my_id",ExistingPeriodicWorkPolicy.KEEP,myRequest)
    }

    private fun simpleWork() {
        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .build()
        WorkManager.getInstance(this)
            .enqueue(mRequest)
    }
//here
    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        Log.d("D", "inside scheudle channel")
        val intent = Intent(applicationContext, Notification::class.java)
        val title = addtitle.text.toString()
        val content = addContent.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, content)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(YEAR, MM, DAY, HOUR, MIN)
        val time = calendar.timeInMillis

        Log.d("MIN",MIN.toString())
        Log.d("HOUR",HOUR.toString())

        Log.d("DAY",DAY.toString())
        Log.d("MONTH",MM.toString())
        Log.d("YEAR",YEAR.toString())
        Log.d("TIMEINMILLI",time.toString())


        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, time, pendingIntent
        )
        Log.d("T", "trigger")
        showAlert(time, title, content)
    }


    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
        AlertDialog.Builder(this)
            .setTitle("Notification scheduled").setMessage(
                "Title:" + title + "Message" + message + "At: " + dateFormat.format(date) + " " + timeFormat.format(
                    time
                )
            )
            .setPositiveButton("Okay", null)
            .show()


    }

    private fun getTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(YEAR, MM, DAY, HOUR, MIN)
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        Log.d("C", "inside notification channel")
        val name = "Notif Channel"
        val desc = "A description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


    }
//here
    private fun gotoHomePage() {
        var intent = Intent(this, HomeActivityNew::class.java)
        startActivity(intent)
    }
}