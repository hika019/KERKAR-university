package jp.hika019.kerkar_university.viewmodels

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.TAG_hoge
import kotlinx.android.synthetic.main.dialog_add_task.view.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

class Create_task_VM: ViewModel() {

    private val TAG = "Create_task_VM" + TAG_hoge

    val task_title = MutableLiveData("")
    val other = MutableLiveData<String?>(null)
    var create_button_enable = MutableLiveData(false)

    val timelimit_day = MutableLiveData("")
    val timelimit_time = MutableLiveData("")

    init {
        other.asFlow()
            .onEach {
                if (other.value != null)
                    Log.d(TAG, "callll")
                    create_button_enable.value = true
            }
            .launchIn(viewModelScope)
    }

    fun create(){
        Log.d(TAG, "create")
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
            { view, _hour, _minute ->
//                    Log.d("hoge", "time: ${hour} -> get")

                calender.set(2000,1,5,_hour, _minute)
                val dfInputdata = SimpleDateFormat("HH:mm")
                val strInputDate = dfInputdata.format(calender.time)
                timelimit_time.value = strInputDate

                Log.d(TAG, "time: $strInputDate")

            },
            calender.get(Calendar.HOUR_OF_DAY),
            calender.get(Calendar.MINUTE),
            true
        )
        timePicker.show()

    }

}