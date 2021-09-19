package jp.hika019.kerkar_university

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import jp.hika019.kerkar_university.viewmodels.Create_courceVM
import jp.hika019.kerkar_university.databinding.ActivityCreateCourcetBinding

class Create_courceActivity: AppCompatActivity() {

    private val TAG = "Create_courceActivity"
    private val viewmodel by viewModels<Create_courceVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_create_courcet)



        val binding = DataBindingUtil.setContentView<ActivityCreateCourcetBinding>(
            this, R.layout.activity_create_courcet
        )
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


        val toolbar = binding.toolbar2
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


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