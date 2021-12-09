package jp.hika019.kerkar_university

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

val week_to_day_jp_list = listOf("日", "月", "火", "水", "木", "金", "土")

val week_to_day_symbol_list_jp_short = listOf("月", "火", "水", "木", "金", "土")

val week_to_day_symbol_list = listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")

val period_list:List<Int> = List(6){it +1}

var uid: String? = null

var check_position = -1


//移動系
var toHomeFragment = MutableLiveData<Boolean>(false)
val createTimetableFinish = MutableLiveData(false)
val createTaskFinish = MutableLiveData(false)


var semester: String? = null
var university_id: String? = null
var timetable_name: String? = ""
var timetable_id = MutableLiveData<String?>(null)

var searchbar = MutableLiveData<String?>(null)

val url = "https://forms.gle/E6fZro59yE6Qdz6dA"

var local_timetable = mutableMapOf<String, String?>()

val developer = false

var week_num = 6
var period_num = 6

var login_flag = false

var tmp_str = ""

var test_course_data_map = mutableMapOf<String, Any?>()


//Create_course
var createCourcePeriod = -1
var createCourceWtd = ""

//使ってる
var user_timetable_data_live = MutableLiveData<Map<String, Any?>>()
var course_data_live = MutableLiveData<MutableMap<String, Any?>>()

const val tagHoge = "Logs"