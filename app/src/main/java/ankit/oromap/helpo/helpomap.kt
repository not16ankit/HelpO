package ankit.oromap.helpo

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.ui.AppBarConfiguration
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.clans.fab.FloatingActionMenu
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_helpomap.*
import kotlinx.android.synthetic.main.app_bar_helpomap.*
import kotlinx.android.synthetic.main.content_helpomap.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class helpomap : AppCompatActivity() {
    private var time=0
    private var al:AlertDialog.Builder? = null
    private var qu:RequestQueue? = null
    private var marker:Marker? = null
    private var userloc:Location? = null
    private var new_marker_drawable:Int = R.drawable.girl8
    private var username:String = ""
    private var count:Int = 0
    private var email_sent = false
    private var drt2:DrawerLayout? = null
    private var password:String = ""
    private var lay:ViewGroup?= null
    private var jj = 0
    private var gender:Int = 0
    private var name:String = ""
    private var lay2:ViewGroup? = null
    private var viewadd:Boolean = false
    private var helps = 0
    private var view:View? = null
    private var avatar:Int = 0
    lateinit var mydescriptions:Array<String?>
    lateinit var myviews:Array<String?>
    private var loc:LocationManager? = null
    private var locationlistener:LocationListener? = null
    private var sr:StringRequest? = null
    lateinit var th:Thread
    private var bottomview:View? = null
    private var verified:String = ""
    private var d:Int = R.drawable.boy2
    private var toohandler:Handler? = null
    private var loading_screen:View? = null
    private var composi:CameraPosition? = null
    private var tooslideclose:Boolean = false
    private var toclose:Boolean = false
    private var al2:AlertDialog.Builder?= null
    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mapp:GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpomap)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(object:NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if(item.itemId==R.id.logout)
                {
                    logout()
                }
                if(item.itemId==R.id.help_reqs)
                {
                    toclose = false
                    val ig = Intent(applicationContext,mine::class.java)
                    val bun = Bundle()
                    bun.putInt("num",helps)
                    bun.putStringArray("descriptions",mydescriptions)
                    bun.putStringArray("views",myviews)
                    ig.putExtras(bun)
                    startActivity(ig)
                }
                return false
            }
        })
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom))
        bottomSheetBehavior!!.setPeekHeight(0,true)
        bottomSheetBehavior!!.isHideable = true
        bottomSheetBehavior!!.setBottomSheetCallback(object:BottomSheetBehavior.BottomSheetCallback()
        {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState==BottomSheetBehavior.STATE_COLLAPSED)
                {
                    tooslideclose = false
                }
            }
        })
        bottomview = findViewById(R.id.bottom)
        lay2 = findViewById<CoordinatorLayout>(R.id.fullcover) as ViewGroup
        view = layoutInflater.inflate(R.layout.waiting_confirmation, lay2, false)
        al = AlertDialog.Builder(this,R.style.slert)
        al2 = AlertDialog.Builder(this,R.style.slert)
        al2!!.setCancelable(false)
        al2!!.setMessage("Location Services required to find people and help calls near you.")
        al2!!.setTitle("Fatal Error")
        al2!!.setNegativeButton("Exit",object:DialogInterface.OnClickListener
        {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                finish()
            }
        })
        al2!!.setPositiveButton("Turn On",object:DialogInterface.OnClickListener
        {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val int = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                toclose=false
                dialog!!.cancel()
                dialog.dismiss()
                startActivityForResult(int,88)
            }
        })
        val data = intent.extras
        username = data!!.getString("username").toString().trim()
        password = data.getString("password").toString().trim()
        qu = Volley.newRequestQueue(this)
        drt2 = findViewById(R.id.drawer_layout)
        lay = findViewById(R.id.swipe2)
        loading_screen = layoutInflater.inflate(R.layout.loading_helps,lay,false)
        if(!File(filesDir,"gender.txt").exists()) {
            val radio = layoutInflater.inflate(R.layout.gender, lay, false)
            lay!!.addView(radio)
            radio.findViewById<RadioGroup>(R.id.radiogroup)
                .setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
                    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                        radio.findViewById<Button>(R.id.submit).isClickable = true
                        radio.findViewById<Button>(R.id.submit).visibility = View.VISIBLE
                        gender = checkedId
                    }
                })
            radio.findViewById<Button>(R.id.submit)
                .setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val f: File = File(filesDir, "gender.txt")
                        val fout: FileOutputStream = FileOutputStream(f)
                        when (gender) {
                            1 -> {
                                fout.write("1".toByteArray())
                                fout.flush()
                                fout.close()
                            }
                            2 -> {
                                fout.write("2".toByteArray())
                                fout.flush()
                                fout.close()
                            }
                            3 -> {
                                fout.write("3".toByteArray())
                                fout.flush()
                                fout.close()
                            }
                        }
                        lay!!.removeView(radio)
                        rekt()
                    }
                })
        }
        else
        {
            rekt()
        }
    }
    fun send_email()
    {
        sr = object:StringRequest(Method.POST,"http://helpo.oromap.in/sendemail.php", Response.Listener {
            if(it.equals("200"))
            {
                Toast.makeText(applicationContext,"Verification Email Sent. \n Check Spam folder too.",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(applicationContext,"Report Error",Toast.LENGTH_LONG).show()
            }
        }, Response.ErrorListener {
            Toast.makeText(applicationContext,"Error",Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val hashs = HashMap<String,String>()
                hashs.put("username",username)
                hashs.put("password",password)
                hashs.put("email",username)
                return hashs
            }
        }
        qu!!.add(sr)
    }
    fun before_rekt()
    {
        val f = File(filesDir,"gender.txt")
        val fin = DataInputStream(FileInputStream(f))
        gender = fin.readLine().toInt()
        fin.close()
        when(gender)
        {
            1->
            {
                avatar = Random().nextInt(8)+3
                when(avatar)
                {
                    3->
                    {
                        d = R.drawable.girl1
                    }
                    4->
                    {
                        d = R.drawable.girl2
                    }
                    5->
                    {
                        d = R.drawable.girl3
                    }
                    6->
                    {
                        d = R.drawable.girl4
                    }
                    7->
                    {
                        d =R.drawable.girl5
                    }
                    8->
                    {
                        d = R.drawable.girl6
                    }
                    9->
                    {
                        d = R.drawable.girl7
                    }
                    10->
                    {
                        d = R.drawable.girl8
                    }
                }
            }
            2->
            {
                avatar = Random().nextInt(3)+11
                when(avatar)
                {
                    11->
                    {
                        d = R.drawable.boy1
                    }
                    12->
                    {
                        d = R.drawable.boy2
                    }
                    13->
                    {
                        d = R.drawable.boy3
                    }
                }
            }
            3->
            {
                avatar = Random().nextInt(11)+3
                when(avatar)
                {
                    3->
                    {
                        d = R.drawable.girl1
                    }
                    4->
                    {
                        d = R.drawable.girl2
                    }
                    5->
                    {
                        d = R.drawable.girl3
                    }
                    6->
                    {
                        d = R.drawable.girl4
                    }
                    7->
                    {
                        d =R.drawable.girl5
                    }
                    8->
                    {
                        d = R.drawable.girl6
                    }
                    9->
                    {
                        d = R.drawable.girl7
                    }
                    10->
                    {
                        d = R.drawable.girl8
                    }
                    11->
                    {
                        d = R.drawable.boy1
                    }
                    12->
                    {
                        d = R.drawable.boy2
                    }
                    13->
                    {
                        d = R.drawable.boy3
                    }
                }
            }
        }
    }
    fun rekt()
    {
        before_rekt()
        sr = object:StringRequest(Method.POST,"http://helpo.oromap.in/aftersignin.php",
            Response.Listener {
                val jsonob = JSONObject(it.trim())
                name = jsonob.getString("name")
                drawer_layout.findViewById<ImageView>(R.id.avatar).setImageBitmap(BitmapFactory.decodeFile(File(filesDir,avatar.toString()+".png").absolutePath))
                drawer_layout.findViewById<TextView>(R.id.name).setText(name)
                if(jsonob.getString("verified").toInt()==1) {
                    if(viewadd)
                    {
                        lay2!!.removeView(view)
                        drt2!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    }
                    ini()
                }
                else
                {
                    if(!email_sent) {
                        send_email()
                        val log: TextView = view!!.findViewById(R.id.logouttext)
                        log.visibility = View.VISIBLE
                        log.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                logout()
                            }
                        })
                        lay2!!.addView(view)
                        viewadd = true
                        email_sent=true
                        drt2!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                    toohandler = object:Handler()
                    {
                        override fun handleMessage(msg: Message) {
                            super.handleMessage(msg)
                            rekt()
                        }
                    }
                    Thread(object:Runnable
                    {
                        override fun run() {
                            Thread.sleep(10000)
                            toohandler!!.sendEmptyMessage(0)
                        }
                    }).start()
                }
            }, Response.ErrorListener {
                Toast.makeText(applicationContext,"Connection Error",Toast.LENGTH_LONG).show()
            })
        {
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String,String>()
                hash.put("username",username)
                hash.put("password",password)
                return hash
            }
        }
        qu!!.add(sr)
    }
    fun ini()
    {
        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(object:OnMapReadyCallback {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onMapReady(p0: GoogleMap?) {
                mapp = p0
                p0!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext,R.raw.green))
                loc()
            }
        })
    }
    fun loc()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf<String>(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 100
                )
            }
        }
        loc = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val k = Intent(this,addhelp::class.java)
        val moveout = AnimationUtils.loadAnimation(applicationContext,R.anim.move_out_lay)
        locationlistener = object:LocationListener
        {
            override fun onLocationChanged(location: Location?) {
                val latlng = LatLng(location!!.latitude, location!!.longitude)
                userloc = location
                composi =
                    CameraPosition.builder().target(latlng).zoom(17.toFloat()).build()
                if(time==0)
                {
                    moveout.setAnimationListener(object:Animation.AnimationListener
                    {
                        override fun onAnimationEnd(animation: Animation?) {
                            tobegone.visibility = View.INVISIBLE
                            findViewById<FragmentContainerView>(R.id.map).visibility = View.VISIBLE
                            fab.visibility = View.VISIBLE
                            composi =
                                CameraPosition.builder().target(LatLng(userloc!!.latitude,userloc!!.longitude)).zoom(17.toFloat()).build()
                            floating.setOnMenuToggleListener(object :
                                FloatingActionMenu.OnMenuToggleListener {
                                override fun onMenuToggle(opened: Boolean) {
                                    if (opened) {
                                        floating_back.visibility = View.VISIBLE
                                    } else {
                                        floating_back.visibility = View.INVISIBLE
                                    }
                                }
                            })
                            locationfab.setOnClickListener(object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    floating.toggle(true)
                                    mapp!!.moveCamera(
                                        CameraUpdateFactory.newCameraPosition(
                                            composi
                                        )
                                    )
                                }
                            })
                            askhelpfab.setOnClickListener(object : View.OnClickListener {
                                override fun onClick(v: View?) {
                                    floating.toggle(true)
                                    toclose = false
                                    val bun = Bundle()
                                    bun.putString("name", name)
                                    bun.putString("lat", userloc!!.latitude.toString())
                                    bun.putString("long", userloc!!.longitude.toString())
                                    bun.putString("username", username)
                                    bun.putString("password", password)
                                    bun.putString("name",name)
                                    k.putExtras(bun)
                                    startActivityForResult(k, 78)
                                }
                            })
                            refreshfab.setOnClickListener(object:View.OnClickListener
                            {
                                override fun onClick(v: View?) {
                                    floating.toggle(true)
                                    mapp!!.clear()
                                    loc!!.removeUpdates(locationlistener)
                                    time = 0
                                    if (ActivityCompat.checkSelfPermission(
                                            applicationContext,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                            applicationContext,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return
                                    }
                                    loc!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0.toLong(),20.toFloat(),locationlistener)
                                    gethelps()
                                }
                            })
                            marker = mapp!!.addMarker(
                                MarkerOptions().position(LatLng(userloc!!.latitude,userloc!!.longitude)).title("This is you")
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarker())))
                            marker!!.tag = 78070
                            mapp!!.moveCamera(CameraUpdateFactory.newCameraPosition(composi))
                            time=1
                            lay!!.addView(loading_screen)
                            gethelps()
                        }

                        override fun onAnimationRepeat(animation: Animation?) {

                        }

                        override fun onAnimationStart(animation: Animation?) {

                        }
                    })
                    tobegone.startAnimation(moveout)
                }
                else{
                    marker!!.position = LatLng(location!!.latitude, location!!.longitude)
                }
            }

            override fun onProviderDisabled(provider: String?) {
               start()
            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                TODO("Not yet implemented")
            }
        }
        start()
    }
    fun start()
    {
        if(loc!!.isProviderEnabled(LocationManager.GPS_PROVIDER) && loc!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf<String>(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 100
                    )
                }
            }
            loc!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0.toLong(),
                10.toFloat(),locationlistener)
            jj = 99
        }
        else
        {
            if(jj==99)
                loc!!.removeUpdates(locationlistener)
            al2!!.show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==78)
        {
            if(resultCode==2)
            {
                val d = data!!.extras
                if(d!!.getBoolean("res"))
                {
                    al!!.setTitle("Congrats")
                    al!!.setMessage("Your help request has been added successfully. The request will be auto-deleted after 1 hour.")
                    al!!.setCancelable(false)
                    al!!.setPositiveButton("OK",object:DialogInterface.OnClickListener
                    {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.cancel()
                            dialog.dismiss()
                            mapp!!.clear()
                            loc!!.removeUpdates(locationlistener)
                            time = 0
                            if (ActivityCompat.checkSelfPermission(
                                    applicationContext,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    applicationContext,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return
                            }
                            loc!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0.toLong(),20.toFloat(),locationlistener)
                            gethelps()
                        }
                    })
                    al!!.show()
                }
            }
        }
        if(requestCode==88)
        {
            start()
        }
    }
