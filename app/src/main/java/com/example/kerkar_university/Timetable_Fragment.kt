package com.example.kerkar_university

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_timetable.view.*
import kotlinx.android.synthetic.main.item_timetable.view.*

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
        val week_to_day_symbol_list = listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")
        val period_list:List<Int> = List(5){it +1}



        if(login_cheack() == true){
            val uid = get_uid()

            val user_doc = firedb.collection("user")
                    .document(uid)


            user_doc.addSnapshotListener { value, error ->
                if(error != null){
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }
                if(value != null){
                    val semester = value.getString("semester")

                    if (semester != null) {
                        user_doc.collection("semester")
                                .document(semester)
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
                                            Log.d("hoge", "tmp_data: ${tmp_data}")

                                            if (tmp_data != null) {
                                                timetable_data_map?.put(week_to_day, tmp_data["course"] as String)
                                            }
                                        }
                                    }

                                    //表示
                                    view.timetable_include_mon1.timetable_title_textView.text = timetable_data_map?.get("mon1")
                                    view.timetable_include_mon2.timetable_title_textView.text = timetable_data_map?.get("mon2")
                                    view.timetable_include_mon3.timetable_title_textView.text = timetable_data_map?.get("mon3")
                                    view.timetable_include_mon4.timetable_title_textView.text = timetable_data_map?.get("mon4")
                                    view.timetable_include_mon5.timetable_title_textView.text = timetable_data_map?.get("mon5")

                                    view.timetable_include_tue1.timetable_title_textView.text = timetable_data_map?.get("tue1")
                                    view.timetable_include_tue2.timetable_title_textView.text = timetable_data_map?.get("tue2")
                                    view.timetable_include_tue3.timetable_title_textView.text = timetable_data_map?.get("tue3")
                                    view.timetable_include_tue4.timetable_title_textView.text = timetable_data_map?.get("tue4")
                                    view.timetable_include_tue5.timetable_title_textView.text = timetable_data_map?.get("tue5")

                                    view.timetable_include_wen1.timetable_title_textView.text =
                                            timetable_data_map?.get("wed1")
                                    view.timetable_include_wen2.timetable_title_textView.text =
                                            timetable_data_map?.get("wed2")
                                    view.timetable_include_wen3.timetable_title_textView.text =
                                            timetable_data_map?.get("wed3")
                                    view.timetable_include_wen4.timetable_title_textView.text =
                                            timetable_data_map?.get("wed4")
                                    view.timetable_include_wen5.timetable_title_textView.text =
                                            timetable_data_map?.get("wed5")

                                    view.timetable_include_thu1.timetable_title_textView.text =
                                            timetable_data_map?.get("thu1")
                                    view.timetable_include_thu2.timetable_title_textView.text =
                                            timetable_data_map?.get("thu2")
                                    view.timetable_include_thu3.timetable_title_textView.text =
                                            timetable_data_map?.get("thu3")
                                    view.timetable_include_thu4.timetable_title_textView.text =
                                            timetable_data_map?.get("thu4")
                                    view.timetable_include_thu5.timetable_title_textView.text =
                                            timetable_data_map?.get("thu5")

                                    view.timetable_include_fri1.timetable_title_textView.text =
                                            timetable_data_map?.get("fri1")
                                    view.timetable_include_fri2.timetable_title_textView.text =
                                            timetable_data_map?.get("fri2")
                                    view.timetable_include_fri3.timetable_title_textView.text =
                                            timetable_data_map?.get("fri3")
                                    view.timetable_include_fri4.timetable_title_textView.text =
                                            timetable_data_map?.get("fri4")
                                    view.timetable_include_fri5.timetable_title_textView.text =
                                            timetable_data_map?.get("fri5")

                                }
                    }
                }
            }
        }
    }



    private fun timetable_onclick_event(view: View){

        val firedb_timetable = context?.let { firedb_timetable(it) }


        view.timetable_include_mon1.setOnClickListener{
            firedb_timetable?.get_course_data("mon", 1)
        }
        view.timetable_include_mon2.setOnClickListener{
            firedb_timetable?.get_course_data("mon", 2)
        }

    }
}