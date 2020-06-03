package ankit.oromap.helpo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ankit.oromap.helpo.extras.Help
import ankit.oromap.helpo.extras.feeling_low
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText

class addhelp : AppCompatActivity() {
private var username = ""
    private var password = ""
    private var name = ""
    private var lat = ""
    private var longi = ""
    private var process = false

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addhelp)
        setSupportActionBar(findViewById(R.id.toolbar))
        if(Build.VERSION.SDK_INT>=21)
            window.statusBarColor = resources.getColor(R.color.back3)
        val data = intent.extras
        val qu = Volley.newRequestQueue(this)
        username = data!!.getString("username").toString()
        password = data.getString("password").toString()
        name = data.getString("name").toString()
        lat = data.getString("lat").toString()
        longi = data.getString("long").toString()
        name = data.getString("name").toString()
        val but = findViewById<Button>(R.id.addhelpbut)
        val prog = findViewById<ProgressBar>(R.id.loginprog)
        val shrink = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.closebutton
        )
        val v2 = findViewById<TextInputEditText>(R.id.description);
       lateinit var list:List<String>
        val lay = findViewById<CoordinatorLayout>(R.id.help_container) as ViewGroup
        val loading = layoutInflater.inflate(R.layout.global_loading,lay,false)
        lay.addView(loading)
        val str = StringRequest(Request.Method.GET,"http://helpo.oromap.in/list.php", Response.Listener {
            lay.removeView(loading)
            list = it.split(',').toList()
            val recyclerView = findViewById<RecyclerView>(R.id.dialogues_container)
            val linearlay = LinearLayoutManager(applicationContext,LinearLayout.HORIZONTAL,false)
            val adapt = adaptre(applicationContext,list,v2)
            recyclerView.adapter = adapt
        }, Response.ErrorListener {
            Toast.makeText(applicationContext,"Connection Error. Restart Page",Toast.LENGTH_LONG).show()
        })
        qu.add(str)
        shrink.setAnimationListener(object :
            Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                prog.visibility = View.VISIBLE
                but.visibility = View.INVISIBLE
                val sr = object:StringRequest(Method.POST,"http://helpo.oromap.in/addhelp.php", Response.Listener {
                    if(it.equals("200"))
                    {
                        process = false
                        val ba = Intent();
                        val bun = Bundle()
                        bun.putBoolean("res",true)
                        ba.putExtras(bun)
                        setResult(2,ba)
                        finish()
                    }
                }
                , Response.ErrorListener {
                        Toast.makeText(applicationContext,"Connection Error",Toast.LENGTH_LONG).show()
                        prog.visibility = View.INVISIBLE
                        but.visibility = View.VISIBLE
                    })
                {
                    override fun getParams(): MutableMap<String, String> {
                        val hashs = HashMap<String,String>()
                        hashs.put("password",password)
                        hashs.put("username",username)
                        hashs.put("lat",lat)
                        hashs.put("long",longi)
                        hashs.put("description",v2!!.text.toString())
                        hashs.put("name",name)
                        return hashs
                    }
                }
                qu.add(sr)
                process = true
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        findViewById<TextInputEditText>(R.id.name)
                    .setText(name)
        findViewById<TextInputEditText>(R.id.coordx).setText(lat)
        findViewById<TextInputEditText>(R.id.coordy).setText(longi)
        findViewById<TextInputEditText>(R.id.name).isFocusable = false
        findViewById<TextInputEditText>(R.id.coordx).isFocusable = false
        findViewById<TextInputEditText>(R.id.coordy).isFocusable = false
        v2!!.requestFocus()
        but.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View?) {
                if (v2!!.text!!.trim().isEmpty()) {
                    v2!!.setError("Required Field")
                } else {
                    findViewById<Button>(R.id.addhelpbut)
                        .startAnimation(shrink)
                }
            }
        })
        }

    override fun onBackPressed() {
        if(process)
        {
            Toast.makeText(applicationContext,"Please Wait....We are adding your help request",Toast.LENGTH_LONG).show()
        }
        else{
            setResult(33,Intent())
            finish()
        }
    }
}
class adaptre(c:Context,list:List<String>,l:TextInputEditText): RecyclerView.Adapter<adaptre.viewholder>() {
    lateinit var con: Context;
    companion object {
        lateinit var lis: List<String>
        lateinit var v2:TextInputEditText
    }
    init {
        this.con = c
        lis = list
        v2 = l
    }


    override fun getItemCount(): Int {
        return lis.size
    }

    override fun onBindViewHolder(holder: adaptre.viewholder, position: Int) {
        holder.bind(position)
    }

    class viewholder(v: View) : RecyclerView.ViewHolder(v)
    {
        lateinit var vi:View
        init {
            vi = v
        }
        fun bind(posi:Int)
        {
            vi.findViewById<TextView>(R.id.dialog).setText(lis.get(posi))
            vi.setOnClickListener(object:View.OnClickListener {
                override fun onClick(v: View?) {
                    v2.setText(lis.get(posi))
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val li = LayoutInflater.from(con).inflate(R.layout.dialog_view,parent,false)
        return viewholder(li)
    }
}