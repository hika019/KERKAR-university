package jp.hika019.kerkar_university

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import jp.hika019.kerkar_university.Home.Home_fragment
import jp.hika019.kerkar_university.Message.MessageFragment
import jp.hika019.kerkar_university.Register_and_Login.LoginActivity
import jp.hika019.kerkar_university.Task.Task_list_Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val TAG = "MainActivity"

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        start()
        setTheme(R.style.AppTheme_Splash)

        setContentView(R.layout.activity_main)

        this.setToolbar()
        this.setDrawerLayout()


        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, Home_fragment())
        ft.commit()

    }

    private fun setToolbar(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(false)

    }


    private fun setDrawerLayout(){
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open , R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment : Fragment? = null

        when (item.itemId){
            R.id.nav_home ->{
                fragment = Home_fragment()
                Log.d(TAG, "select: Home_fragment")
            }
            R.id.nav_timetable -> {
                fragment = Timetable_Fragment()
                Log.d(TAG, "select: Timetable_fragment")
            }
            R.id.nav_assignment_list -> {
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
            ft.replace(R.id.nav_host_fragment, fragment)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun start(){
        Log.d(TAG, "start() -> call")
        val dataStore = getSharedPreferences(UserData_SharedPreferences_name, Context.MODE_PRIVATE)

        val local_uid = dataStore.getString("uid", null)

        if (local_uid != null){
            uid = local_uid
            Log.d(TAG, "uid: $uid")



            firedb_register_login(this).cheak_user_data()
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }else{
            val uuid = UUID.randomUUID().toString()
            create_uid(uuid)

//            uid = sha256(uuid).substring(0, 24)
            Log.d("hoge", uid!!)

            val editor: SharedPreferences.Editor = dataStore.edit()
            editor.putString("uid", uid)
            editor.commit()

            val register_dialog_class = register_dialog(this)
            register_dialog_class.select_univarsity_rapper()
        }
    }
}