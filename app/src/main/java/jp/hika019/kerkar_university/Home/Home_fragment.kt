package jp.hika019.kerkar_university.Home

import android.content.Context
import android.content.Intent
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
import jp.hika019.kerkar_university.Course_detail.Course_detail_Activity
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import jp.hika019.kerkar_university.databinding.FragmentHomeBinding
import jp.hika019.kerkar_university.viewmodels.Home_VM
import kotlin.collections.ArrayList

class Home_fragment(): Fragment() {

    private val TAG = "Home_fragment"

    private var course_id_ArrayList = ArrayList<Int>()
    private var lecture_id_ArrayList = ArrayList<Int>()
    private var room_id_ArrayList = ArrayList<Int>()


    val calendar: Calendar = Calendar.getInstance()
    val now_week = week_to_day_symbol_list[calendar.get(Calendar.DAY_OF_WEEK)-1]

    private val viewmodel by viewModels<Home_VM>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
        val view = FragmentHomeBinding.inflate(inflater, container, false)
        view.viewmodel = viewmodel
        view.lifecycleOwner = viewLifecycleOwner



        view.homeTimetableLinear.addView(create_period(requireContext()))
        view.homeTimetableLinear.addView(create_course_textview(requireContext()))



        runBlocking {
            try{
                //timetable_onclick_event(view)
                load_task(view)
                //firedb_timetable(view.context).courses_is_none()
            }catch (e: Exception){
                Log.w(TAG, "start -> error")
            }
        }



        view.floatingActionButton.setOnClickListener {

            val hoge = context?.let { it -> firedb_task(it) }
            if (hoge != null) {
                hoge.get_course_list()
            }
        }

        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel.course_data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            for (period in 0 until period_num){
                show_course(period)
            }
        })
    }

    private fun show_course(period: Int){
        Log.d(TAG, "show_course -> call")
        Log.d(TAG, "$period_num")

        Log.d(TAG, "peri: $period")
        val title_id = course_id_ArrayList[period]

        val course_textview = requireView().findViewById<TextView>(title_id)
        val course_name = viewmodel.course_data.value?.get("$now_week${period+1}")?.get("course")
        if (course_name == null)
            course_textview.text = ""
        else
            course_textview.text = course_name.toString()

        val lecture_id = lecture_id_ArrayList[period]
        val lecture_textview = requireView().findViewById<TextView>(lecture_id!!)
        val course_lecturer = viewmodel.course_data.value?.get("$now_week${period+1}")?.get("lecturer") as List<String>?

        if (course_lecturer == null){
            lecture_textview.text = ""
        }else{
            if (course_lecturer.size == 1){
                Log.d(TAG, "course_lecturer: $course_lecturer")
                lecture_textview.text = "${course_lecturer[0]}"
            }else{
                lecture_textview.text = "${course_lecturer?.get(0)}...他"
            }
        }

        val room_id = room_id_ArrayList[period]
        val room_textview = requireView().findViewById<TextView>(room_id)
        val course_room = viewmodel.course_data.value?.get("$now_week${period+1}")?.get("room")
        if (course_room != null)
            room_textview.text = ""
        else
            room_textview.text = course_room.toString()

    }


    private fun create_period(context: Context): LinearLayout {
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

    private fun create_course_textview(context: Context): LinearLayout {
        Log.d(TAG, "create_course_textview -> call")
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            3f
        )
        linearLayout.gravity = CENTER

        for (period in 1..viewmodel.period.value!!){
            val course_linearLayout_bg = LinearLayout(context)
            course_linearLayout_bg.orientation = LinearLayout.VERTICAL
            course_linearLayout_bg.setBackgroundResource(R.drawable.timetable_course_background_gray)
            course_linearLayout_bg.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f
            )
            val lp_bg: ViewGroup.LayoutParams = course_linearLayout_bg.layoutParams
            val mlp_bg = lp_bg as ViewGroup.MarginLayoutParams
            mlp_bg.setMargins(4, 4, 4, 4)
            course_linearLayout_bg.layoutParams = mlp_bg;
            course_linearLayout_bg.gravity = CENTER


            val course_linearLayout = LinearLayout(context)
            course_linearLayout.orientation = LinearLayout.VERTICAL
            course_linearLayout.setBackgroundResource(R.drawable.timetable_course_background)
            course_linearLayout.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                4f
            )
            val lp: ViewGroup.LayoutParams = course_linearLayout.layoutParams
            val mlp = lp as ViewGroup.MarginLayoutParams
            mlp.setMargins(6, 4, 6, 0)
            course_linearLayout.layoutParams = mlp;
            course_linearLayout.gravity = CENTER


            val course_name = TextView(context)
            course_name.id = ViewCompat.generateViewId()
            course_name.text = ""
            course_name.gravity = CENTER
            course_name.textSize = 12f
            course_name.maxLines = 2
            course_name.maxEms = 10
            course_name.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                4f
            )

            course_name.setOnClickListener {
                viewmodel.get_course_data(now_week, period, context)
            }


            val course_teacher = TextView(context)
            course_teacher.id = ViewCompat.generateViewId()
            course_teacher.text = ""
            course_teacher.gravity = CENTER
            course_teacher.textSize = 8f
            course_teacher.maxLines = 1
            course_teacher.maxEms = 8
            course_teacher.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )

            val course_room = TextView(context)
            course_room.id = ViewCompat.generateViewId()
            course_room.text = ""
            course_room.gravity = CENTER
            course_room.textSize = 8f
            course_room.maxLines = 1
            course_room.maxEms = 8
            course_room.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )

            course_id_ArrayList.add(course_name.id)
            lecture_id_ArrayList.add(course_teacher.id)
            room_id_ArrayList.add(course_room.id)


            course_linearLayout.addView(course_name)
            course_linearLayout.addView(course_teacher)

            course_linearLayout_bg.addView(course_linearLayout)
            course_linearLayout_bg.addView(course_room)

            linearLayout.addView(course_linearLayout_bg)
        }
        return linearLayout
    }

    private fun load_task(view: FragmentHomeBinding){
        context?.let { firedb_task(it).get_tomorrow_not_comp_task_list(view) }
    }

}