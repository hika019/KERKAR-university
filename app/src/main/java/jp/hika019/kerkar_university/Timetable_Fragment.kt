package jp.hika019.kerkar_university

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_timetable.view.*
import kotlinx.android.synthetic.main.item_timetable.view.*
import kotlinx.coroutines.runBlocking

class Timetable_Fragment(): Fragment() {
    val TAG = "Timetable_Fragment"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_timetable, container, false)

        val firedb_semester = firedb_semester(view.context, view)

        firedb_semester.get_semester_title()

        load_timetable(view)

        timetable_onclick_event(view)


        view.semester_select_button.setOnClickListener{
            Log.d(TAG, "semester_select_button -> push")
            val firedb_semester = firedb_semester(view.context, view)
            firedb_semester.get_semester_list()
        }

        return view
    }

    private fun load_timetable(view: View){
        val period_list:List<Int> = List(5){it +1}



        val user_doc = firedb.collection("user")
                .document(uid!!)


        user_doc.addSnapshotListener { value, error ->
            if(error != null){
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }
            if(value != null){
                semester = value.getString("semester")

                val university_id = value.getString("university_id")

                if (semester != null) {
                    user_doc.collection("semester")
                            .document(semester!!)
                            .addSnapshotListener { value, error ->
                                if (error != null) {
                                    Log.w(TAG, "Listen failed.", error)
                                    return@addSnapshotListener
                                }
                                Log.d(TAG, "get_course_symbol -> success")

                                var timetable_data_map: MutableMap<String, String>? = mutableMapOf()

                                //取得
                                for (week in week_to_day_symbol_list) {
                                    for (period in period_list) {

                                        val week_to_day = week + period.toString()
                                        val tmp_data = value?.get(week_to_day) as Map<String?, Any?>?
                                        val course_id = tmp_data?.get("course_id") as? String
                                        var course_name = ""
                                        //Log.d(TAG, course_id.toString())
                                        show_timetable(view, week_to_day, course_name)
                                        if (course_id != null) {
                                            runBlocking {
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
                                                        show_timetable(view, week_to_day, course_name)

                                                    }
                                            }
                                        }
                                        //Log.d(TAG, "show: "+week_to_day)
                                        show_timetable(view, week_to_day, course_name)

                                    }
                                }

                            }

                }
            }
        }

    }

    private fun show_timetable(view: View, week_to_day: String, course_name: String){
        when(week_to_day){
            "mon1" -> view.timetable_include_mon1.timetable_title_textView.text = course_name
            "mon2" -> view.timetable_include_mon2.timetable_title_textView.text = course_name
            "mon3" -> view.timetable_include_mon3.timetable_title_textView.text = course_name
            "mon4" -> view.timetable_include_mon4.timetable_title_textView.text = course_name
            "mon5" -> view.timetable_include_mon5.timetable_title_textView.text = course_name

            "tue1" -> view.timetable_include_tue1.timetable_title_textView.text = course_name
            "tue2" -> view.timetable_include_tue2.timetable_title_textView.text = course_name
            "tue3" -> view.timetable_include_tue3.timetable_title_textView.text = course_name
            "tue4" -> view.timetable_include_tue4.timetable_title_textView.text = course_name
            "tue5" -> view.timetable_include_tue5.timetable_title_textView.text = course_name

            "wed1" -> view.timetable_include_wed1.timetable_title_textView.text = course_name
            "wed2" -> view.timetable_include_wed2.timetable_title_textView.text = course_name
            "wed3" -> view.timetable_include_wed3.timetable_title_textView.text = course_name
            "wed4" -> view.timetable_include_wed4.timetable_title_textView.text = course_name
            "wed5" -> view.timetable_include_wed5.timetable_title_textView.text = course_name

            "thu1" -> view.timetable_include_thu1.timetable_title_textView.text = course_name
            "thu2" -> view.timetable_include_thu2.timetable_title_textView.text = course_name
            "thu3" -> view.timetable_include_thu3.timetable_title_textView.text = course_name
            "thu4" -> view.timetable_include_thu4.timetable_title_textView.text = course_name
            "thu5" -> view.timetable_include_thu5.timetable_title_textView.text = course_name


            "fri1" -> view.timetable_include_fri1.timetable_title_textView.text = course_name
            "fri2" -> view.timetable_include_fri2.timetable_title_textView.text = course_name
            "fri3" -> view.timetable_include_fri3.timetable_title_textView.text = course_name
            "fri4" -> view.timetable_include_fri4.timetable_title_textView.text = course_name
            "fri5" -> view.timetable_include_fri5.timetable_title_textView.text = course_name

        }
    }


    private fun timetable_onclick_event(view: View){

        val firedb_timetable = context?.let { firedb_timetable(it) }

        //月
        view.timetable_include_mon1.setOnClickListener{
            firedb_timetable?.get_course_data("mon", 1)
        }
        view.timetable_include_mon2.setOnClickListener{
            firedb_timetable?.get_course_data("mon", 2)
        }
        view.timetable_include_mon3.setOnClickListener{
            firedb_timetable?.get_course_data("mon", 3)
        }
        view.timetable_include_mon4.setOnClickListener{
            firedb_timetable?.get_course_data("mon", 4)
        }
        view.timetable_include_mon5.setOnClickListener{
            firedb_timetable?.get_course_data("mon", 5)
        }

        //火
        view.timetable_include_tue1.setOnClickListener{
            firedb_timetable?.get_course_data("tue", 1)
        }
        view.timetable_include_tue2.setOnClickListener{
            firedb_timetable?.get_course_data("tue", 2)
        }
        view.timetable_include_tue3.setOnClickListener{
            firedb_timetable?.get_course_data("tue", 3)
        }
        view.timetable_include_tue4.setOnClickListener{
            firedb_timetable?.get_course_data("tue", 4)
        }
        view.timetable_include_tue5.setOnClickListener{
            firedb_timetable?.get_course_data("tue", 5)
        }

        //水
        view.timetable_include_wed1.setOnClickListener{
            firedb_timetable?.get_course_data("wed", 1)
        }
        view.timetable_include_wed2.setOnClickListener{
            firedb_timetable?.get_course_data("wed", 2)
        }
        view.timetable_include_wed3.setOnClickListener{
            firedb_timetable?.get_course_data("wed", 3)
        }
        view.timetable_include_wed4.setOnClickListener{
            firedb_timetable?.get_course_data("wed", 4)
        }
        view.timetable_include_wed5.setOnClickListener{
            firedb_timetable?.get_course_data("wed", 5)
        }

        //木
        view.timetable_include_thu1.setOnClickListener{
            firedb_timetable?.get_course_data("thu", 1)
        }
        view.timetable_include_thu2.setOnClickListener{
            firedb_timetable?.get_course_data("thu", 2)
        }
        view.timetable_include_thu3.setOnClickListener{
            firedb_timetable?.get_course_data("thu", 3)
        }
        view.timetable_include_thu4.setOnClickListener{
            firedb_timetable?.get_course_data("thu", 4)
        }
        view.timetable_include_thu5.setOnClickListener{
            firedb_timetable?.get_course_data("thu", 5)
        }

        //金
        view.timetable_include_fri1.setOnClickListener{
            firedb_timetable?.get_course_data("fri", 1)
        }
        view.timetable_include_fri2.setOnClickListener{
            firedb_timetable?.get_course_data("fri", 2)
        }
        view.timetable_include_fri3.setOnClickListener{
            firedb_timetable?.get_course_data("fri", 3)
        }
        view.timetable_include_fri4.setOnClickListener{
            firedb_timetable?.get_course_data("fri", 4)
        }
        view.timetable_include_fri5.setOnClickListener{
            firedb_timetable?.get_course_data("fri", 5)
        }



    }
}