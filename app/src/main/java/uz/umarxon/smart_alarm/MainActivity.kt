package uz.umarxon.smart_alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.deleteRecording
import kotlinx.android.synthetic.main.activity_main.name
import kotlinx.android.synthetic.main.activity_main.playRecording
import kotlinx.android.synthetic.main.activity_main.selectTimDate
import kotlinx.android.synthetic.main.activity_main.setTime
import kotlinx.android.synthetic.main.activity_main.startRecording
import kotlinx.android.synthetic.main.activity_main.stopRecording
import kotlinx.android.synthetic.main.activity_main.timeTxt
import kotlinx.android.synthetic.main.activity_main.vibrate
import kotlinx.android.synthetic.main.activity_main.vibrate_layout
import uz.umarxon.smart_alarm.DB.Entity.AlarmModel
import uz.umarxon.valyutaarxiv22122021.DB.Database.AppDatabase
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var mHour = 0
    var mMin = 0
    var mYear = 0
    var mMonth = 0
    var mDay = 0
    var dateAndTimeSelected = false
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    var isRecorded = false

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = resources.getColor(R.color.white)
        window.statusBarColor = resources.getColor(R.color.purple_500)
        setContentView(R.layout.activity_main)

        selectTimDate.setOnClickListener {
            val datepicker = DatePickerDialog(this)

            vibrate_layout.setOnClickListener {

                vibrate.isChecked = !vibrate.isChecked

            }

            datepicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth

                val timePicker = TimePickerDialog(
                    this,
                    { view, hourOfDay, minute ->

                        mHour = hourOfDay
                        mMin = minute
                        timeTxt.setText("$mYear//$mMonth//$mDay || $mHour:$mMin")
                        dateAndTimeSelected = true

                    },
                    24, 60, true,
                )

                timePicker.show()
            }

            datepicker.show()
        }

        timeTxt.setOnClickListener {
            val datepicker = DatePickerDialog(this)

            vibrate_layout.setOnClickListener {

                vibrate.isChecked = !vibrate.isChecked

            }

            datepicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth

                val timePicker = TimePickerDialog(
                    this,
                    { view, hourOfDay, minute ->

                        mHour = hourOfDay
                        mMin = minute
                        timeTxt.setText("$mYear//$mMonth//$mDay || $mHour:$mMin")
                        dateAndTimeSelected = true

                    },
                    24, 60, true,
                )

                timePicker.show()
            }

            datepicker.show()
        }

        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            5 * 1000,
            pendingIntent
        );

        setTime.setOnClickListener {
            setTimer()
        }

        startRecording.visibility = View.VISIBLE
        stopRecording.visibility = View.GONE
        playRecording.visibility = View.GONE
        deleteRecording.visibility = View.GONE

        startRecording.setOnClickListener {
            startRecording.visibility = View.GONE
            stopRecording.visibility = View.VISIBLE
            playRecording.visibility = View.GONE

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {

                output = externalCacheDir?.absolutePath + "/${SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_SSS").format(Date())}.mp3"
                mediaRecorder = MediaRecorder()

                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                mediaRecorder?.setOutputFile(output)

                startRecording()
            }

        }
        stopRecording.setOnClickListener {
            startRecording.visibility = View.GONE
            stopRecording.visibility = View.GONE
            playRecording.visibility = View.VISIBLE
            stopRecording()
            isRecorded = true
        }
        deleteRecording.setOnClickListener {
            output = ""
            isRecorded = false
            startRecording.visibility = View.VISIBLE
            stopRecording.visibility = View.GONE
            playRecording.visibility = View.GONE
            deleteRecording.visibility = View.GONE
        }
        playRecording.setOnClickListener {
            val mp = MediaPlayer()

            mp.apply {
                setDataSource(output)
                prepare()
                start()
            }

        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun setTimer() {

        val name = name.text.toString().trim()
        if (dateAndTimeSelected && name.isNotEmpty() && isRecorded) {
            val calAlarm = Calendar.getInstance()

            calAlarm.set(Calendar.YEAR, mYear)
            calAlarm.set(Calendar.MONTH, mMonth)
            calAlarm.set(Calendar.DAY_OF_MONTH, mDay)
            calAlarm.set(Calendar.HOUR_OF_DAY, mHour)
            calAlarm.set(Calendar.MINUTE, mMin)
            calAlarm.set(Calendar.SECOND, 0)

            val alarmModel = AlarmModel(
                SimpleDateFormat("dd/MM/yyyy  hh:mm").format(calAlarm.time),
                vibrate.isChecked,
                name,
                output,
                true
            )
            AppDatabase.getInstance(this).courseDao().addAlarm(alarmModel)
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Malumotlar To'liq Emas", Toast.LENGTH_SHORT).show()
        }

    }

    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording(){
        if(state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
        }else{
            Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    /* @SuppressLint("SimpleDateFormat")
     private fun setTimer() {

         val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

         *//*val date = Date()

        val calAlarm = Calendar.getInstance()
        val calNow = Calendar.getInstance()

        calAlarm.time = date
        calNow.time = date

        calAlarm.set(Calendar.YEAR, mYear)
        calAlarm.set(Calendar.MONTH, mMonth)
        calAlarm.set(Calendar.DAY_OF_MONTH, mDay)
        calAlarm.set(Calendar.HOUR_OF_DAY, mHour)
        calAlarm.set(Calendar.MINUTE, mMin)
        calAlarm.set(Calendar.SECOND, 0)*//*

        val alarmModel = AlarmModel("$mHour:$mMin",vibrate.isChecked, "Salom", "no", true)
        AppDatabase.getInstance(this).courseDao().addAlarm(alarmModel)
        val i = Intent(this, MyReceiver::class.java)
        i.putExtra("vibrate", vibrate.isChecked)
        i.putExtra("alarm", alarmModel)

        val id = System.currentTimeMillis().toInt()
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, id, i, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, id, i, 0)
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pendingIntent)

        now.text = SimpleDateFormat("dd/MM/yyyy  hh:mm:ss").format(calAlarm.time)
        day.text = ""
        Toast.makeText(this, "Alarm set ${SimpleDateFormat("dd/MM/yyyy  hh:mm:ss").format(calAlarm.time)}", Toast.LENGTH_SHORT).show()
        Log.d("murodhonov", "setTimer: ${SimpleDateFormat("dd/MM/yyyy  hh:mm:ss").format(calAlarm.time)}")

    }*/
}