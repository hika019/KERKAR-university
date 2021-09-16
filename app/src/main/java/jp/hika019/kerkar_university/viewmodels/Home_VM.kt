package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.util.*

class Home_VM: ViewModel() {
    private val TAG = "Home_VM" + TAG_hoge
    var course_data = MutableLiveData<MutableMap<String, Map<String, Any>?>>()

    var period = MutableLiveData<Int>(6)

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
    }

    fun get_period(){
        val tmp = user_timetable_data_live.value?.get("period") as Long
        this.period.value = tmp.toInt()
    }



}