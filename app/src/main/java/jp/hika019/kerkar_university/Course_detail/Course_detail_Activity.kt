package jp.hika019.kerkar_university.Course_detail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.viewmodels.Course_ditail_VM
import jp.hika019.kerkar_university.databinding.ActivityCourseDetailBinding
import kotlinx.android.synthetic.main.activity_course_detail.*

class Course_detail_Activity: AppCompatActivity() {

    private val TAG = "Course_detail_Activity" + TAG_hoge
    private val viewmodel by viewModels<Course_ditail_VM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCourseDetailBinding>(
            this, R.layout.activity_course_detail
        )

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this
        set_toolbar()

        createtimetable_finish.observe(this, Observer {
            if (createtimetable_finish.value == true){
                createtimetable_finish.value =false
                finish()
            }
        })

        viewmodel.course_lecturer.observe(this, Observer {
            binding.courseLecturerLinearLayout.removeAllViews()
            for(lecturer in viewmodel.course_lecturer.value!!){
                val textview = TextView(this)
                textview.text = lecturer
                textview.textSize = 20f
                textview.setPadding(8, 0, 8, 0)
                //textview.setTextColor(getColor(R.color.white))
                //textview.setBackgroundResource(R.drawable.lecturer_background)

                binding.courseLecturerLinearLayout.addView(textview)
            }


        })

        floatingActionButton2.setOnClickListener {
            val i = Intent(this, Create_task_Activity::class.java)
            startActivity(i)
        }



        val intent_data = intent.getStringArrayExtra("week_period")

        viewmodel.week_period.value = "${intent_data!![0]}${intent_data[1]}"

//        val hoge = listOf(1,2,3,4,5,6,7,8,9,10)

        val hoge = firedb_task_new()
        hoge.get_course_task(task_recycleview, viewmodel.week_period.value!!, viewmodel.course_id!!, this)

        binding.courseLecturerLinearLayout






    }

    fun set_toolbar(){
        val toolbar = course_detail_toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}