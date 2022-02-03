package uz.umarxon.smart_alarm.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bottom_dialog.view.*
import tyrantgit.explosionfield.ExplosionField
import uz.umarxon.smart_alarm.DB.Entity.AlarmModel
import uz.umarxon.smart_alarm.databinding.ItemRvBinding
import uz.umarxon.valyutaarxiv22122021.DB.Database.AppDatabase

class RvAdapter(var context: Context,var clicking:OnItemCLickListener,var activit: Activity): ListAdapter<AlarmModel, RvAdapter.Vh>(MyDiffUtil()) {

    inner class Vh(var itemRv: ItemRvBinding): RecyclerView.ViewHolder(itemRv.root){

        fun onBind(alarm: AlarmModel,pos:Int){
            itemRv.timeText.text = alarm.time

            itemRv.switchCompat.isChecked = alarm.isActive!!

            itemRv.switchCompat.setOnCheckedChangeListener { compoundButton, b ->
                val appDatabase = AppDatabase.getInstance(context)

                val currentAlarm = appDatabase.courseDao().getAlarmById(alarm.id!!)

                currentAlarm.vibrator = b
                Toast.makeText(context, b.toString(), Toast.LENGTH_SHORT).show()
            }

            itemRv.card.setOnLongClickListener {
                //clicking.delete(alarm,pos)
                val explosionField = ExplosionField.attach2Window(activit)
                explosionField.explode(it)
                true
            }
            itemRv.card.setOnClickListener {
                clicking.onItemClick(alarm,pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position),position)
    }

    class MyDiffUtil: DiffUtil.ItemCallback<AlarmModel>(){
        override fun areItemsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
            return oldItem.equals(newItem)
        }

    }

    interface OnItemCLickListener{
        fun onItemClick(user: AlarmModel,pos:Int)
        fun delete(alarm: AlarmModel,pos:Int)
    }

}