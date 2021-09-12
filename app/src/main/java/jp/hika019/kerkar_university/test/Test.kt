package jp.hika019.kerkar_university.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.test.view.*
import android.view.ViewGroup.MarginLayoutParams
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.Timetable.Create_timetableActivity


class test: Fragment() {

    private val TAG = "test_timetable"


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
        0.1f
    )

    private val set_timetable_Column_space_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        0.2f
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.test, container, false)

        view.semester_textView.text = timetable_name

        view.setting_ic.setOnClickListener {
            val i = Intent(context, Create_timetableActivity::class.java)
            context?.startActivity(i)
        }


        //曜日
        view.hogee.addView(week_title(week_num))
        Log.d(TAG, "period: $period_num")
        for(period in 1..period_num){
            view.hogee.addView(row_spacer())
            view.hogee.addView(row_courses(period))
        }



        return view
    }

    private fun week_title(size: Int): LinearLayout {
        val week_title_Linear = LinearLayout(context)
        week_title_Linear.layoutParams = set_timetable_title_layout
        week_title_Linear.gravity = CENTER

        //曜日
        for(index in 0..size){
            val textView = TextView(context)
            textView.textSize = 14f
            if(index != 0){
                textView.text = week_to_day_symbol_list_jp_short[index-1]
                textView.layoutParams = set_timetable_row_layout
            }else{
                textView.text = ""
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

    private fun course(week_and_period: String): LinearLayout {
        return course(week_and_period, "読み込み中", "読み込み中")
    }

    private fun course(week_and_period: String, course_name: String, teacher_name: String): LinearLayout {

        var course_name = course_name
        var teacher_name = teacher_name

        /*
        if (course_name == "" || course_name.isNullOrEmpty()){
            course_name = "読み込み中"
        }
        if (teacher_name == "" || teacher_name.isNullOrEmpty()){
            teacher_name = "読み込み中"
        }
         */

        val course = LinearLayout(context)
        //course.setBackgroundResource(R.color.black)
        course.orientation = LinearLayout.VERTICAL
        course.layoutParams = set_timetable_course_layout

        val lp: ViewGroup.LayoutParams = course.layoutParams
        val mlp = lp as MarginLayoutParams
        mlp.setMargins(4, 4, 4, 4)
        course.layoutParams = mlp;



        val couse_name_textview = TextView(context)
        couse_name_textview.text = course_name
        couse_name_textview.gravity = CENTER
        couse_name_textview.textSize = 10f
        couse_name_textview.maxLines = 2
        couse_name_textview.maxEms = 10
        couse_name_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            2f
        )

        couse_name_textview.setOnClickListener {
            firedb_timetable(requireContext()).get_course_data(week_and_period)
        }

        val teacher_name_textview = TextView(context)
        teacher_name_textview.text = teacher_name
        teacher_name_textview.gravity = CENTER
        teacher_name_textview.textSize = 10f
        teacher_name_textview.maxLines = 1
        teacher_name_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            0.5f
        )



        //Log.d("hogee", "na: $week_and_period")

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
                Linear.addView(hoge)
                Linear.addView(course(week_and_period, get_course_name(week_and_period), get_lecturer(week_and_period)))
            }
        }
        return Linear
    }



}