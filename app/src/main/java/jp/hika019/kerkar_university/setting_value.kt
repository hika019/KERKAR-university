package jp.hika019.kerkar_university

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val week_to_day_jp_list = listOf("日", "月", "火", "水", "木", "金", "土")

val week_to_day_symbol_list_jp_short = listOf("月", "火", "水", "木", "金", "土")

val week_to_day_symbol_list = listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")

val period_list:List<Int> = List(5){it +1}

var uid: String? = null

var check_position = -1

val UserData_SharedPreferences_name = "UserDataStore"

var semester: String? = null
var university_id: String? = null

var local_timetable = mutableMapOf<String, String?>()

val developer = false

var week_num = 6
var period_num = 6

var timetable_id: MutableMap<String, ArrayList<Int>> = mutableMapOf()

var test_course_id = MutableStateFlow<Map<String, String?>?>(null)
