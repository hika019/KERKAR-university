package jp.hika019.kerkar_university.select_course

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.TAG_hoge
import jp.hika019.kerkar_university.databinding.ActivitySelectCourseBinding
import jp.hika019.kerkar_university.firedb_timetable_new
import jp.hika019.kerkar_university.viewmodels.Select_course_VM

class Select_course_Activity: AppCompatActivity() {

    private val viewmodel by viewModels<Select_course_VM>()
    private val TAG = "Select_course_Activity"+ TAG_hoge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySelectCourseBinding>(
            this, R.layout.activity_select_course
        )

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        val toolbar = binding.toolbar6
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val hoge = firedb_timetable_new()
        hoge.get_course_list(binding, this, "mon1")

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search){

        }else{
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}