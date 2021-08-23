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

        user_doc.addSnapshotListener { value, error ->
            if(error != null){
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }
            if(value != null){
                semester = value.getString("semester")

                if (semester != null) {
                    user_doc.collection("semester")
                        .document(semester!!)
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error)
                                return@addSnapshotListener
                            }
                            Log.d(TAG, "get_course_symbol -> success")


                            //取得
                            for (period in period_list) {

                                val week_to_day = now_week_to_day + period.toString()
                                val tmp_data = value?.get(week_to_day) as Map<String?, Any?>?
                                val course_id = tmp_data?.get("course_id") as? String
                                var course_name = ""
                                //Log.d(TAG, period.toString()+" "+course_id.toString())
                                show_timetable(view, period, course_name)
                                if (course_id != null) {
                                    firedb.collection("university")
                                        .document(university_id!!)
                                        .collection("semester")
                                        .document(semester!!)
                                        .collection(week_to_day)
                                        .document(course_id)
                                        .get()
                                        .addOnSuccessListener {
                                            val data = it.data
                                            course_name = data?.get("course").toString()
                                            Log.d(TAG, week_to_day+": "+course_name)
                                            show_timetable(view, period, course_name)

                                        }
                                }
                                //Log.d(TAG, "show: "+week_to_day)
                                show_timetable(view, period, course_name)

                            }

                        }
                }else Log.w(TAG, "semester is null")
            }
        }
    }

    private fun show_timetable(view: View, period: Int, course_name: String){
        when(period){
            1 -> view.today_first_period.timetable_title_textView.text = course_name
            2 -> view.today_second_period.timetable_title_textView.text = course_name
            3 -> view.today_third_period.timetable_title_textView.text = course_name
            4 -> view.today_fourth_period.timetable_title_textView.text = course_name
            5 -> view.today_fifth_period.timetable_title_textView.text = course_name
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