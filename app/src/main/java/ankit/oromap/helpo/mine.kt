package ankit.oromap.helpo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class mine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mine)
        setSupportActionBar(findViewById(R.id.toolbar))
        if(Build.VERSION.SDK_INT>=21)
            window.statusBarColor = resources.getColor(R.color.orange)
        val list = findViewById<ListView>(R.id.list)
        val number = intent.extras!!.getInt("num")
        if(number==0)
        {
            list.visibility = View.INVISIBLE
            findViewById<TextView>(R.id.noreqs).visibility = View.VISIBLE
        }
        else
        {
            val helps = intent.extras!!.getStringArray("descriptions")!!.toList()
            val views = intent.extras!!.getStringArray("views")!!.toList()
            val adapt = adapter(this,number,helps,views)
            list.adapter = adapt
        }
    }

    override fun onBackPressed() {
       finish()
    }
}
class adapter(c: Context,num:Int,helps:List<String>,view:List<String>) : ArrayAdapter<String>(c,num)
{
    companion object
    {
        lateinit var views:List<String>
        lateinit var con:Context
        private var number = 0
        lateinit var helparray:List<String>
    }
    init {
        views = view
        helparray = helps
        con = c
        number = num
    }
    override fun getCount(): Int {
        return number
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(con).inflate(R.layout.listview,null)
        view.findViewById<TextView>(R.id.viewcount).setText(views[position])
        view.findViewById<TextView>(R.id.helpdes).setText(helparray[position])
        return view
    }
}