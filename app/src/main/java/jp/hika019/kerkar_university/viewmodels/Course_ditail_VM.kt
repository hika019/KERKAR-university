package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.course_data_live
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.util.*
import jp.hika019.kerkar_university.TAG_hoge

class Course_ditail_VM: ViewModel() {

    val week_period = MutableLiveData("")

    val week_and_period_title = MutableLiveData<String>("月曜日1限")
    val course_neme = MutableLiveData("経済基礎")
    val course_room = MutableLiveData("915教室")
    val course_lecturer = MutableLiveData<List<String>>()

    private val TAG = "Course_ditail_VM" + TAG_hoge

    init {
        week_period.asFlow()
            .onEach {
                get_course_data()
            }
            .launchIn(viewModelScope)
    }

    fun get_course_data(){
        Log.d(TAG, "get_course_data -> call")
        val data = course_data_live.value?.get(week_period.value!!) as? Map<String, Any>
        course_neme.value = data?.get("course") as? String
        course_room.value = data?.get("room") as? String
        course_lecturer.value = data?.get("lecturer") as? List<String>

        Log.d(TAG, "course_data: ${data}")

    }


}