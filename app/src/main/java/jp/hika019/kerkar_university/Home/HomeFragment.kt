package jp.hika019.kerkar_university.Home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import jp.hika019.kerkar_university.databinding.FragmentHomeBinding
import jp.hika019.kerkar_university.viewmodels.Home_VM
import kotlin.collections.ArrayList

class HomeFragment(): Fragment() {

    private val TAG = "Home_fragment$tagHoge"

    private var courseIdArraylist = ArrayList<Int>()
    private var lectureIdArraylist = ArrayList<Int>()
    private var roomIdArraylist = ArrayList<Int>()


    val calendar: Calendar = Calendar.getInstance()
    val now_week = week_to_day_symbol_list[calendar.get(Calendar.DAY_OF_WEEK)-1]

    private val viewmodel by viewModels<Home_VM>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
        val view = FragmentHomeBinding.inflate(inflater, container, false)
        view.viewmodel = viewmodel
        view.lifecycleOwner = viewLifecycleOwner



        view.homeTimetableLinear.addView(createPeriod(requireContext()))
        view.homeTimetableLinear.addView(createCourseTextview(requireContext()))



        runBlocking {
            try{
                //timetable_onclick_event(view)
                loadTask(view)
                //firedb_timetable(view.context).courses_is_none()
            }catch (e: Exception){
                Log.w(TAG, "start -> error")
            }
        }



        view.floatingActionButton.setOnClickListener {

            val hoge = context?.let { it -> firedb_task(it) }
            hoge?.getCourseList()
        }

        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel.course_data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            for (period in 0 until period_num){
                showCourse(period)
            }
        })
    }

    private fun showCourse(period: Int){
        Log.d(TAG, "show_course -> call")
        Log.d(TAG, "$period_num")

        Log.d(TAG, "peri: $period")
        val titleId = courseIdArraylist[period]

        val courseTextview = requireView().findViewById<TextView>(titleId)
        val courseName = viewmodel.course_data.value?.get("$now_week${period+1}")?.get("course")
        if (courseName == null)
            courseTextview.text = ""
        else
            courseTextview.text = courseName.toString()

        val lectureId = lectureIdArraylist[period]
        val lectureTextview = requireView().findViewById<TextView>(lectureId!!)
        val courseLecturer = viewmodel.course_data.value?.get("$now_week${period+1}")?.get("lecturer") as List<String>?

        if (courseLecturer == null){
            lectureTextview.text = ""
        }else{
            if (courseLecturer.size == 1){
                Log.d(TAG, "course_lecturer: $courseLecturer")
                lectureTextview.text = courseLecturer[0]
            }else{
                lectureTextview.text = "${courseLecturer?.get(0)}...他"
            }
        }

        val room_id = roomIdArraylist[period]
        val room_textview = requireView().findViewById<TextView>(room_id)
        val course_room = viewmodel.course_data.value?.get("$now_week${period+1}")?.get("room")
        Log.d(TAG, "room: ${course_room}")
        if (course_room == null)
            room_textview.text = ""
        else
            room_textview.text = course_room.toString()

    }


    private fun createPeriod(context: Context): LinearLayout {
        Log.d(TAG, "create_period -> call")
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            1f
            )
        linearLayout.gravity = CENTER

        for (period in 1..viewmodel.period.value!!){
            val textView = TextView(context)
            textView.text = "${period}限"
            textView.gravity = CENTER
            textView.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f
            )
            linearLayout.addView(textView)
        }


        return linearLayout
    }

    private fun createCourseTextview(context: Context): LinearLayout {
        Log.d(TAG, "create_course_textview -> call")
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            3f
        )
        linearLayout.gravity = CENTER

        for (period in 1..viewmodel.period.value!!){
            val courseLinearlayoutBg = LinearLayout(context)
            courseLinearlayoutBg.orientation = LinearLayout.VERTICAL
            courseLinearlayoutBg.setBackgroundResource(R.drawable.timetable_course_background_gray)
            courseLinearlayoutBg.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f
            )
            val lpBg: ViewGroup.LayoutParams = courseLinearlayoutBg.layoutParams
            val mlpBg = lpBg as ViewGroup.MarginLayoutParams
            mlpBg.setMargins(4, 4, 4, 4)
            courseLinearlayoutBg.layoutParams = mlpBg;
            courseLinearlayoutBg.gravity = CENTER


            val courseLinearlayout = LinearLayout(context)
            courseLinearlayout.orientation = LinearLayout.VERTICAL
            courseLinearlayout.setBackgroundResource(R.drawable.timetable_course_background)
            courseLinearlayout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                4f
            )
            val lp: ViewGroup.LayoutParams = courseLinearlayout.layoutParams
            val mlp = lp as ViewGroup.MarginLayoutParams
            mlp.setMargins(6, 4, 6, 0)
            courseLinearlayout.layoutParams = mlp;
            courseLinearlayout.gravity = CENTER


            val courseName = TextView(context)
            courseName.id = ViewCompat.generateViewId()
            courseName.text = ""
            courseName.gravity = CENTER
            courseName.textSize = 12f
            courseName.maxLines = 2
            courseName.maxEms = 10
            courseName.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                4f
            )

            courseName.setOnClickListener {
                viewmodel.get_course_data(now_week, period, context)
            }


            val courseTeacher = TextView(context)
            courseTeacher.id = ViewCompat.generateViewId()
            courseTeacher.text = ""
            courseTeacher.gravity = CENTER
            courseTeacher.textSize = 8f
            courseTeacher.maxLines = 1
            courseTeacher.maxEms = 8
            courseTeacher.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )

            val courseRoom = TextView(context)
            courseRoom.id = ViewCompat.generateViewId()
            courseRoom.text = ""
            courseRoom.gravity = CENTER
            courseRoom.textSize = 8f
            courseRoom.maxLines = 1
            courseRoom.maxEms = 8
            courseRoom.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )

            courseIdArraylist.add(courseName.id)
            lectureIdArraylist.add(courseTeacher.id)
            roomIdArraylist.add(courseRoom.id)


            courseLinearlayout.addView(courseName)
            courseLinearlayout.addView(courseTeacher)

            courseLinearlayoutBg.addView(courseLinearlayout)
            courseLinearlayoutBg.addView(courseRoom)

            linearLayout.addView(courseLinearlayoutBg)
        }
        return linearLayout
    }

    private fun loadTask(view: FragmentHomeBinding){
        context?.let { firedb_task(it).getTomorrowNotCompTaskList(view) }
    }

}