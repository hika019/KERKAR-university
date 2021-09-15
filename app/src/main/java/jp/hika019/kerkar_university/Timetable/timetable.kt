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
import kotlinx.android.synthetic.main.timetable.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat.generateViewId
import androidx.core.view.marginTop
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.Timetable.Create_timetableActivity
import jp.hika019.kerkar_university.viewmodels.Timetable_VM
import jp.hika019.kerkar_university.databinding.TimetableBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class test: Fragment() {

    private val TAG = "test_timetable"

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
    private var id_map = mutableMapOf<String, Int>()

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
//            if (period != 1)
//                view.hogee.addView(row_spacer())

            view.hogee.addView(row_courses(period))
        }



        return view.root
//        view.semester_textView.text = timetable_name
//        view.setting_ic.setOnClickListener {
//            val i = Intent(context, Create_timetableActivity::class.java)
//            context?.startActivity(i)
//        }
//
//        //曜日
//        view.hogee.addView(week_title(week_num))
//        Log.d(TAG, "period: $period_num")
//        for(period in 1..period_num){
//            view.hogee.addView(row_spacer())
//            view.hogee.addView(row_courses(period))
//        }
//
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel.course_data.observe(viewLifecycleOwner, Observer {

            for (week in week_to_day_symbol_list){
                for (period in period_list){
                    val week_period = "$week$period"
                    val id = id_map[week_period]
                    if (id != null){
                        val hoge = view.findViewById<TextView>(id!!)
                        val course_name = viewmodel.course_data.value?.get(week_period)?.get("course")
                        if (course_name == null)
                            hoge.text = ""
                        else
                            hoge.text = course_name.toString()
                    }
                }
            }
        })

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
        course.setBackgroundResource(R.color.white)
        course.elevation = 2f

        val lp: ViewGroup.LayoutParams = course.layoutParams
        val mlp = lp as MarginLayoutParams
        mlp.setMargins(4, 4, 4, 4)
        course.layoutParams = mlp;


        val hoge = viewmodel.course_data.value?.get("mon1")
        //Log.d("hoge", "dha: $hoge")

        val couse_name_textview = TextView(context)
        couse_name_textview.id = generateViewId()
        couse_name_textview.text = "@={viewmodel.course_data.value?.get('mon1')?.get('course')}"
        couse_name_textview.gravity = CENTER
        couse_name_textview.textSize = 10f
        couse_name_textview.maxLines = 2
        couse_name_textview.maxEms = 10
        couse_name_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            8f
        )

        id_map["$week$period"] = couse_name_textview.id
        //Log.d("hoge", "id: $id_map")


        couse_name_textview.setOnClickListener {
            viewmodel.get_course_data(week, period, requireContext())
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
                //Linear.addView(hoge)
                Linear.addView(course(week_to_day_symbol_list[week], period, get_course_name(week_and_period), get_lecturer(week_and_period)))
            }
        }
        return Linear
    }



}