package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.util.*
import android.widget.Toast
import jp.hika019.kerkar_university.*

class Course_ditail_VM: ViewModel() {

    val week_period = MutableLiveData("")
    var course_id: String? = ""
    val week_and_period_title = MutableLiveData<String>("月曜日1限")
    val course_neme = MutableLiveData("経済基礎")
    val course_room = MutableLiveData("915教室")
    val course_lecturer = MutableLiveData<List<String>>()

    private var context: Context? = null

    private val TAG = "Course_ditail_VM" + tagHoge

    init {
        week_period.asFlow()
            .onEach {
                if (!week_period.value.equals(""))
                    get_course_data()
            }
            .launchIn(viewModelScope)

    }

    fun get_course_data(){
        Log.d(TAG, "get_course_data -> call")
        val data = course_data_live.value?.get(week_period.value!!) as? Map<String, Any>
        Log.d(TAG, "data: ${course_data_live.value}")
        val course_datas = user_timetable_data_live.value?.get(week_period.value!!) as? Map<String, Any?>
        course_id = course_datas?.get("course_id") as? String
        course_neme.value = data?.get("course") as? String
        course_room.value = data?.get("room") as? String
        course_lecturer.value = data?.get("lecturer") as? List<String>

        Log.d(TAG, "course_data: ${course_datas}")

    }

    fun delete_course(_context: Context){
        context = _context
        Log.d(TAG, "delete_course(${week_period.value}) -> call")
        val hoge = firedb_timetable(context!!)
        hoge.deleteUserTimetable(week_period.value!!)

    }

    fun change_color(_context: Context){
        Toast.makeText(_context, "未実装機能です", Toast.LENGTH_SHORT).show()
    }




}