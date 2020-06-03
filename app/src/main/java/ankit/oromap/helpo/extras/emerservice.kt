package ankit.oromap.helpo.extras

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast
import java.util.*

class emerservice : Service() {
    override fun onCreate() {
        super.onCreate()
        if(isRun(emerservice::class.java))
        {
            Toast.makeText(applicationContext,"1 already running", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(applicationContext,"Ruunning 1",Toast.LENGTH_LONG).show()
        }
        val pend = PendingIntent.getService(this,9,Intent(this,emerservice::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(AlarmManager.RTC,System.currentTimeMillis(),60000,pend)
    }
    fun isRun(serv:Class<*>?):Boolean
    {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for(service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(serv!!.name.equals(service.service.className))
            {
                return true
            }
        }
        return false
    }
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
