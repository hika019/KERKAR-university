package jp.hika019.kerkar_university

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import java.security.MessageDigest

fun sha256(str: String): String {
    val strHash = MessageDigest.getInstance("SHA-256")
            .digest(str.toByteArray())
            .joinToString(separator = "") {
                "%02x".format(it)
            }
    return strHash
}


fun week_to_day_jp_chenger(week: String): String{
    val index: Int = week_to_day_symbol_list.indexOf(week)
    return week_to_day_jp_list[index]
}

fun week_to_day_symbol_chenger(week: String): String{
    val index: Int = week_to_day_jp_list.indexOf(week)
    return week_to_day_symbol_list[index]
}

fun str_num_normalization(str: String): String{
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

fun str_to_array(str: String): List<String> {
    var str = str_num_normalization(str)
    var position: Int
    val list: List<String>

    str = str.replace("[", "").replace("]", "")

    if(str.indexOf(",") != -1) list = str.split(",")
    else list = listOf(str)

    return list
}

fun course_data_map_to_str(data: Map<String, Any>): String {
    Log.d("other_fun", "course_data_map_to_str -> call")

    val map_data = data
    val teacher = map_data["lecturer"] as List<String>
    var teacher_str = ""

    if(teacher.size != 1){
        teacher_str = "${teacher[0]} ...他"
    }else{
        teacher_str = "${teacher[0]}"
    }

    val str = "教科: ${map_data["course"]}\n" +
            "講師: $teacher_str\n" +
            "教室: ${map_data["room"]}\n"
    return str
}

fun add_array_to_array(base: Array<String>?, add: ArrayList<String?>?): Array<String> {

    var base_new = arrayOf<String>()
    if(base != null){
        base_new = base
    }
    if(add != null){
        for(item in add){
            if (item != null) base_new += item
        }
    }
    return base_new
}

fun cheack_uid(uid0: String, uid1: String, uid2: String, uid3: String, uid4: String): Boolean {
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