fun getMarker():Bitmap
{
    val customMarker = layoutInflater.inflate(R.layout.marker,null)
    val im = customMarker.findViewById<ImageView>(R.id.avatar)
    im.setImageResource(d)
    customMarker.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
    customMarker.layout(0,0,customMarker.measuredWidth,customMarker.measuredHeight)
    customMarker.buildDrawingCache()
    val returnedbit = Bitmap.createBitmap(customMarker.measuredWidth,customMarker.measuredHeight,Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedbit)
    canvas.drawColor(Color.WHITE,PorterDuff.Mode.SRC_IN)
    val drawable = customMarker.background
    if(drawable != null)
        drawable.draw(canvas)
    customMarker.draw(canvas)
    return returnedbit
}
    fun gethelps()
    {
        val sr = object:StringRequest(Request.Method.POST,"http://helpo.oromap.in/gethelps.php", Response.Listener {
          setmarkers(it.trim())
        }, Response.ErrorListener {
            Toast.makeText(applicationContext,"Error",Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String,String>()
                hash.put("username",username)
                hash.put("password",password)
                return hash
            }
        }
        qu!!.add(sr)
    }
    fun logout()
    {
        val f1 = File(filesDir,"username.txt")
        val f2 = File(filesDir,"password.txt")
        val f3 = File(filesDir,"gender.txt")
        f1.delete()
        f2.delete()
        f3.delete()
        val i = Intent(this,cat::class.java)
        toclose=true
        startActivity(i)
    }

    override fun onPause() {
        super.onPause()
        if(toclose)
        {
            finish()
        }
    }
    fun setmarkers(json:String)
    {
        val ob = JSONObject(json)
        count = ob.getString("helpcount").toInt()
       val locations = arrayOfNulls<Location>(count)
       val  usernames = arrayOfNulls<String>(count)
       val  description = arrayOfNulls<String>(count)
       val distances = arrayOfNulls<String>(count)
        val names = arrayOfNulls<String>(count)
        mydescriptions = arrayOfNulls<String>(count)
        myviews = arrayOfNulls<String>(count)
        var i = 1
        var u = 0
        helps= 0
        var loc = Location(LocationManager.GPS_PROVIDER)
        while(i<=count)
        {
            if(!username.equals(ob.getString("help"+(i)+"user").trim(),true)) {
                loc.latitude = ob.getString("help" + (i) + "lat").toDouble()
                loc.longitude = ob.getString("help" + (i) + "long").toDouble()
                if (loc.distanceTo(userloc) < 1000) {
                    names[u] = ob.getString("help" + (i) + "name")
                    distances[u] = loc.distanceTo(userloc).toString()
                    locations[u] = loc
                    usernames[u] = ob.getString("help" + (i) + "user")
                    description[u] = ob.getString("help" + (i) + "des")
                    get_random_view()
                    mapp!!.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.fromBitmap(
                                getMarker()
                            )
                        ).position(LatLng(loc.latitude, loc.longitude))
                    ).tag = u
                    u++
                }
            }
            else{
                mydescriptions[helps] = ob.getString("help" + (i) + "des")
                myviews[helps] = ob.getString("help"+(i)+"view")
                helps++
            }
            i++
        }
        lay!!.removeView(loading_screen)
        mapp!!.setOnMarkerClickListener(object:GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                val t =p0!!.tag.toString().toInt();
                set_details(t,description,distances,names,usernames)
                return false
            }
        })
    }
    fun get_random_view()
    {
        val avatar2 = Random().nextInt(11)+3
        when(avatar2)
        {
            3->
            {
                d = R.drawable.girl1
            }
            4->
            {
                d = R.drawable.girl2
            }
            5->
            {
                d = R.drawable.girl3
            }
            6->
            {
                d = R.drawable.girl4
            }
            7->
            {
                d =R.drawable.girl5
            }
            8->
            {
                d = R.drawable.girl6
            }
            9->
            {
                d = R.drawable.girl7
            }
            10->
            {
                d = R.drawable.girl8
            }
            11->
            {
                d = R.drawable.boy1
            }
            12->
            {
                d = R.drawable.boy2
            }
            13->
            {
                d = R.drawable.boy3
            }
        }
    }
    fun set_details(i:Int,description:Array<String?>,distances:Array<String?>,names:Array<String?>,usernames:Array<String?>)
    {
        if(!(i==78070)) {
            val ai = AlertDialog.Builder(this,R.style.slert)
            ai.setCancelable(false)
            ai.setTitle("Contact Details")
            ai.setMessage("You can reach "+names[i]+ " at "+usernames[i])
            ai.setNegativeButton("OK",object:DialogInterface.OnClickListener
            {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.cancel()
                    dialog.cancel()
                }
            })
            addview(usernames[i].toString(),description[i].toString())
            bottomview!!.findViewById<TextView>(R.id.bottomname).setText(names[i])
            bottomview!!.findViewById<TextView>(R.id.bottomdescription).setText(description[i])
            bottomview!!.findViewById<TextView>(R.id.bottomdistance).setText(distances[i]+" m")
            bottomview!!.findViewById<Button>(R.id.help_but).setOnClickListener(object:View.OnClickListener{
                override fun onClick(v: View?) {
                    ai.show()
                }
            })
            if (!tooslideclose) {
                tooslideclose = true
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
fun addview(usern:String,desc:String)
{
    val st = object :
        StringRequest(Request.Method.POST, "http://helpo.oromap.in/addview.php",
            Response.Listener {

            }, Response.ErrorListener { }) {
        override fun getParams(): MutableMap<String, String> {
            val hash = HashMap<String, String>()
            hash.put("username", usern.toString().trim())
            hash.put("description", desc.toString().trim())
            return hash
        }
    }
    qu!!.add(st)
}
    override fun onBackPressed() {
        if(tooslideclose)
        {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            tooslideclose = false
        }else
        {
            super.onBackPressed()
        }
    }
}