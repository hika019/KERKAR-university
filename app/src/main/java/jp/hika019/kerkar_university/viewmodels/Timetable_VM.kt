package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.util.*
import jp.hika019.kerkar_university.Course_detail.Course_detail_Activity
import jp.hika019.kerkar_university.SelectTimetable.Select_timetableActivity
import jp.hika019.kerkar_university.select_course.Select_course_Activity

class Timetable_VM: ViewModel() {
    private val TAG = "Timetable_VM"+ tagHoge
    val timetable = MutableLiveData<Map<String, Any>>()

    val timetable_name = MutableLiveData("")
    var context: Context? =null

    private val firedb_tt_class = FiredbTimetableNew()

    init {
        Log.d(TAG, "Timetable_VM / init -> call")
        //Log.d(TAG, "user_timetable_data_live: ${user_timetable_data_live.value}")

        timetable_id.asFlow()
            .onEach {
                user_timetable_data_live.asFlow()
                    .onEach {
                        get_timetable_name()
                        course_data_live.value = (null)

                        for (week in week_to_day_symbol_list){
                            for (period in period_list){
                                firedb_tt_class.get_user_course_data(week, period)
                            }
                        }
                    }
                    .launchIn(viewModelScope)
            }
            .launchIn(viewModelScope)

    }

    fun get_timetable_name(){
        Log.d(TAG, "get_timetable_name -> call")
        timetable_name.value = user_timetable_data_live.value?.get("timetable_name") as String
        Log.d(TAG, "timetable_name.value: ${timetable_name.value}")
    }

    fun get_course_data(week: String, period: Int, _context: Context){
        Log.d(TAG, "get_course_data -> call")
        context = _context
        var message: String? = null

        val data = course_data_live.value?.get("$week$period")
        Log.d(TAG, "data: ${data}")

        if (data != null) {
            message = courseDataMapToStr(data as Map<String, Any>)
        }

        Log.d(TAG, "mess: $message")
        if (message.isNullOrEmpty()){
            val i = Intent(context, Select_course_Activity::class.java)
            i.putExtra("week_period", arrayOf<String>(week, period.toString()))
            context!!.startActivity(i)

        }else{
            //授業の詳細
            val i = Intent(context, Course_detail_Activity::class.java)
            i.putExtra("week_period", arrayOf<String>(week, period.toString()))
            context!!.startActivity(i)


        }
    }

    fun show_course_info(week: String, period: Int, message: String){
        Log.d(TAG, "show_course_info -> call")

        val week_jp = weekToDayJpChenger(week)

        val dialog = AlertDialog.Builder(context!!)
            .setTitle("${week_jp}曜日 ${period}限の授業")
            .setMessage(message)
            .setPositiveButton("授業登録") { dialog, which ->
                val firedb_tt_old_class = firedb_timetable(context!!)
                firedb_tt_old_class.getCourseList(week, period)
            }
            .setNegativeButton("戻る"){ dialog, which ->

            }
//                .setNeutralButton("課題を確認") { dialog, which ->
//
//                }
//        val tmp = dialog.create()
//        val lp = tmp.window?.attributes
//        lp?.gravity = Gravity.BOTTOM
//        tmp.show()

        dialog.create().show()
    }

    fun setting_timetable(context: Context){
        val i = Intent(context, Select_timetableActivity::class.java)
        context.startActivity(i)
    }




}