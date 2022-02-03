package uz.umarxon.smart_alarm

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Vibrator
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notification.*
import uz.umarxon.smart_alarm.DB.Entity.AlarmModel
import uz.umarxon.valyutaarxiv22122021.DB.Database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        window.navigationBarColor = resources.getColor(R.color.myPrimary)
        window.statusBarColor = resources.getColor(R.color.color2)
        setContentView(R.layout.activity_notification)

        val alarm = intent.getSerializableExtra("alarm") as AlarmModel

        alarm.isActive = false

        message.text = alarm.message

        val r = MediaPlayer()

        r.apply {
            setDataSource(alarm.recordedSound)
            prepare()
            start()
        }

        AppDatabase.getInstance(this).courseDao().updateAlarm(alarm)

        if (intent.getBooleanExtra("vibrate", false)) {
            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(2000)
        }

        r.start()

        timeText.text = SimpleDateFormat("hh:mm").format(Date())

        stop.setOnClickListener {
            r.stop()
            finish()
        }
    }
}