package jp.hika019.kerkar_university

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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

        viewmodel.createbutton_enable.observe(this, Observer {
            if(viewmodel.createbutton_enable.value == true){
                binding.CreateButton.setTextColor(getColor(R.color.logo_color))
            }
        })

        viewmodel.course_lecture.observe(this, Observer {
            binding.lectureLinearLayout.removeAllViews()
            if(viewmodel.course_lecture_list.value != null){
                for(lecturer in viewmodel.course_lecture_list.value!!){
                val textview = TextView(this)
                textview.text = lecturer
                textview.textSize = 12f
                textview.setPadding(8, 0, 8, 0)
                binding.lectureLinearLayout.addView(textview)
                }
            }

        })



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }


}