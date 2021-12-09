package jp.hika019.kerkar_university

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.hika019.kerkar_university.task.TaskListFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ListenerRegistration
import jp.hika019.kerkar_university.home.HomeFragment
import jp.hika019.kerkar_university.home.LoadFragment
import jp.hika019.kerkar_university.test.TimetableFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"+ tagHoge

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    private val flag = true

    var hoge: ListenerRegistration? = null

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login_flag = false

        Log.d(TAG, "MainActivity")
        if (!flag)
            timetable_id.value = getTimetableId(this)

        timetable_id.observe(this, Observer {
            Log.d(TAG, "change timetable_id")
            val firedb_tt_class = FiredbTimetableNew()
            //授業の取得
            hoge?.remove()
            if (timetable_id.value != null){
                hoge =firedb_tt_class.getUserTimetableAllData(this)
            }else{
                firedb_tt_class.checkUserTimetable(this)
            }
        })

        setContentView(R.layout.activity_main)
        //this.setToolbar()
        //this.setDrawerLayout()
        this.bottom_navi_view.setOnNavigationItemSelectedListener(bottomNav)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_host_fragment, LoadFragment())
        ft.commit()

        toHomeFragment.observe(this, Observer {
            if (toHomeFragment.value == true){
                val ft = supportFragmentManager.beginTransaction()
                //ft.replace(R.id.main_host_fragment, test())
                ft.replace(R.id.main_host_fragment, HomeFragment())
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
                ft.replace(R.id.main_host_fragment, HomeFragment())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_timetable -> {

                ft.replace(R.id.main_host_fragment, TimetableFragment())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_task -> {

                ft.replace(R.id.main_host_fragment, TaskListFragment())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_setting -> {
                ft.replace(R.id.main_host_fragment, SettingFragment())
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
        }

        //drawer_layout.closeDrawer(GravityCompat.START)
        false
    }
}