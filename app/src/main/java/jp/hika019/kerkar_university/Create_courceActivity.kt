package jp.hika019.kerkar_university

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.generateViewId
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import jp.hika019.kerkar_university.databinding.ActivityCreateCourcetBinding
import kotlinx.android.synthetic.main.activity_create_courcet.*
import java.util.*

class Create_courceActivity: AppCompatActivity() {

    private val TAG = "Create_courceActivity"
    private val viewmodel by viewModels<Create_courceVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_create_courcet)



        val binding = DataBindingUtil.setContentView<ActivityCreateCourcetBinding>(this, R.layout.activity_create_courcet)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


        val toolbar = binding.toolbar2
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        viewmodel.finish_event.observe(this, androidx.lifecycle.Observer {
            if (viewmodel.finish_event.value == true)
                finish()
        })





    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }


}