package uz.umarxon.smart_alarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_dialog.view.*
import tyrantgit.explosionfield.ExplosionField
import uz.umarxon.smart_alarm.DB.Entity.AlarmModel
import uz.umarxon.smart_alarm.adapter.RvAdapter
import uz.umarxon.valyutaarxiv22122021.DB.Database.AppDatabase
import java.lang.Exception
import java.text.SimpleDateFormat

class HomeActivity : AppCompatActivity() {

    lateinit var rvAdapter:RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvAdapter = RvAdapter(this,object :RvAdapter.OnItemCLickListener{
            override fun onItemClick(user: AlarmModel, pos: Int) {
                startActivity(Intent(this@HomeActivity,EditActivity::class.java).putExtra("alarm",user))
            }
            override fun delete(alarm: AlarmModel, pos: Int) {/*
                val bottomSheetDialog = BottomSheetDialog(this@HomeActivity)
                val itemView = LayoutInflater.from(this@HomeActivity).inflate(R.layout.bottom_dialog,null,false)
                bottomSheetDialog.setContentView(itemView)
                itemView.delete.setOnClickListener {
                    val explosionField = ExplosionField.attach2Window(this@HomeActivity)
                    explosionField.explode(itemView.remove)
                    AppDatabase.getInstance(this@HomeActivity).courseDao().deleteAlarm(alarm)
                    bottomSheetDialog.hide()
                }
                itemView.noDelete.setOnClickListener {
                    bottomSheetDialog.hide()
                }
                itemView.name.text = "Do you want to delete this alarm time is ${alarm.time}"
                bottomSheetDialog.show()
            */}
        },this)

        val appDatabase = AppDatabase.getInstance(this)

        appDatabase.courseDao().getAllAlarm()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<AlarmModel>>,
                io.reactivex.functions.Consumer<List<AlarmModel>> {
                override fun accept(t: List<AlarmModel>) {
                    rvAdapter.submitList(t)
                }
            })

        rv.adapter = rvAdapter

        add.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }


    }

}