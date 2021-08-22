package jp.hika019.kerkar_university.Setup

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.Setting_Fragment
import jp.hika019.kerkar_university.setup_dialog

class SetupActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "Setup_Activity"

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        Log.d(TAG, "hogeeee")
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.setup_host_fragment, Select_university_fragment())
        ft.commit()



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

}

class setup(): setup_dialog(){
    fun hoge(context: Context){

    }
}