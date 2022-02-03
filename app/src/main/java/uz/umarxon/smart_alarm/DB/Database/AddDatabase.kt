package uz.umarxon.valyutaarxiv22122021.DB.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.umarxon.smart_alarm.DB.Entity.AlarmModel
import uz.umarxon.valyutaarxiv22122021.DB.Dao.CurrencyDao

@Database(entities = [AlarmModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CurrencyDao

    companion object {
        private var instanse: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {

            when (instanse) {
                null -> {
                    instanse = Room.databaseBuilder(context, AppDatabase::class.java, "alarms")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return instanse!!
        }

    }
}