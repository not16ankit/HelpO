package ankit.oromap.helpo

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import ankit.oromap.helpo.extras.biryani_interface
import ankit.oromap.helpo.extras.geticons
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.intro_meme.*
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.util.concurrent.TimeUnit

class cat : AppCompatActivity() {
    private var toclose:Boolean = false
    private var optionsview:View? = null
    private var snack:Snackbar? = null
    private var entryview:View? = null
    private var waiting_email:View? = null
    lateinit var key:String
    private var pd:ProgressDialog? = null
    private var code:String = ""
    private var phone:String = ""
    private var username:String = ""
    private var reqcode = 100
    private var password:String = ""
    private var moveinanim:Animation? = null
    private var moveoutanim:Animation? = null
    lateinit var auth:FirebaseAuth
    private var gsoclient : GoogleSignInClient? = null
    private var gso:GoogleSignInOptions? = null
    private var al:AlertDialog.Builder? = null
    private var email:String = ""
    private var waiting_phone:View? = null
    private var lay: ViewGroup? = null
    private var fullphone:String = ""
    private var loading_view:View? = null
    private var name:String = ""
    private var internet:Handler? = null
    private var queue:RequestQueue? = null
    private var pager:ViewPager? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        if(Build.VERSION.SDK_INT>=21)
            window.statusBarColor = resources.getColor(R.color.back3)
        initialize()
       }
    fun she_purs_for_me()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf<String>(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 22
                )
            }
        }
    }
    fun initialize()
    {
        pd = ProgressDialog(this,R.style.slert2)
        pd!!.setCancelable(false)
        pd!!.setTitle("Please Wait......")
        pd!!.setMessage("Getting google credentials")
        loading_view = layoutInflater.inflate(R.layout.global_loading,lay,false)
        al = AlertDialog.Builder(this,R.style.slert)
        queue = Volley.newRequestQueue(this)
        auth = FirebaseAuth.getInstance()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build()
        gsoclient = GoogleSignIn.getClient(this,gso!!)
        lay = findViewById(R.id.container23) as ViewGroup
        val intro = layoutInflater.inflate(R.layout.intro_meme,lay,false)
        entryview = layoutInflater.inflate(R.layout.communte_for,lay,false)
        optionsview = layoutInflater.inflate(R.layout.communism,lay,false)
        pager = findViewById(R.id.pager)
        lay!!.addView(intro)
        waiting_email = layoutInflater.inflate(R.layout.waiting_confirmation,lay,false)
        waiting_phone = layoutInflater.inflate(R.layout.waiting_phone_confirmation,lay,false)
        val animformove = AnimationUtils.loadAnimation(this,R.anim.trans)
        val animforindishow = AnimationUtils.loadAnimation(this,R.anim.move_in)
        val animforindiremove = AnimationUtils.loadAnimation(this,R.anim.move_out)
        moveinanim = AnimationUtils.loadAnimation(this,R.anim.move_in_lay)
        moveoutanim = AnimationUtils.loadAnimation(this,R.anim.move_out_lay)
        val av = intro.findViewById<AVLoadingIndicatorView>(R.id.indictor)
        val logo = intro.findViewById<FrameLayout>(R.id.shiftvisi)
        val animforlogoshow = AnimationUtils.loadAnimation(this,R.anim.appearanim)
        internet = object :Handler()
        {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Toast.makeText(applicationContext,"Connection Error. Restart App.",Toast.LENGTH_LONG).show()
            }
        }
        animforindishow.setAnimationListener(object :Animation.AnimationListener
        {
            override fun onAnimationEnd(animation: Animation?) {
                indictor.visibility = View.VISIBLE
                val ob = geticons(internet as Handler,applicationContext)
                ob.biryaniInterface = object:biryani_interface
                {
                    override fun biryani() {
                        av.startAnimation(animforindiremove)
                    }
                }
                ob.execute()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        animforindiremove.setAnimationListener(object :Animation.AnimationListener
        {
            override fun onAnimationEnd(animation: Animation?) {
               av.visibility = View.INVISIBLE
                logo.startAnimation(animformove)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        animformove.setAnimationListener(object:Animation.AnimationListener
        {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                lay!!.removeView(intro)
                make_pager()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        val i  = Intent(this,helpomap::class.java)
        animforlogoshow.setAnimationListener(object : Animation.AnimationListener
        {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                if(File(filesDir,"username.txt").exists())
                {
                    val f1 = File(filesDir,"username.txt")
                    val f2 = File(filesDir,"password.txt")
                    var fin = DataInputStream(FileInputStream(f1))
                    i.putExtra("username",fin.readLine().trim())
                    fin.close()
                    fin = DataInputStream(FileInputStream(f2))
                    i.putExtra("password",fin.readLine().trim())
                    fin.close()
                    toclose=true
                    startActivity(i)
                }
                else{
                    av.startAnimation(animforindishow)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
                
            }
        })
        logo.startAnimation(animforlogoshow)
        googleclick.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                google_signin()
               pd!!.show()
            }
        })
    }
    fun make_pager()
    {
        val pager_tasks = object : Handler()
        {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                username = msg.data.getString("username").toString()
                password = msg.data.getString("password").toString()
                val code = msg.data.getInt("code")
                var view = pager!!.getChildAt(0)
                if(code==0)
                    view = pager!!.getChildAt(0)
                else if(code==1)
                    view = pager!!.getChildAt(1)
                val but = view.findViewById<Button>(R.id.loginbut)
                val prog = view.findViewById<ProgressBar>(R.id.loginprog)
                val anim = AnimationUtils.loadAnimation(applicationContext,R.anim.closebutton)
                anim.setAnimationListener(object:Animation.AnimationListener
                {
                    override fun onAnimationEnd(animation: Animation?) {
                        prog.visibility = View.VISIBLE
                        but.visibility = View.INVISIBLE
                        but.isClickable = false
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                but.startAnimation(anim)
                if(code==0)
                {
                    val sr = object:StringRequest(Method.POST,"http://helpo.oromap.in/signin.php",
                        Response.Listener {
                            if(it.equals("200"))
                            {
                                signin()
                            }
                            else
                            {
                                al!!.setCancelable(false)
                                al!!.setTitle("Error")
                                al!!.setMessage("Username and passwords do not match")
                                al!!.setPositiveButton("OK",object:DialogInterface.OnClickListener
                                {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        dialog!!.cancel()
                                        dialog!!.dismiss()
                                        prog.visibility = View.INVISIBLE
                                        but.visibility = View.VISIBLE
                                        but.isClickable = true
                                    }
                                })
                                al!!.show()
                            }
                        }, Response.ErrorListener {
                            prog.visibility = View.INVISIBLE
                            but.visibility = View.VISIBLE
                            but.isClickable = true
                            internet!!.sendEmptyMessage(0)
                        })
                    {
                        override fun getParams(): MutableMap<String, String> {
                            val hash = HashMap<String,String>()
                            hash.put("username",username)
                            hash.put("type","0")
                            hash.put("password",password)
                            return hash
                        }
                    }
                    queue!!.add(sr)
                }
                else if(code==1)
                {
                    name=username
                moveinanim!!.setAnimationListener(object:Animation.AnimationListener
                {
                    override fun onAnimationEnd(animation: Animation?) {
                        optionsview!!.findViewById<LinearLayout>(R.id.email_click).setOnClickListener(object : View.OnClickListener
                        {
                            override fun onClick(v: View?) {
                                entry(1)
                            }
                        })
                        optionsview!!.findViewById<LinearLayout>(R.id.phone_click).setOnClickListener(object : View.OnClickListener
                        {
                            override fun onClick(v: View?) {
                                entry(2)
                            }
                        })
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                lay!!.addView(optionsview)
                optionsview!!.findViewById<ImageView>(R.id.emailimage).setImageBitmap(BitmapFactory.decodeFile(File(filesDir,"1.png").absolutePath))
                optionsview!!.findViewById<ImageView>(R.id.phoneimage).setImageBitmap(BitmapFactory.decodeFile(File(filesDir,"2.png").absolutePath))
                optionsview!!.startAnimation(moveinanim)
            }
            }
        }
        val adap = ankit.oromap.helpo.extras.adapter(supportFragmentManager,pager_tasks)
        pager!!.adapter = adap
        she_purs_for_me()
    }
    fun entry(i:Int)
    {
        moveoutanim!!.setAnimationListener(object:Animation.AnimationListener
        {
            override fun onAnimationEnd(animation: Animation?) {
                lay!!.removeView(optionsview)
                lay!!.addView(entryview)
                entryview!!.startAnimation(moveinanim)
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        val phoneet = entryview!!.findViewById<TextInputEditText>(R.id.numberforconfirm)
        val codeet = entryview!!.findViewById<EditText>(R.id.codeforconfirm)
        val emailet = entryview!!.findViewById<TextInputEditText>(R.id.emailforconfirm)
        entryview!!.findViewById<Button>(R.id.verifybut).setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(v: View?) {
                when(i)
                {
                    1->
                    {
                        email = emailet.text!!.trim().toString()
                        if(email.isEmpty())
                        {
                            emailet.setError("Required Field")
                        }
                        else
                        {
                            username = email
                            lay!!.removeView(entryview)
                            check(email,2)
                        }
                    }
                    2->
                    {
                        code = codeet.text.trim().toString()
                        phone = phoneet.text!!.trim().toString()
                        if(code.isEmpty())
                        {
                            codeet.setError("Required Field")
                        }
                        else{
                            if(phone.isEmpty())
                            {
                                phoneet.setError("Required Field")
                            }
                            else
                            {
                                check(phone,1)
                            }
                        }
                    }
                }
            }
        })
        if(i==1)
        {
            moveinanim!!.setAnimationListener(object :Animation.AnimationListener
            {
                override fun onAnimationEnd(animation: Animation?) {
                    entryview!!.findViewById<LinearLayout>(R.id.emaillay).visibility = View.VISIBLE
                    entryview!!.findViewById<LinearLayout>(R.id.numlay).visibility = View.INVISIBLE
                    phoneet.isFocusable = false
                    codeet.isFocusable = false
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {

                }
            })
        }
        else if(i==2)
        {
            moveinanim!!.setAnimationListener(object :Animation.AnimationListener
            {
                override fun onAnimationEnd(animation: Animation?) {
                    entryview!!.findViewById<LinearLayout>(R.id.numlay).visibility = View.VISIBLE
                    entryview!!.findViewById<LinearLayout>(R.id.emaillay).visibility = View.INVISIBLE
                    emailet.isFocusable = false
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {

                }
            })
        }
        optionsview!!.startAnimation(moveoutanim)
    }
    fun phone_verify(code:String,phone:String)
    {
        fullphone=phone;
        val codeet1 = waiting_phone!!.findViewById<EditText>(R.id.code1)
        val codeet2 = waiting_phone!!.findViewById<EditText>(R.id.code2)
        val codeet3 = waiting_phone!!.findViewById<EditText>(R.id.code3)
        val codeet4 = waiting_phone!!.findViewById<EditText>(R.id.code4)
        val codeet5 = waiting_phone!!.findViewById<EditText>(R.id.code5)
        val codeet6 = waiting_phone!!.findViewById<EditText>(R.id.code6)
        moveinanim!!.setAnimationListener(object:Animation.AnimationListener
        {
            override fun onAnimationEnd(animation: Animation?) {
                codeet1.addTextChangedListener(object:TextWatcher
                {
                    override fun afterTextChanged(s: Editable?) {
                        if(!codeet1.text.trim().isEmpty()) {
                            codeet2.requestFocus()
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        
                    }

                })
                codeet2.addTextChangedListener(object:TextWatcher
                {
                    override fun afterTextChanged(s: Editable?) {
                        if(!codeet2.text.trim().isEmpty()) {
                            codeet3.requestFocus()
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        
                    }

                })
                codeet3.addTextChangedListener(object:TextWatcher
                {
                    override fun afterTextChanged(s: Editable?) {
                        if(!codeet3.text.trim().isEmpty()) {
                            codeet4.requestFocus()
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        
                    }

                })
                codeet4.addTextChangedListener(object:TextWatcher
                {
                    override fun afterTextChanged(s: Editable?) {
                        if(!codeet4.text.trim().isEmpty()) {
                            codeet5.requestFocus()
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        
                    }

                })
                codeet5.addTextChangedListener(object:TextWatcher
                {
                    override fun afterTextChanged(s: Editable?) {
                        if(!codeet5.text.trim().isEmpty()) {
                            codeet6.requestFocus()
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        
                    }

                })
                codeet6.addTextChangedListener(object:TextWatcher
                {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        
                    }

                })
                waiting_phone!!.findViewById<Button>(R.id.codeenter).setOnClickListener(object:View.OnClickListener
                {
                    override fun onClick(v: View?) {
                        if(!codeet1.text.trim().isEmpty()&&!codeet2.text.trim().isEmpty()&&!codeet3.text.trim().isEmpty()&&!codeet4.text.trim().isEmpty()&&!codeet5.text.trim().isEmpty()&&!codeet6.text.trim().isEmpty())
                            manual(codeet1.text.trim().toString()+codeet2.text.trim()+codeet3.text.trim()+codeet4.text.trim()+codeet5.text.trim()+codeet6.text.trim())
                    }
                })
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        moveoutanim!!.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                lay!!.removeView(entryview)
                lay!!.addView(waiting_phone)
                waiting_phone!!.startAnimation(moveinanim)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        PhoneAuthProvider.getInstance().verifyPhoneNumber(code+phone,30,TimeUnit.SECONDS,this,object:PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val code3 = p0.smsCode
                var i = 0
                while(i<code3!!.length-1)
                {
                    when(i)
                    {
                        0->
                        {
                            codeet1.setText(code3[i].toString())
                        }
                        1->
                        {
                            codeet2.setText(code3[i].toString())
                        }
                        2->
                        {
                            codeet3.setText(code3[i].toString())
                        }
                        3->
                        {
                            codeet4.setText(code3[i].toString())
                        }
                        4->
                        {
                            codeet5.setText(code3[i].toString())
                        }
                        5->
                        {
                            codeet6.setText(code3[i].toString())
                        }
                    }
                    i++
                }
                lay!!.removeView(waiting_phone)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                key = p0
                entryview!!.startAnimation(moveoutanim)
            }
        })
    }
    fun check(query:String,code:Int){
        username = query
        val lanim = AnimationUtils.loadAnimation(this,R.anim.closebutton)
        lanim.setAnimationListener(object:Animation.AnimationListener
        {
            override fun onAnimationEnd(animation: Animation?) {
                entryview!!.findViewById<ProgressBar>(R.id.loginprog).visibility = View.VISIBLE
                entryview!!.findViewById<Button>(R.id.verifybut).isClickable = false
                entryview!!.findViewById<Button>(R.id.verifybut).visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        entryview!!.findViewById<Button>(R.id.verifybut).startAnimation(lanim)
        val sr = object:StringRequest(Method.POST,"http://helpo.oromap.in/check.php", Response.Listener {
            if(it.equals("yes"))
            {
                if(code==1)
                {
                    val localcode = this.code
                    al!!.setCancelable(false)
                    al!!.setTitle("Confirm")
                    al!!.setMessage("Send verification code to "+this.code+phone)
                    al!!.setNegativeButton("NO",object:DialogInterface.OnClickListener
                    {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            dialog.dismiss()
                            entryview!!.findViewById<ProgressBar>(R.id.loginprog).visibility = View.INVISIBLE
                            entryview!!.findViewById<Button>(R.id.verifybut).isClickable = true
                            entryview!!.findViewById<Button>(R.id.verifybut).visibility = View.VISIBLE
                        }
                    })
                    al!!.setPositiveButton("YES",object:DialogInterface.OnClickListener
                    {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            dialog.dismiss()
                            phone_verify(localcode,phone)
                        }
                    })
                    al!!.show()
                }
                else if(code==2)
                {
                    signup(username,"0",password,name)
                }
            }
            else if(it.equals("no")){
                entryview!!.findViewById<ProgressBar>(R.id.loginprog).visibility = View.INVISIBLE
                entryview!!.findViewById<Button>(R.id.verifybut).isClickable = true
                entryview!!.findViewById<Button>(R.id.verifybut).visibility = View.VISIBLE
                if(code==1)
                {
                    val phoneet = entryview!!.findViewById<TextInputEditText>(R.id.numberforconfirm)
                    phoneet.setError("Phone already in use")
                }
                else if(code==2)
                {
                    val emailet = entryview!!.findViewById<TextInputEditText>(R.id.emailforconfirm)
                    emailet.setError("Email already in use")
                }
            }
        }, Response.ErrorListener {
            
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String,String>()
                hash.put("query",query)
                return hash
            }
        }
        queue!!.add(sr)
    }
    fun manual(code:String)
    {
        val creds = PhoneAuthProvider.getCredential(key,code)
        auth.signInWithCredential(creds).addOnCompleteListener {
            it.addOnSuccessListener(OnSuccessListener {
                lay!!.removeView(waiting_phone)
                Toast.makeText(applicationContext,"Code Verified",Toast.LENGTH_LONG).show()
                signup(fullphone,"1",password,name)
            })
            it.addOnFailureListener(OnFailureListener {

            })
        }
    }
    fun signin()
    {
        toclose = true
        val intent = Intent(this,helpomap::class.java)
        val bun = Bundle()
        bun.putString("username",username)
        bun.putString("password",password)
        intent.putExtras(bun)
        val f1 = File(filesDir,"username.txt")
        val f2 = File(filesDir,"password.txt")
        var fout = FileOutputStream(f1)
        fout.write(username.toByteArray())
        fout.flush()
        fout.close()
        fout = FileOutputStream(f2)
        fout.write(password.toByteArray())
        fout.flush()
        fout.close()
        startActivity(intent)
    }
    fun google_signin()
    {
        val intent = gsoclient!!.signInIntent
        toclose = false
        startActivityForResult(intent,98)
    }
fun signup(username:String,verified:String,password:String,name:String)
{
val sr = object:StringRequest(Method.POST,"http://helpo.oromap.in/signup.php", Response.Listener {
    if(it.equals("200"))
    {
        Toast.makeText(applicationContext,it,Toast.LENGTH_LONG).show()
        signin()
    }
}, Response.ErrorListener {
    internet!!.sendEmptyMessage(0)
})
{
    override fun getParams(): MutableMap<String, String> {
        val hash = HashMap<String,String>()
        hash.put("username",username)
        hash.put("verified",verified)
        hash.put("password",password)
        hash.put("name",name)
        return hash
    }
}
    queue!!.add(sr)
}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==22)
        {
            if(ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED) {
                val al = AlertDialog.Builder(this, R.style.slert)
                al.setCancelable(false)
                al.setMessage("Location Permission are required while you use the app.")
                al.setTitle("Request")
                al.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog!!.cancel()
                        dialog.dismiss()
                        she_purs_for_me()
                    }
                })
                al.show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==98)
        {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnCompleteListener(object:OnCompleteListener<GoogleSignInAccount>
            {
                override fun onComplete(p0: Task<GoogleSignInAccount>) {
                    pd!!.cancel()
                    pd!!.dismiss()
                }
            })
            task.addOnSuccessListener(object:OnSuccessListener<GoogleSignInAccount>{
                override fun onSuccess(p0: GoogleSignInAccount?) {
                    val acc = p0;
                    if(!acc!!.email!!.isEmpty())
                    {
                        val ob2 = ankit.oromap.helpo.extras.extrafuncs()
                        name = acc!!.displayName.toString()
                        username = acc!!.email.toString()
                        password = ob2.twist(acc!!.id.toString())
                        val sr = object:StringRequest(Method.POST,"http://helpo.oromap.in/signin.php",
                            Response.Listener {
                                if(it.equals("200"))
                                {
                                    signin()
                                }
                                else
                                {
                                    Toast.makeText(applicationContext,it,Toast.LENGTH_LONG).show()
                                }
                            }, Response.ErrorListener {
                                pd!!.cancel()
                                pd!!.dismiss()
                                internet!!.sendEmptyMessage(0)
                            })
                        {
                            override fun getParams(): MutableMap<String, String> {
                                val hash = HashMap<String,String>()
                                hash.put("username",username)
                                hash.put("verified","1")
                                hash.put("password",password)
                                hash.put("name",name)
                                hash.put("type","1")
                                return hash
                            }
                        }
                        queue!!.add(sr)
                    }
                }
            })
        }
    }
    override fun onPause() {
        super.onPause()
        if(toclose)
        {
            finish()
        }
    }
}
