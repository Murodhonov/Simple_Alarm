package uz.umarxon.smart_alarm.DB.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class AlarmModel:Serializable {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    var time:String? = null
    var vibrator:Boolean? = null
    var message:String? = null
    var recordedSound:String? = null
    var isActive:Boolean? = null


    constructor()

    constructor(
        id: Int?,
        time: String,
        vibrator: Boolean?,
        message: String?,
        recordedSound: String?,
        isActive: Boolean?
    ) {
        this.id = id
        this.time = time
        this.vibrator = vibrator
        this.message = message
        this.recordedSound = recordedSound
        this.isActive = isActive
    }

    constructor(time: String,vibrator: Boolean?, message: String?, recordedSound: String?, isActive: Boolean?) {
        this.vibrator = vibrator
        this.message = message
        this.time = time
        this.recordedSound = recordedSound
        this.isActive = isActive
    }

    override fun toString(): String {
        return "AlarmModel(id=$id, time='$time', vibrator=$vibrator, message=$message, recordedSound=$recordedSound)"
    }


}