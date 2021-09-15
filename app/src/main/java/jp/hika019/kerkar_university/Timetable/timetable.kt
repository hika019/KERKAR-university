package jp.hika019.kerkar_university.test

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat.generateViewId
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.viewmodels.Timetable_VM
import jp.hika019.kerkar_university.databinding.TimetableBinding
import android.util.*


class test: Fragment() {

    private val TAG = "test_timetable" + TAG_hoge

    private val viewmodel by viewModels<Timetable_VM>()




    private val set_timetable_row_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.MATCH_PARENT,
        1.0f
    )

    private val set_timetable_Column_layout = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        0,
        1.0f
    )

    private val set_timetable_row_space_layout = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        0,
        0.025f
    )

    private val set_timetable_Column_space_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        0.05f
    )

    private val set_timetable_period_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.MATCH_PARENT,
        0.7f
    )

    private val set_timetable_title_layout = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        0,
        0.4f
    )

    private val set_timetable_course_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.MATCH_PARENT,
        1f
    )

    var id: Int? = null
    private var course_id_map = mutableMapOf<String, Int>()
    private var lecture_id_map = mutableMapOf<String, Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = TimetableBinding.inflate(inflater, container, false)
        view.viewmodel = viewmodel
        view.lifecycleOwner = viewLifecycleOwner


        view.hogee.addView(week_title(week_num))
        for (period in 1..period_num){
            //空白消去
//            if (period != 1)
//                view.hogee.addView(row_spacer())

            view.hogee.addView(row_courses(period))
        }



        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel.course_data.observe(viewLifecycleOwner, Observer {

            for (week_index in 1..week_num){
                for (period in 1..period_num){
                    val week_period = "${week_to_day_symbol_list[week_index]}$period"
                    show_course(week_period)
                }
            }
        })

    }

    fun show_course(week_period: String){
        Log.d(TAG, "show_course(${week_period})")
        val title_id = course_id_map[week_period]

        Log.d(TAG, "title_id: $title_id")

        val course_textview = requireView().findViewById<TextView>(title_id!!)
        if (title_id != null){
            val course_name = viewmodel.course_data.value?.get(week_period)?.get("course")
            if (course_name == null)
                course_textview.text = ""
            else
                course_textview.text = course_name.toString()
        }else{
            course_textview.text = ""
        }

        val lecture_id = lecture_id_map[week_period]
        //Log.d(TAG, "($week_period) lecture_id: $lecture_id")
        if (lecture_id != null){
            val lecture_textview = requireView().findViewById<TextView>(lecture_id!!)
            val course_lecturer = viewmodel.course_data.value?.get(week_period)?.get("lecturer") as List<String>?
            //Log.d(TAG, "($week_period)course_lecturer: ${course_lecturer}")

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


    }

    private fun week_title(size: Int): LinearLayout {
        val week_title_Linear = LinearLayout(context)
        week_title_Linear.layoutParams = set_timetable_title_layout
        week_title_Linear.gravity = CENTER

        //曜日
        for(index in 0..size){
            val textView = TextView(context)
            textView.textSize = 18f
            if(index != 0){
                textView.text = week_to_day_symbol_list_jp_short[index-1]
                textView.layoutParams = set_timetable_row_layout
            }else{
                textView.text = ""
                //textView.background = R.color.black.toDrawable()
                textView.layoutParams = set_timetable_period_layout
            }
            textView.gravity = CENTER

            //textView.setBackgroundResource(R.color.black)

            week_title_Linear.addView(textView)

            if (index != size){
                val space = View(context)
                space.layoutParams = set_timetable_Column_space_layout
                //space.setBackgroundResource(R.color.black)
                week_title_Linear.addView(space)
            }
        }
        return week_title_Linear

    }


    private fun course(week: String, period: Int, course_name: String, teacher_name: String): LinearLayout {

        var course_name = course_name
        var teacher_name = teacher_name



        val course = LinearLayout(context)
        //course.setBackgroundResource(R.color.black)
        course.orientation = LinearLayout.VERTICAL
        course.layoutParams = set_timetable_course_layout
        course.setBackgroundResource(R.drawable.timetable_course_background)
        course.elevation = 2f

        val lp: ViewGroup.LayoutParams = course.layoutParams
        val mlp = lp as MarginLayoutParams
        mlp.setMargins(4, 4, 4, 4)
        course.layoutParams = mlp;



        val couse_name_textview = TextView(context)
        couse_name_textview.id = generateViewId()
        couse_name_textview.text = "読み込み中"
        couse_name_textview.gravity = CENTER
        couse_name_textview.textSize = 10f
        couse_name_textview.maxLines = 2
        couse_name_textview.maxEms = 10
        couse_name_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            2f
        )

        course_id_map.put("$week$period" , couse_name_textview.id)
        //Log.d(TAG, "course_id_map: ${course_id_map}")


        couse_name_textview.setOnClickListener {
            viewmodel.get_course_data(week, period, requireContext())
        }

        val teacher_name_textview = TextView(context)
        teacher_name_textview.id = generateViewId()
        teacher_name_textview.text = teacher_name
        teacher_name_textview.gravity = CENTER
        teacher_name_textview.textSize = 10f
        teacher_name_textview.maxLines = 1
        teacher_name_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            1f
        )

        lecture_id_map.put("$week$period" , teacher_name_textview.id)
        //Log.d(TAG, "lecture_id_map: ${lecture_id_map}")

        course.addView(couse_name_textview)
        course.addView(teacher_name_textview)

        return course

    }

    private fun row_spacer(): View {
        val hoge = View(context)
        hoge.layoutParams = set_timetable_row_space_layout
        return hoge
    }

    private fun row_courses(period: Int): LinearLayout {
        val Linear = LinearLayout(context)
        //Liner.setBackgroundResource(R.color.black)
        Linear.orientation = LinearLayout.HORIZONTAL
        Linear.layoutParams = set_timetable_Column_layout


        for (week in 0..week_num){
            if (week == 0){

                val textView = TextView(context)
                textView.text = "${period}限"
                textView.gravity = CENTER
                textView.layoutParams = set_timetable_period_layout
                Linear.addView(textView)


            }else{
                val week_and_period = week_to_day_symbol_list[week]+period.toString()
                val hoge = View(context)
                hoge.layoutParams = set_timetable_Column_space_layout
                //Linear.addView(hoge)
                Linear.addView(course(week_to_day_symbol_list[week], period, get_course_name(week_and_period), get_lecturer(week_and_period)))
            }
        }
        return Linear
    }



}