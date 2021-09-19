package jp.hika019.kerkar_university

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import jp.hika019.kerkar_university.databinding.ActivityCreateTaskBinding
import jp.hika019.kerkar_university.viewmodels.Create_task_VM
import kotlinx.android.synthetic.main.activity_create_task.*

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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.create_menu){
            viewmodel.create()
        }else{
            finish()
        }


        return super.onOptionsItemSelected(item)
    }

}