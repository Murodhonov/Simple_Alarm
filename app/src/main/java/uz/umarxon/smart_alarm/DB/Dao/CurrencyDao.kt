package uz.umarxon.valyutaarxiv22122021.DB.Dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import uz.umarxon.smart_alarm.DB.Entity.AlarmModel

@Dao
interface CurrencyDao {

    @Query("select * from alarmmodel where id=:id")
    fun getAlarmById(id:Int):AlarmModel

    @Query("select * from alarmmodel")
    fun getAll(): List<AlarmModel>

    @Query("select * from alarmmodel")
    fun getAllAlarm(): Flowable<List<AlarmModel>>

    @Insert
    fun addAlarm(alarmModel: AlarmModel)

    @Update
    fun updateAlarm(alarmModel: AlarmModel)

    @Delete
    fun deleteAlarm(alarmModel: AlarmModel)

}