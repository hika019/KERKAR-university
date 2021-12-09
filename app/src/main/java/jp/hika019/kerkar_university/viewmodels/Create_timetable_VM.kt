package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.util.*
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Create_timetable_VM: ViewModel() {

    private val TAG = "Create_timetable_VM" + tagHoge

    val timetable_name = MutableLiveData("")
    val select_year = MutableLiveData<String>("選択してください")
    val create_button_enable = MutableLiveData(false)
    val sat_check = MutableLiveData<Boolean>(false)
    val radioGroup = MutableLiveData(R.id.term_radioGroup)
    val term = MutableLiveData(-1)
    val period = MutableLiveData(5)

    val create_button_bg = MutableLiveData(R.drawable.setup_button_bg_false)


    init {
        listOf(timetable_name, select_year, term).forEach {
            it.asFlow()
                .onEach {
                    check()
                }
                .launchIn(viewModelScope)
        }

    }




    fun select_year(context: Context){
        Log.d(TAG, "select_year -> call")
        val now_year = SimpleDateFormat("yyyy").format(Date()).toInt()
        val year_list = Array(4){(now_year-2+it).toString()}
        var select_index: Int? = null

        val dialog = AlertDialog.Builder(context)
            .setTitle("年の選択")
            .setSingleChoiceItems(year_list, -1){ dialog, which ->
                select_index = which
            }
            .setPositiveButton("確定"){ dialog, which ->
                if(select_index !=null)
                    select_year.value = year_list[select_index!!]

            }
        dialog.show()
    }

    fun check(){
        if (term.value != -1 && select_year.value != "選択してください" && timetable_name.value !="")
            create_button_enable.value = true
    }

    fun create_timetable(context: Context){
        Log.d(TAG, "create_timetable -> call")

        var week = 5

        if (sat_check.value == true)
            week = 6

        val doc_id = firedb.collection("user")
            .document(uid!!)
            .collection("timetable")
            .document().id

        val tt_data = mapOf(
            "wtd" to week,
            "year" to select_year.value!!.toInt(),
            "term" to term.value!!+1,
            "period" to period.value,
            "timetable_id" to doc_id,
            "timetable_name" to timetable_name.value
        )
        var sattmp ="土曜日を表示しない"
        if (week == 6)
            sattmp = "土曜日を表示する"

        val message = "時間割名: ${timetable_name.value}\n" +
                "年: ${select_year.value}\n" +
                "学期: ${term.value!!+1}\n" +
                "${period.value}限まで\n" +
                sattmp

        AlertDialog.Builder(context)
            .setTitle("確認")
            .setMessage(message)
            .setPositiveButton("作成する"){_, _ ->
                Log.d(TAG, "data: ${tt_data}")

                val firedb = FiredbTimetableNew()
                firedb.createTimetable(context, tt_data)
            }
            .setNegativeButton("キャンセル"){_, _ ->
                false
            }
            .create().show()

    }

}