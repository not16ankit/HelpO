package ankit.oromap.helpo.extras

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import org.xml.sax.ContentHandler
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class geticons(h:Handler,c:Context) : AsyncTask<Object,String,String>() {
    lateinit var handler:Handler
    public var biryaniInterface:biryani_interface? = null
    lateinit var con:Context
    init {
        handler = h
        con = c
    }
    override fun doInBackground(vararg params: Object?): String {
        try {
            lateinit var ucon:HttpURLConnection
            lateinit var u:URL
            lateinit var filewriter:FileOutputStream
            lateinit var inp:InputStream
            var i = 1
            while(i<=13)
            {
                var b = ByteArray(1024)
                var l = 0
                u = URL("http://helpo.oromap.in/resources/icons/"+i+".png")
                ucon = u.openConnection() as HttpURLConnection
                ucon.addRequestProperty("Referer","http://helpo.oromap.in")
                inp = ucon.inputStream
                filewriter = FileOutputStream(File(con.filesDir,i.toString()+".png"))
                while(inp.read(b).also { l = it } > -1)
                {
                    filewriter.write(b,0,l)
                }
                filewriter.flush()
                filewriter.close()
                i++
            }
            biryaniInterface!!.biryani()
        }
        catch (e:Exception){
            val mess = Message()
            val bun = Bundle()
            bun.putString("error","internet23")
            mess.data = bun
            handler.sendMessage(mess)
        }
        return ""
    }
}