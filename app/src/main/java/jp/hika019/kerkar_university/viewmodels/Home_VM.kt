package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.util.*
import androidx.appcompat.app.AlertDialog
import jp.hika019.kerkar_university.Course_detail.Course_detail_Activity
import jp.hika019.kerkar_university.select_course.Select_course_Activity

class Home_VM: ViewModel() {
    private val TAG = "Home_VM" + TAG_hoge
    var course_data = MutableLiveData<MutableMap<String, Map<String, Any>?>>()

    var period = MutableLiveData<Int>(6)

    var context: Context? = null

    private val firedb_tt_class = firedb_timetable_new()

    init{
        Log.d(TAG, "Home_VM / init -> call")

        user_timetable_data_live.asFlow()
            .onEach {
                for (week in week_to_day_symbol_list){
                    for (period in period_list){
                        firedb_tt_class.get_user_course_data(week, period)
                    }
                }
                get_period()
            }
            .launchIn(viewModelScope)

        course_data_live.asFlow()
            .onEach {
                course_data_live_to_course_data()
            }
            .launchIn(viewModelScope)
    }


    fun get_period(){
        val tmp = user_timetable_data_live.value?.get("period") as Long
        this.period.value = tmp.toInt()
    }

    fun course_data_live_to_course_data(){
        Log.d(TAG, "course_data_live_to_course_data -> call")
        course_data.value = course_data_live.value as? MutableMap<String, Map<String, Any>?>
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