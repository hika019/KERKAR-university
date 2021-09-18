package jp.hika019.kerkar_university

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.hika019.kerkar_university.Message.MessageFragment
import jp.hika019.kerkar_university.Task.Task_list_Fragment
import com.google.android.material.navigation.NavigationView
import jp.hika019.kerkar_university.Home.Home_fragment
import jp.hika019.kerkar_university.Home.load_fragment
import jp.hika019.kerkar_university.test.test
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"+ TAG_hoge

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    private val flag = true

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "MainActivity")
        if (!flag)
            timetable_id.value = get_timetable_id(this)

        timetable_id.observe(this, Observer {
            Log.d(TAG, "change timetable_id")
            //授業の取得
            val firedb_tt_class = firedb_timetable_new()
            firedb_tt_class.check_user_timetable(this)
        })
        Log.d(TAG, "hogee")

        setContentView(R.layout.activity_main2)
        //this.setToolbar()
        //this.setDrawerLayout()
        this.bottom_navi_view.setOnNavigationItemSelectedListener(bottomNav)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_host_fragment, load_fragment())
        ft.commit()

        to_home_fragment.observe(this, Observer {
            if (to_home_fragment.value == true){
                val ft = supportFragmentManager.beginTransaction()
                //ft.replace(R.id.main_host_fragment, test())
                ft.replace(R.id.main_host_fragment, Home_fragment())
                ft.commit()
            }
        })

    }



    private fun setToolbar(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
    }


//    private fun setDrawerLayout(){
//        drawerLayout = findViewById(R.id.drawer_layout)
//        navView = findViewById(R.id.nav_view)
//
//        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open , R.string.nav_close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//        navView.setNavigationItemSelectedListener(this)
//    }

    private val bottomNav = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val ft = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.nav_home -> {
                ft.replace(R.id.main_host_fragment, Home_fragment())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_timetable -> {

                ft.replace(R.id.main_host_fragment, test())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_task -> {

                ft.replace(R.id.main_host_fragment, Task_list_Fragment())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_setting -> {
                ft.replace(R.id.main_host_fragment, Setting_Fragment())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
        }

        //drawer_layout.closeDrawer(GravityCompat.START)
        false
    }
}