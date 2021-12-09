package jp.hika019.kerkar_university

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.security.MessageDigest

fun sha256(str: String): String {
    val strHash = MessageDigest.getInstance("SHA-256")
            .digest(str.toByteArray())
            .joinToString(separator = "") {
                "%02x".format(it)
            }
    return strHash
}


fun weekToDayJpChenger(week: String): String{
    val index: Int = week_to_day_symbol_list.indexOf(week)
    return week_to_day_jp_list[index]
}

fun weekToDaySymbolChenger(week: String): String{
    val index: Int = week_to_day_jp_list.indexOf(week)
    return week_to_day_symbol_list[index]
}

fun strNumNormalization(str: String): String{
    val norm_str = str.replace("０", "0").replace("１", "1")
            .replace("２", "2").replace("３", "3")
            .replace("４", "4").replace("５", "5")
            .replace("６", "6").replace("７", "7")
            .replace("８", "8").replace("９", "9")
            .replace("　", " ")
            .replace("Ⅰ", "I")
            .replace("Ⅱ", "II").replace("Ⅲ", "III")
            .replace("Ⅳ", "IV").replace("Ⅴ", "V")
            .replace("Ⅵ", "VI").replace("Ⅶ", "VII")
            .replace("Ⅷ", "VIII").replace("Ⅸ", "IX")
            .replace("ⅰ", "I")
            .replace("ⅱ", "II").replace("ⅲ", "III")
            .replace("ⅳ", "IV").replace("ⅴ", "V")
            .replace("ⅵ", "VI").replace("ⅶ", "VII")
            .replace("ⅷ", "VIII").replace("ⅸ", "IX")
            .replace("（", "(").replace("）", ")")
            .replace("、", ",")

    return norm_str
}

fun strToArray(str: String): List<String> {
    var str = strNumNormalization(str)
    var position: Int
    val list: List<String>

    str = str.replace("[", "").replace("]", "")

    if(str.indexOf(",") != -1) list = str.split(",")
    else list = listOf(str)

    return list
}

fun courseDataMapToStr(data: Map<String, Any>): String {
    Log.d("other_fun", "course_data_map_to_str -> call")

    val mapData = data
    val teacher = mapData["lecturer"] as List<String>
    var teacherStr = ""

    teacherStr = if (teacher.size != 1) {
        "${teacher[0]} ...他"
    } else {
        teacher[0]
    }

    return "教科: ${mapData["course"]}\n" +
            "講師: $teacherStr\n" +
            "教室: ${mapData["room"]}\n"
}

fun addArrayToArray(base: Array<String>?, add: ArrayList<String?>?): Array<String> {

    var baseNew = arrayOf<String>()
    if(base != null){
        baseNew = base
    }
    if(add != null){
        for(item in add){
            if (item != null) baseNew += item
        }
    }
    return baseNew
}

fun cheackUid(uid0: String, uid1: String, uid2: String, uid3: String, uid4: String): Boolean {
    val block0 = Integer.parseInt(uid0, 16)
    val block1 = Integer.parseInt(uid1, 16)
    val block2 = Integer.parseInt(uid2, 16)
    val block3 = Integer.parseInt(uid3, 16)

    var check = Integer.toHexString((((((block0 +block1)%16777215) +block2)%16777215) +block3)% 16777215) //FFFFFFでmod
    while(check.length < 6){
        check = "0" + check
    }

    return uid4 == check


}

fun getTimetableId(context: Context): String? {
    val datastore: SharedPreferences = context.getSharedPreferences("user_db", Context.MODE_PRIVATE)
    return datastore.getString("timetable_id", null)
}

fun setTimetableId(context: Context, id: String) {
    val datastore: SharedPreferences = context.getSharedPreferences("user_db", Context.MODE_PRIVATE)
    val editer = datastore.edit()
    editer.putString("timetable_id", id)
    editer.commit()
    timetable_id.value = id
}


fun getCourseName(week_period: String): String {
    Log.d("timetable", "get_course_id -> call")
    val tmp =  test_course_data_map?.get(week_period)

    var course_name = ""

    if (tmp != null){
        val data = tmp as Map<String, Any>
        course_name = data["course"] as String
    }
    return course_name
}

fun getLecturer(week_period: String): String {
    Log.d("timetable", "get_lecturer -> call")
    val tmp =  test_course_data_map?.get(week_period)

    var lecturer = arrayListOf<String>("")

    if (tmp != null){
        val data = tmp as Map<String, Any>
        lecturer = data["lecturer"] as ArrayList<String>
    }
    return lecturer[0]
}

fun get_course_room(week_period: String): String {
    Log.d("timetable", "get_course_room -> call")
    val tmp =  test_course_data_map?.get(week_period)

    var lecturer = ""

    if (tmp != null){
        val data = tmp as Map<String, Any>
        lecturer = data["room"] as String
    }
    return lecturer
}
