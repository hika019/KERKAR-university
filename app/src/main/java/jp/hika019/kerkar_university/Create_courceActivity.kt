package jp.hika019.kerkar_university

import android.content.Context
import android.os.Bundle
import android.util.Log
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

        //setToolbar()

        val binding = DataBindingUtil.setContentView<ActivityCreateCourcetBinding>(this, R.layout.activity_create_courcet)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.finish_event.observe(this, androidx.lifecycle.Observer {
            if (viewmodel.finish_event.value == true)
                finish()
        })





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        /*
        // android.R.id.home に戻るボタンを押した時のidが取得できる
        if (item.itemId == android.R.id.home) {
            // 今回はActivityを終了させている
            finish()
        }

         */
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar(){
        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    public fun setOnFinish(callback: () -> Unit){

    }

}