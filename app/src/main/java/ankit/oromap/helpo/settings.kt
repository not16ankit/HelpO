package ankit.oromap.helpo

import android.os.Bundle
import android.view.View
import android.widget.Switch
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_settings.*


class settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<Switch>(R.id.switch2).setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
            }
        })
    }

}
