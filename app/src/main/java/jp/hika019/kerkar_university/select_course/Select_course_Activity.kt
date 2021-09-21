package jp.hika019.kerkar_university.select_course

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.util.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.databinding.ActivitySelectCourseBinding
import jp.hika019.kerkar_university.viewmodels.Select_course_VM

class Select_course_Activity: AppCompatActivity() {

    private val viewmodel by viewModels<Select_course_VM>()
    private val TAG = "Select_course_Activity"+ TAG_hoge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySelectCourseBinding>(
            this, R.layout.activity_select_course
        )

        val intent_data = intent.getStringArrayExtra("week_period")


        val week = intent_data!![0]
        val period = intent_data!![1].toInt()

        val week_jp = week_to_day_jp_chenger(week)
        viewmodel.week_to_day.value = "${week_jp}曜日 ${period}限 での検索結果"

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        tmp_str = "$week$period"

        Log.d(TAG, "wtd: $week$period")

        val toolbar = binding.toolbar6
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val hoge = firedb_timetable_new()
        hoge.get_course_list(binding, this, "$week$period")

        binding.CreateCourseButton.setOnClickListener {
            val i = Intent(this, Create_courceActivity::class.java)
            createcource_wtd = week
            createcource_period = period
            startActivity(i)
            finish()
        }

        createtask_finish.observe(this, Observer {
            if (createtask_finish.value == true){
                createtask_finish.value = false
                finish()
            }
        })

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search){

        }else{
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}