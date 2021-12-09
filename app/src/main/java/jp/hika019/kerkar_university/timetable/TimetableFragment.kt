package jp.hika019.kerkar_university.test

import android.graphics.Typeface
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
import jp.hika019.kerkar_university.databinding.FragmentTimetableBinding
import android.util.*
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class TimetableFragment: Fragment() {

    private val TAG = "Timetable_Fragment" + tagHoge

    private val viewmodel by viewModels<Timetable_VM>()



    private val set_timetable_Column_layout = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        0,
        1.0f
    )

    private val set_timetable_row_space_layout = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        0,
        0.5f
    )

    private val set_timetable_Column_space_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        0.05f
    )

    private val set_timetable_period_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.MATCH_PARENT,
        0.4f
    )

    private val set_timetable_title_layout = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        0,
        0.3f
    )

    private val set_timetable_course_layout = LinearLayout.LayoutParams(
        0,
        ViewGroup.LayoutParams.MATCH_PARENT,
        1.0f
    )

    var id: Int? = null
    private var course_id_map = mutableMapOf<String, Int>()
    private var lecture_id_map = mutableMapOf<String, Int>()
    private var room_id_map = mutableMapOf<String, Int>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentTimetableBinding.inflate(inflater, container, false)
        view.viewmodel = viewmodel
        view.lifecycleOwner = viewLifecycleOwner

        timetable_id.asFlow()
            .onEach {

                user_timetable_data_live.asFlow()
                    .onEach {
                        view.hogee.removeAllViews()

                        view.hogee.addView(week_title(week_num))
                        for (period in 1..period_num){
                            view.hogee.addView(row_courses(period))
                        }
                    }
                    .launchIn(lifecycleScope)

            }
            .launchIn(lifecycleScope)




        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        course_data_live.observe(viewLifecycleOwner, Observer {

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

        //Log.d(TAG, "title_id: $title_id")

        val course_data = course_data_live.value?.get(week_period) as? Map<String, Any?>
        Log.d(TAG, "${course_data_live.value}")
        Log.d(TAG, "${user_timetable_data_live.value}")

        val course_textview = requireView().findViewById<TextView>(title_id!!)
        val course_name = course_data?.get("course")
        if (course_name == null)
            course_textview.text = ""
        else
            course_textview.text = course_name.toString()



        val lecture_id = lecture_id_map[week_period]
        //Log.d(TAG, "($week_period) lecture_id: $lecture_id")
        val lecture_textview = requireView().findViewById<TextView>(lecture_id!!)
        val course_lecturer = course_data?.get("lecturer") as List<String>?

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


        val room_id = room_id_map[week_period]
        val room_textview = requireView().findViewById<TextView>(room_id!!)
        val course_room = course_data?.get("room") as? String
        if (course_room == null){
            room_textview.text = ""
        }else{
            room_textview.text = course_room
        }


    }

    private fun week_title(size: Int): LinearLayout {
        val week_title_Linear = LinearLayout(context)
        week_title_Linear.layoutParams = set_timetable_title_layout
        week_title_Linear.gravity = CENTER

        //曜日
        for(index in 0..size){
            val textView = TextView(context)
            textView.textSize = 12f
            textView.gravity = CENTER
            textView.typeface = Typeface.DEFAULT_BOLD
            if(index != 0){
                textView.text = week_to_day_symbol_list_jp_short[index-1]
                textView.layoutParams = set_timetable_course_layout
            }else{
                textView.layoutParams = set_timetable_period_layout
                //textView.setBackgroundColor(Color.RED)
            }

            //textView.setBackgroundResource(R.color.black)

            week_title_Linear.addView(textView)

        }
        return week_title_Linear

    }


    private fun course(week: String, period: Int): LinearLayout {
        val background = LinearLayout(context)
        background.layoutParams = set_timetable_course_layout
        background.setBackgroundResource(R.drawable.timetable_course_background_gray)
        background.orientation = LinearLayout.VERTICAL
        //background.setBackgroundColor(Color.RED)
        background.elevation = 2f

        val bglp: ViewGroup.LayoutParams = background.layoutParams
        val bgmlp = bglp as MarginLayoutParams
        bgmlp.setMargins(4, 4, 4, 4)
        background.layoutParams = bgmlp


        val course = LinearLayout(context)
        //course.setBackgroundResource(R.color.black)
        course.orientation = LinearLayout.VERTICAL
        course.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            5.0f
        )
        course.setBackgroundResource(R.drawable.timetable_course_background)
        //course.elevation = 2f



        val lp: ViewGroup.LayoutParams = course.layoutParams
        val mlp = lp as MarginLayoutParams
        mlp.setMargins(6, 4, 6, 0)
        course.layoutParams = mlp;



        val couse_name_textview = TextView(context)
        couse_name_textview.id = generateViewId()
        couse_name_textview.gravity = CENTER
        couse_name_textview.textSize = 12f
        couse_name_textview.maxLines = 2
        couse_name_textview.maxEms = 10
        couse_name_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            4f
        )

        course_id_map.put("$week$period" , couse_name_textview.id)
        //Log.d(TAG, "course_id_map: ${course_id_map}")



        val teacher_name_textview = TextView(context)
        teacher_name_textview.id = generateViewId()
        teacher_name_textview.gravity = CENTER
        teacher_name_textview.textSize = 10f
        teacher_name_textview.maxLines = 1
        teacher_name_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            1f
        )
        lecture_id_map.put("$week$period" , teacher_name_textview.id)

        val teacher_room_textview = TextView(context)
        teacher_room_textview.id = generateViewId()
        teacher_room_textview.gravity = CENTER
        teacher_room_textview.textSize = 10f
        teacher_room_textview.maxLines = 1
        teacher_room_textview.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            0,
            1f
        )
        room_id_map.put("$week$period" , teacher_room_textview.id)


        //Log.d(TAG, "lecture_id_map: ${lecture_id_map}")

        course.addView(couse_name_textview)
        course.addView(teacher_name_textview)


        course.setOnClickListener {
            viewmodel.get_course_data(week, period, requireContext())
        }

        background.addView(course)
        background.addView(teacher_room_textview)
        return background

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
        Linear.gravity = CENTER
        Linear.layoutParams = set_timetable_Column_layout


        for (week in 0..week_num){
            if (week == 0){

                val textView = TextView(context)
                textView.text = "${period}"
                textView.gravity = CENTER
                textView.layoutParams = set_timetable_period_layout
                //textView.setBackgroundColor(Color.BLUE)
                textView.textSize = 12f
                Linear.addView(textView)


            }else{
                val week_and_period = week_to_day_symbol_list[week]+period.toString()
                val hoge = View(context)
                hoge.layoutParams = set_timetable_Column_space_layout
                //Linear.addView(hoge)
                Linear.addView(course(
                    week_to_day_symbol_list[week], period
                ))
            }
        }
        return Linear
    }



}