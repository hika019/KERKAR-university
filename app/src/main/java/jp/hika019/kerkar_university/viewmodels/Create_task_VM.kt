package jp.hika019.kerkar_university.viewmodels

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

class Create_task_VM: ViewModel() {

    private val TAG = "Create_task_VM" + TAG_hoge

    val course_name = MutableLiveData("")
    val task_title = MutableLiveData("")
    val other = MutableLiveData<String>("")
    var create_button_enable = MutableLiveData(false)

    val timelimit_day = MutableLiveData("")
    val timelimit_time = MutableLiveData("")

    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    var hour: Int? = null
    var minutes: Int? = null


    var week_period: String? = (null)

    init {
        listOf(other, task_title, timelimit_day, timelimit_time).forEach {
            it.asFlow()
            .onEach {
                check()
            }
            .launchIn(viewModelScope)
        }
    }

    fun get_course_name(){
        Log.d(TAG, "get_course_name -> call")
        val data = course_data_live.value?.get(week_period) as? Map<String, Any>
        val hoge = data?.get("course") as? String
        course_name.value = hoge
    }

    fun create(context: Context){
        Log.d(TAG, "create -> call")
        val calendar = Calendar.getInstance()
        calendar.set(year!!,month!!,day!!,hour!!, minutes!!)

        val course_data = user_timetable_data_live.value?.get(week_period) as Map<String, Any?>
        val course_id = course_data["course_id"]

        val data = mutableMapOf(
            "time_limit" to Timestamp(calendar.time),
            "task_name" to task_title.value,
            "note" to other.value
        )
        Log.d(TAG, "create task data: $data")
        Log.d(TAG, "id :$course_id")
        val hoge = firedb_task_new()
        hoge.create_task(context, week_period!!, course_id.toString(), data as MutableMap<String, Any?>)
    }

    fun check(){
        Log.d(TAG, "check -> call")
        Log.d(TAG, "${timelimit_day.value}")
        if (task_title.value != "" &&
            timelimit_day.value!!.isNotEmpty() && timelimit_time.value!!.isNotEmpty()){
            create_button_enable.value = true
        }
    }

    fun select_day(_context: Context){
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(_context,
            { view, _year, _month, _day -> //setした日付を取得して表示
                calendar.set(_year, _month, _day)
                val dfInputeDate = SimpleDateFormat("yyyy/MM/dd")
                val strInputDate = dfInputeDate.format(calendar.time)
//                    Log.d("hoge", "time: ${calendar.time} -> show")
                timelimit_day.value = strInputDate
                Log.d(TAG, "day: $_year/$_month/$_day -> set")
                year = _year
                month = _month
                day = _day
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )
        datePickerDialog.show()
    }

    fun select_time(_context: Context){
        val calender = Calendar.getInstance()

        val timePicker = TimePickerDialog(_context,
            { view, _hour, _minutes ->
                calender.set(2000,1,5,_hour, _minutes)
                val dfInputdata = SimpleDateFormat("HH:mm")
                val strInputDate = dfInputdata.format(calender.time)
                timelimit_time.value = strInputDate

                Log.d(TAG, "time: ${timelimit_time.value}")
                hour = _hour
                minutes = _minutes
            },
            calender.get(Calendar.HOUR_OF_DAY),
            calender.get(Calendar.MINUTE),
            true
        )
        timePicker.show()

    }

}