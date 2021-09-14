package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import jp.hika019.kerkar_university.course_data_map_to_str
import jp.hika019.kerkar_university.user_timetable_data_live
import jp.hika019.kerkar_university.week_to_day_jp_chenger

class Timetable_VM: ViewModel() {
    val timetable = MutableLiveData<Map<String, Any>>()

    val timetable_semester = MutableLiveData("")
    var context: Context? =null

    init {

    }

    fun get_course_data(week: String, period: Int, _context: Context){
        val timetable_map = user_timetable_data_live.value
        context = _context
        var message = ""

        val data = timetable_map?.get("$week+$period")
        if (data != null) {
            message = course_data_map_to_str(data as Map<String, Any>)

        }
        show_course_info(week, period, message)
    }

    fun show_course_info(week: String, period: Int, message: String){


        val week_jp = week_to_day_jp_chenger(week)

        val dialog = AlertDialog.Builder(context!!)
            .setTitle("${week_jp}曜日 ${period}限の授業")
            .setMessage(message)
            .setPositiveButton("授業登録") { dialog, which ->
            }
            .setNegativeButton("戻る"){ dialog, which ->

            }
//                .setNeutralButton("課題を確認") { dialog, which ->
//
//                }
        dialog.show()
    }


}