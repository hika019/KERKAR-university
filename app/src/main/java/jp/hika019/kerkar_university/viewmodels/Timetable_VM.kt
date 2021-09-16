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

class Timetable_VM: ViewModel() {
    private val TAG = "Timetable_VM"+ TAG_hoge
    val timetable = MutableLiveData<Map<String, Any>>()

    val timetable_name = MutableLiveData("")
    var context: Context? =null
    var course_data = MutableLiveData<MutableMap<String, Map<String, Any>?>>()

    private val firedb_tt_class = firedb_timetable_new()

    init {
        Log.d(TAG, "Timetable_VM / init -> call")
        //Log.d(TAG, "user_timetable_data_live: ${user_timetable_data_live.value}")


        user_timetable_data_live.asFlow()
            .onEach {
                get_timetable_name()
                for (week in week_to_day_symbol_list){
                    for (period in period_list){
                        firedb_tt_class.get_user_course_data(week, period)
                    }
                }
            }
            .launchIn(viewModelScope)

        course_data_live.asFlow()
            .onEach {
                course_data_live_to_course_data()
            }
            .launchIn(viewModelScope)

    }

    fun get_timetable_name(){
        Log.d(TAG, "get_timetable_name -> call")
        timetable_name.value = user_timetable_data_live.value?.get("timetable_name") as String
        Log.d(TAG, "timetable_name.value: ${timetable_name.value}")
    }

    fun course_data_live_to_course_data(){
        Log.d(TAG, "course_data_live_to_course_data -> call")
        course_data.value = course_data_live.value as MutableMap<String, Map<String, Any>?>
        Log.d(TAG, "course_data: ${course_data.value}")
    }

    fun get_course_data(week: String, period: Int, _context: Context){
        context = _context
        var message = ""

        val data = course_data.value?.get("$week$period")
        Log.d(TAG, "data: ${data}")
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
                val firedb_tt_old_class = firedb_timetable(context!!)
                firedb_tt_old_class.get_course_list(week, period)
            }
            .setNegativeButton("戻る"){ dialog, which ->

            }
//                .setNeutralButton("課題を確認") { dialog, which ->
//
//                }
        dialog.show()
    }


}