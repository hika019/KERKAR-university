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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import jp.hika019.kerkar_university.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.item_home_activity_taimetable.view.*
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import jp.hika019.kerkar_university.databinding.ActivityHomeBinding
import jp.hika019.kerkar_university.viewmodels.Home_VM

class Home_fragment(): Fragment() {

    private val TAG = "Home_fragment"

    private var course_id_map = mutableMapOf<String, Int>()
    private var lecture_id_map = mutableMapOf<String, Int>()

    val calendar: Calendar = Calendar.getInstance()
    val now_week_to_day = week_to_day_symbol_list[calendar.get(Calendar.DAY_OF_WEEK)-1]

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
                //load_timetable(view, requireContext())
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

    }

    private fun create_period(context: Context): LinearLayout {
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

            course_linearLayout.addView(course_name)
            course_linearLayout.addView(course_teacher)
            linearLayout.addView(course_linearLayout)
        }
        return linearLayout
    }



    private fun load_timetable(view: View, context: Context){
        Log.d(TAG, "load_timetable -> call")

//        for (period in 1..period_num){
//            show_timetable(view, period, get_course_name("${now_week_to_day+period}"))
//        }
    }

//    private fun show_timetable(view: View, period: Int, course_name: String){
//        when(period){
//            1 -> view.today_first_period.timetable_title_textView.text = course_name
//            2 -> view.today_second_period.timetable_title_textView.text = course_name
//            3 -> view.today_third_period.timetable_title_textView.text = course_name
//            4 -> view.today_fourth_period.timetable_title_textView.text = course_name
//            5 -> view.today_fifth_period.timetable_title_textView.text = course_name
//        }
//    }
//
//    private fun timetable_onclick_event(view: View){
//        val firedb_timetable = context?.let { firedb_timetable(it) }
//
//        view.today_first_period.setOnClickListener {
//            firedb_timetable?.get_course_data(now_week_to_day, 1)
//        }
//        view.today_second_period.setOnClickListener {
//            firedb_timetable?.get_course_data(now_week_to_day, 2)
//        }
//        view.today_third_period.setOnClickListener {
//            firedb_timetable?.get_course_data(now_week_to_day, 3)
//        }
//        view.today_fourth_period.setOnClickListener {
//            firedb_timetable?.get_course_data(now_week_to_day, 4)
//        }
//        view.today_fifth_period.setOnClickListener {
//            firedb_timetable?.get_course_data(now_week_to_day, 5)
//        }
//
//    }

    private fun load_task(view: View){
        context?.let { firedb_task(it).get_tomorrow_not_comp_task_list(view) }
    }

}