package uz.umarxon.smart_alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import uz.umarxon.valyutaarxiv22122021.DB.Database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class MyReceiver : BroadcastReceiver() {

    @SuppressLint("SimpleDateFormat")
    override fun onReceive(context: Context, intent: Intent) {

        val list = AppDatabase.getInstance(context).courseDao().getAll()

        val date = SimpleDateFormat("dd/MM/yyyy  hh:mm").format(Date().time)

        for (i in list) {
            if (i.isActive == true && i.time == date) {

                val intent2 = Intent(context, NotificationActivity::class.java)

                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

                intent2.putExtra("vibrate", i.vibrator)

                intent2.putExtra("alarm", i)

                context.startActivity(intent2)

                val notificationCompat = NotificationCompat.Builder(context, "1")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(SimpleDateFormat("hh:mm").format(Date()))
                    .setContentText(i.message)
                    .setAutoCancel(true)

                val notification = notificationCompat.build()

                val notificationManager: NotificationManager =
                    context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = "Name"
                    val descriptionText = "Description"
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel("1", name, importance).apply {
                        description = descriptionText
                    }
                    notificationManager.createNotificationChannel(channel)
                }

                notificationManager.notify(1, notification)


                break
            }
        }

    }
}