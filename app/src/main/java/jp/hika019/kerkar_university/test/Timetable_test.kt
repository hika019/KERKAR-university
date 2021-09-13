package jp.hika019.kerkar_university.test

import android.content.Context
import jp.hika019.kerkar_university.firedb
import jp.hika019.kerkar_university.get_timetable_id
import jp.hika019.kerkar_university.uid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

object Timetable_test{

    private var a_course_id = MutableStateFlow<String?>("1")

    fun set_course_id(): String? {
        var tmp = a_course_id.value?.toInt()
        if (tmp != null) {
            tmp = tmp+1
        }

        return tmp.toString()
    }

}