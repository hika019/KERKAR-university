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
import jp.hika019.kerkar_university.databinding.ActivityHomeBinding
import jp.hika019.kerkar_university.viewmodels.Home_VM
import kotlin.collections.ArrayList

class Home_fragment(): Fragment() {

    private val TAG = "Home_fragment"

    private var course_id_ArrayList = ArrayList<Int>()
    private var lecture_id_ArrayList = ArrayList<Int>()

    val calendar: Calendar = Calendar.getInstance()
    val now_week = week_to_day_symbol_list[calendar.get(Calendar.DAY_OF_WEEK)-1]

    private val viewmodel by viewModels<Home_VM>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
        val view = ActivityHomeBinding.inflate(inflater, container, false)
        view.viewmodel = viewmodel
        view.lifecycleOwner = viewLifecycleOwner



        view.homeTimetableLinear.addView(create_period(requireContext()))
        view.homeTimetableLinear.addView(create_course_textview(requireContext()))



        runBlocking {
            try{
                //timetable_onclick_event(view)
                //load_task(view)
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
        val course_name = viewmodel.course_data.value?.get("$now_week$period")?.get("course")
        if (course_name == null)
            course_textview.text = ""
        else
            course_textview.text = course_name.toString()

        val lecture_id = lecture_id_ArrayList[period]
        val lecture_textview = requireView().findViewById<TextView>(lecture_id!!)
        val course_lecturer = viewmodel.course_data.value?.get("$now_week$period")?.get("lecturer") as List<String>?

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
        linearLayout

        for (period in 1..viewmodel.period.value!!){
            val course_linearLayout = LinearLayout(context)
            course_linearLayout.orientation = LinearLayout.VERTICAL
            course_linearLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f
            )
            val lp: ViewGroup.LayoutParams = course_linearLayout.layoutParams
            val mlp = lp as ViewGroup.MarginLayoutParams
            mlp.setMargins(4, 4, 4, 4)
            course_linearLayout.layoutParams = mlp;

            course_linearLayout.gravity = CENTER

            val course_name = TextView(context)
            course_name.id = ViewCompat.generateViewId()
            course_name.text = "授業名"
            course_name.gravity = CENTER
            course_name.textSize = 12f
            course_name.maxLines = 2
            course_name.maxEms = 10
            course_name.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                2f
            )

            course_name.setOnClickListener {
                viewmodel.get_course_data(now_week, period, context)
            }

            course_id_ArrayList.add(course_name.id)

            val course_teacher = TextView(context)
            course_teacher.text = "教師"
            course_teacher.gravity = CENTER
            course_teacher.textSize = 8f
            course_teacher.maxLines = 1
            course_teacher.maxEms = 8
            course_teacher.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )

            lecture_id_ArrayList.add(course_name.id)

            course_linearLayout.addView(course_name)
            course_linearLayout.addView(course_teacher)
            linearLayout.addView(course_linearLayout)
        }
        return linearLayout
    }

    private fun load_task(view: View){
        context?.let { firedb_task(it).get_tomorrow_not_comp_task_list(view) }
    }

}