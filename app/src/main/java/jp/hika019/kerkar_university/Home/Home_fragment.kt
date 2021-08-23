package jp.hika019.kerkar_university.Home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.hika019.kerkar_university.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.item_home_activity_taimetable.view.*
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*

class Home_fragment(): Fragment() {

    private val TAG = "Home_fragment"

    val calendar: Calendar = Calendar.getInstance()
    val now_week_to_day = week_to_day_symbol_list[calendar.get(Calendar.DAY_OF_WEEK)-1]

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)
        runBlocking {
            try{
                load_timetable(view)
                timetable_onclick_event(view)
                load_task(view)
                firedb_timetable(view.context).courses_is_none()
            }catch (e: Exception){
                Log.w(TAG, "start -> error")
            }
        }



        view.floatingActionButton.setOnClickListener {
            context?.let { it1 -> firedb_task(it1).get_course_list() }
        }

        return view
    }



    private fun load_timetable(view: View){
        Log.d(TAG, "load_timetable -> call")
        val user_doc = firedb.collection("user")
                .document(uid!!)

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
                                                timetable_data_map?.put(week_to_day, tmp_data["course_id"] as String)
                                            }
                                        }
                                    }

                                    /*
                                    view.today_first_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+1)
                                    view.today_second_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+2)
                                    view.today_third_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+3)
                                    view.today_fourth_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+4)
                                    view.today_fifth_period.timetable_title_textView.text = timetable_data_map?.get(now_week_to_day+5)
                                     */
                                }
                    }else{
                        Log.w(TAG, "semester is null")
                    }

                }
                .addOnFailureListener {
                    Log.w(TAG, "load_timetable: get semester -> failure")
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

    private fun load_task(view: View){
        context?.let { firedb_task(it).get_tomorrow_not_comp_task_list(view) }
    }

}