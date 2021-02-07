package com.example.kerkar_university.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kerkar_university.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.item_home_activity_taimetable.view.*
import java.util.*

class Home_fragment(): Fragment() {

    private val TAG = "Home_fragment"

    val week_to_day_symbol_list = listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")
    val period_list:List<Int> = List(5){it +1}

    val calendar: Calendar = Calendar.getInstance()
    val now_week_to_day = week_to_day_symbol_list[calendar.get(Calendar.DAY_OF_WEEK)-1]


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)

        load_timetable(view)
        timetable_onclick_event(view)

        view.floatingActionButton.setOnClickListener {
            context?.let { it1 -> firedb_task(it1).get_course_list() }
        }

        return view
    }

    private fun load_timetable(view: View){


        if(login_cheack()){

            val uid = get_uid()
            val user_doc = firedb.collection("user")
                    .document(uid)

            user_doc.get()
                    .addOnSuccessListener {
                        val semester = it.getString("semester")
                        Log.d(TAG, "load_timetable: get semester -> success")

                        if(semester != null){
                            user_doc.collection("semester")
                                    .document(semester)
                                    .addSnapshotListener { value, error ->
                                        if(error != null){
                                            Log.w(TAG, "Home_fragment -> load_timetable -> get timetable -> failed", error)
                                            return@addSnapshotListener
                                        }

                                        Log.d(TAG, "get_course_data -> success")

                                        var timetable_data_map: MutableMap<String, String>? = mutableMapOf()

                                        for (week in week_to_day_symbol_list) {
                                            for (period in period_list) {
                                                val week_to_day = week + period.toString()
                                                val tmp_data = value?.get(week_to_day) as Map<String?, Any?>?
//                                                Log.d("hoge", "tmp_data: ${tmp_data}")

                                                if (tmp_data != null) {
                                                    timetable_data_map?.put(week_to_day, tmp_data["course"] as String)
                                                }
                                            }
                                        }


                                        view.today_first_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+1)
                                        view.today_second_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+2)
                                        view.today_third_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+3)
                                        view.today_fourth_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+4)
                                        view.today_fifth_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+5)
                                    }
                        }

                    }
                    .addOnFailureListener {
                        Log.d(TAG, "load_timetable: get semester -> failure")
                    }
        }else{
            Log.d(TAG, "load_timetable: not login")
        }
    }

    private fun timetable_onclick_event(view: View){
        val firedb_timetable = context?.let { firedb_timetable(it) }

        view.today_first_period.setOnClickListener {
            firedb_timetable?.get_course_data(now_week_to_day, 1)
        }
        view.today_second_period.setOnClickListener {
            firedb_timetable?.get_course_data(now_week_to_day, 2)
        }
        view.today_third_period.setOnClickListener {
            firedb_timetable?.get_course_data(now_week_to_day, 3)
        }
        view.today_fourth_period.setOnClickListener {
            firedb_timetable?.get_course_data(now_week_to_day, 4)
        }
        view.today_fifth_period.setOnClickListener {
            firedb_timetable?.get_course_data(now_week_to_day, 5)
        }

    }
}