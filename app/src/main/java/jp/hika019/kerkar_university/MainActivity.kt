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
import jp.hika019.kerkar_university.test.test
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //cheack_timetable(this)
        val firedb_tt_class = firedb_timetable_new()
        firedb_tt_class.get_user_timetable_all_data(this)

        setContentView(R.layout.activity_main2)
        //this.setToolbar()
        //this.setDrawerLayout()
        this.bottom_navi_view.setOnNavigationItemSelectedListener(bottomNav)

        val ft = supportFragmentManager.beginTransaction()
        //ft.replace(R.id.main_host_fragment, test())
        ft.replace(R.id.main_host_fragment, Home_fragment())
        ft.commit()



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
        var fragment : Fragment? = null
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
        }

        //drawer_layout.closeDrawer(GravityCompat.START)
        false
    }

    /*
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment : Fragment? = null

        when (item.itemId){
            R.id.nav_home ->{
                //fragment = Test_fragment()
                fragment = Home_fragment()
                Log.d(TAG, "select: Home_fragment")
            }
            R.id.nav_timetable -> {
                fragment = test()
                //fragment = Timetable_Fragment()
                Log.d(TAG, "select: Timetable_fragment")
            }
            //R.id.nav_assignment_list -> {
            R.id.nav_task -> {

                fragment = Task_list_Fragment()
                Log.d(TAG, "select: Assignment_list_fragment")
            }
            R.id.nav_message -> {
                fragment = MessageFragment()
                Log.d(TAG, "select fragment: Message_fragment")
            }
            R.id.nav_setting -> {
//                AuthUI.getInstance()
//                        .signOut(this)
//                        .addOnCompleteListener {
//                            val intent = Intent(this, LoginActivity::class.java)
//                            startActivity(intent)
//                        }
//                Log.d(TAG, "logout")
                fragment = Setting_Fragment()
                Log.d(TAG, "select fragment: Setting_Fragment")

            }
        }

        if (fragment != null){
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.main_host_fragment, fragment)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
     */

}