package jp.hika019.kerkar_university

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import jp.hika019.kerkar_university.databinding.ActivityCreateTaskBinding
import jp.hika019.kerkar_university.viewmodels.Create_task_VM
import kotlinx.android.synthetic.main.activity_create_task.*
import android.util.*
import kotlinx.android.synthetic.main.activity_create_timetable.*

class Create_task_Activity: AppCompatActivity() {

    private val TAG = "Create_task_Activity" + TAG_hoge
    private val viewmodel by viewModels<Create_task_VM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityCreateTaskBinding>(
            this, R.layout.activity_create_task
        )

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar5)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val hoge  = intent.getStringExtra("week_period")
        Log.d(TAG, "hoge: $hoge")
        viewmodel.week_period = hoge
        viewmodel.get_course_name()

        createtask_finish.observe(this, Observer {

            if (createtask_finish.value == true){
                createtask_finish.value = false
                finish()
            }
        })

        viewmodel.create_button_enable.observe(this, Observer {
            if(viewmodel.create_button_enable.value == true){
                binding.button7.setTextColor(getColor(R.color.logo_color))
            }
        })



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}