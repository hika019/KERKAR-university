package jp.hika019.kerkar_university

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



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        val item = menu.findItem(R.id.create_menu)
        item.isEnabled = false

        viewmodel.create_button_enable.observe(this, Observer {
            item.isEnabled = viewmodel.create_button_enable.value == true
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.create_menu){
            viewmodel.create(this)
        }else{
            finish()
        }


        return super.onOptionsItemSelected(item)
    }

}