package jp.hika019.kerkar_university

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
        val period_list:List<Int> = List(5){it +1}



        val user_doc = firedb.collection("user")
                .document(uid!!)


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
//                                            Log.d("hoge", "tmp_data: ${tmp_data}")

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

                                view.timetable_include_wed1.timetable_title_textView.text = timetable_data_map?.get("wed1")
                                view.timetable_include_wed2.timetable_title_textView.text = timetable_data_map?.get("wed2")
                                view.timetable_include_wed3.timetable_title_textView.text = timetable_data_map?.get("wed3")
                                view.timetable_include_wed4.timetable_title_textView.text = timetable_data_map?.get("wed4")
                                view.timetable_include_wed5.timetable_title_textView.text = timetable_data_map?.get("wed5")

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