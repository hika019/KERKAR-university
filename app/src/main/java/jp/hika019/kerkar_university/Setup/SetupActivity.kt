package jp.hika019.kerkar_university.Setup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.TAG_hoge
import jp.hika019.kerkar_university.firedb_setup
import jp.hika019.kerkar_university.url
import kotlinx.android.synthetic.main.activity_setup.*

class SetupActivity: AppCompatActivity(){
    private val TAG = "Setup_Activity" + TAG_hoge

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        /*
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.setup_host_fragment, Select_university_fragment())
        ft.commit()

         */

        new_acount.setOnClickListener {

            val intent = Intent(this, Select_university_Activity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        load_acount.setOnClickListener {
            Toast.makeText(this, "未実装機能です", Toast.LENGTH_SHORT).show()
        }

        button2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


    }


}

class setup(): firedb_setup(){
}