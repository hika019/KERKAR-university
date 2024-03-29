package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import android.util.*
import jp.hika019.kerkar_university.*

class Create_courceVM: ViewModel() {
    private val TAG = "Create_courceVM" + tagHoge

    var week_and_period = MutableLiveData("")
    val course_name = MutableLiveData<String>("")
    val course_room = MutableLiveData<String>("")
    val course_lecture = MutableLiveData<String>("")
    var course_lecture_list = MutableLiveData<ArrayList<String>>()
    val _course_lecture_list = arrayListOf<String>()

    val createbutton_enable = MutableLiveData(false)

    val finish_event = MutableLiveData(false)

    init {
        val week_jp = weekToDayJpChenger(createCourceWtd)


        week_and_period.value = "${week_jp}曜日 ${createCourcePeriod}限"

        listOf(course_name, course_room, course_lecture_list, course_lecture).forEach {
            it.asFlow()
                .onEach {
                    check_room_str()
                    check_name_str()
                    check_lecture_str()
                    CreateButton_to_true()
                }
                .launchIn(viewModelScope)
        }
    }

    fun check_room_str(){
        val hoge = course_room.value?.takeLast(1)
        if (hoge.equals("\n")){
            course_room.value = course_room.value?.substring(0, course_room.value!!.length-1)
        }
    }

    fun check_name_str(){
        val hoge = course_name.value?.takeLast(1)
        if (hoge.equals("\n")){
            course_name.value = course_name.value?.substring(0, course_name.value!!.length-1);
        }
    }

    fun check_lecture_str(){
        val hoge = course_lecture.value?.takeLast(1)
        if (hoge.equals("\n")){
            course_lecture.value = course_lecture.value?.substring(0, course_lecture.value!!.length-1);
        }
    }

    fun CreateButton_to_true(){

        if (course_name.value != "" && course_room.value != "" &&(!_course_lecture_list.isEmpty() || course_lecture.value != ""))
            createbutton_enable.value = true

    }

    fun add_lecturer(){

        if(course_lecture.value != ""){
            _course_lecture_list += course_lecture.value!!
            course_lecture.value = ""
        }
        course_lecture_list.value = _course_lecture_list
        Log.d(TAG, "${_course_lecture_list}")
    }

    fun create_couce(context: Context){
        add_lecturer()

        val data = mapOf<String, Any>(
            "semester_id" to semester!!,
            "week_to_day" to (createCourceWtd + createCourcePeriod),
            "course" to course_name.value!!,
            "lecturer" to _course_lecture_list,
            "room" to course_room.value!!
        )
        val hoge = firedb_timetable(context)
        hoge.createCourseUniversityTimetable(data)
        finish()
    }

    fun finish(){
        finish_event.value = true
    }
}