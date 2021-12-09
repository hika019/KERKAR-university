package jp.hika019.kerkar_university

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Timestamp
import jp.hika019.kerkar_university.timetable.CreateTimetableActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun none_Timetable(context: Context){

    val message = "時間割がまだありません\n新しく時間割を登録しましょう"

    val dialog = AlertDialog.Builder(context)
        .setTitle(message)
        .setMessage("時間割を追加してください")
        .setPositiveButton("OK"){ dialog, which ->
            val i = Intent(context, CreateTimetableActivity::class.java)
            context?.startActivity(i)
        }

    dialog.show()
}

class TimetableDialog(override val context: Context): firedb_timetable(context){
    private val TAG = "timetable_dialog"

    fun searchTimetableDialog(week_to_day: String, period: Int, list: List<Any>, semester_id: String){
        Log.d(TAG, "course_list_dialog -> call")
        var idList: Array<String> = arrayOf()
        var selecterList: Array<String> = arrayOf()

        for(item in list){
            val data = item as Map<String, Any>
            idList += data["course_id"] as String

            val course = data["course"]
            val teacher = data["lecturer"] as List<String>
            val room = data["room"]
            var lecturer = ""

            if(teacher.size > 1){
                lecturer += teacher[0] + " ...他"
            }else{
                lecturer += teacher[0]
            }

            val str = "教科: ${course}\n" +
                    "講師: ${lecturer}\n" +
                    "教室: $room"

            selecterList += str

            Log.d(TAG, "item: $item")
        }

        val weekJp = weekToDayJpChenger(week_to_day)
        var index = 0

        val dialog = AlertDialog.Builder(context)
                .setTitle(weekJp + "曜日 " + period + "限 での検索結果")
                .setSingleChoiceItems(selecterList, -1){ dialog, which ->
                    index = which
                }
                .setPositiveButton("確定"){ _, _ ->
                    if(list.isNotEmpty()){
                        val data = list[index] as Map<String, Any>
                        val classId = data["course_id"] as String

                        addUserTimetable(week_to_day+period, classId)
                    }
                }
                .setNeutralButton("授業をつくる"){ _, _ ->

                    createCourceWtd = week_to_day
                    createCourcePeriod = period

                    val i = Intent(context, CreateCourceActivity::class.java)
                    context.startActivity(i)
                    //create_course_dialog()
                }
        dialog.create().show()

    }
}

class TaskDialog(val context: Context){

    val tag = "task_dialog$tagHoge"

    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
    var hour: Int? = null
    var minute: Int? = null

    var time: String? = null
    var subjectData: MutableMap<String, String> = mutableMapOf()
    var semesterIdData: String? = null
    var semester: String? = null


    fun courseSelecterDialog(class_datas: ArrayList<Any>){

        var classNameList = arrayOf<String>()
        var classIdList = arrayOf<String>()
        var classWeekToDayList = arrayOf<String>()

        for(item in class_datas){
            val itemData = item as Map<String, String>
            classNameList += itemData["course"]!!
            classIdList += itemData["course_id"]!!
            classWeekToDayList += itemData["week_to_day"]!!
        }


        var select_point: Int? = null

        val builder = AlertDialog.Builder(context)
                .setTitle("課題を追加する授業を選択してください")
                .setSingleChoiceItems(classNameList, -1){ _, which ->
                    select_point = which
                }
                .setPositiveButton("確定"){ _, _ ->
                    if(select_point != null) {

                        val data =hashMapOf<String, String>(
                                "class_name" to classNameList[select_point!!],
                                "class_id" to classIdList[select_point!!],
                                "week_to_day" to classWeekToDayList[select_point!!]
                        )
                        val i = Intent(context, CreateTaskActivity::class.java)
                        i.putExtra(
                            "week_period",
                            "${classWeekToDayList[select_point!!].take(3)}${classWeekToDayList[select_point!!][3]}")
                        context.startActivity(i)

//                        subject_data = data
//                        Log.d(TAG, "class_id1: ${class_id_list[select_point!!]}")
//                        create_task_dialog()
                    }
                }
                .setNegativeButton("戻る"){ dialog, which ->
                }

        builder.show()

    }

}

class TaskDialogNew(): firedbColDoc(){

    @SuppressLint("SimpleDateFormat")
    fun taskDetailDialog(context: Context, task_data: Map<String, Any>): AlertDialog.Builder {
        val cal = Calendar.getInstance()

        val classData = task_data["class_data"] as Map<String, Any>
        val timeLimitTimestamp = task_data["time_limit"] as Timestamp

        cal.time = timeLimitTimestamp.toDate()
        val df = SimpleDateFormat("MM/dd HH:mm")


        val str = "　期限: ${df.format(cal.time)}\n" +
                "　教科: ${classData["course"]}\n" +
                "　詳細: ${task_data["task_name"]}\n" +
                "その他: ${task_data["note"]}"

        return AlertDialog.Builder(context)
            .setTitle("課題")
            .setMessage(str)
    }

}