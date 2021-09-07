package jp.hika019.kerkar_university

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_add_class_editer.view.*
import kotlinx.android.synthetic.main.dialog_add_task.view.*
import kotlinx.android.synthetic.main.dialog_add_university.view.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun error_college_upload_dialog(context: Context){
    val messege = "ユーザー情報が正しくアップロードされなかった可能性があります。"
    val dialog = AlertDialog.Builder(context)
            .setTitle("アップロードエラー")
            .setMessage(messege)
            .setPositiveButton("OK"){ dialog, which -> false}
            .show()
}



fun none_Timetable(context: Context){

    val message = "授業がまだありません\n新しく授業を登録しましょう"

    val dialog = AlertDialog.Builder(context)
        .setTitle(message)
        .setMessage("時間割画面から授業を追加してください")
        .setPositiveButton("OK"){ dialog, which ->

        }

    dialog.show()
}


open class register_dialog(open val context: Context){
    open val TAG = ""

    fun select_univarsity_rapper(){
        Log.d(TAG, "select_univarsity_rapper -> call")
        firedb_register_login(context).get_university_list_LoadActivity()
    }


    fun select_univarsity(university_name_list: Array<String>, university_id_list:Array<String>) {

        Log.d(TAG, "select_univarsity_dialog -> call")
        var university:String? = null
        var university_id: String? = null
//        val list = localdb.get_tmp()

        val dialog = AlertDialog.Builder(context)
                .setTitle("大学の選択")
                .setCancelable(false)
                .setSingleChoiceItems(university_name_list, -1){ dialog, which ->
                    university = university_name_list[which]
                    university_id = university_id_list[which]

//                    Toast.makeText(context, university_name_list[which], Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("確定"){ dialog, which ->
//                    Toast.makeText(context, university.toString(), Toast.LENGTH_SHORT).show()
                    if(university != null && university_id != null){
                        val firedb = firedb_register_login(context)
                        firedb.create_user_data(uid!!, university.toString(), university_id.toString())

//                        val i = Intent(context, MainActivity::class.java)
//                        context.startActivity(i)
                    }else{
                        Toast.makeText(context, "大学の選択/追加をしてください", Toast.LENGTH_LONG).show()
                        select_univarsity(university_name_list, university_id_list)
                    }
                }
                .setNegativeButton("大学を追加"){ dialog, which->
                    create_university()
                }
        runBlocking {
            dialog.create().show()
        }

    }

    fun create_university(){

        Log.d(TAG, "create_university_dialog -> call")
        val dialog_layout = LayoutInflater.from(context).inflate(R.layout.dialog_add_university, null)
        val firedb = firedb_register_login(context)

        val dialog = AlertDialog.Builder(context)
                .setTitle("大学を追加")
                .setCancelable(false)
                .setView(dialog_layout)
                .setPositiveButton("登録"){ dialog, which ->
                    val university_name = dialog_layout.univarsity_edittext.text.toString()

                    if(university_name != ""){
                        firedb.create_university_collection(university_name)
                    }else{
                        select_univarsity_rapper()
                    }


                }
                .setNegativeButton("戻る"){ dialog, which ->
                    select_univarsity_rapper()
                }

        dialog.create().show()
    }

}

class timetable_dialog(override val context: Context): firedb_timetable(context){
    private val TAG = "timetable_dialog"

    fun timetable_data_dialog(week: String, time: Int, message: String?){

        val week_jp = week_to_day_jp_chenger(week)

        val dialog = AlertDialog.Builder(context)
                .setTitle("${week_jp}曜日 ${time}限の授業")
                .setMessage(message)
                .setPositiveButton("授業登録") { dialog, which ->

                    //登録画面
                    get_course_list(week, time)
                }
                .setNegativeButton("戻る"){ dialog, which ->

                }
//                .setNeutralButton("課題を確認") { dialog, which ->
//
//                }
         dialog.show()
    }

    fun search_timetable_dialog(week_to_day: String, period: Int, list: List<Any>, semester_id: String){
        Log.d(TAG, "course_list_dialog -> call")
        var id_list: Array<String> = arrayOf()
        var selecter_list: Array<String> = arrayOf()

        for(item in list){
            val data = item as Map<String, Any>
            id_list += data["course_id"] as String

            val week_to_day = data["week_to_day"]
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
                    "教室: ${room}"

            selecter_list += str

            Log.d(TAG, "item: ${item}")
        }

        val week_jp = week_to_day_jp_chenger(week_to_day)
        var index = 0

        val dialog = AlertDialog.Builder(context)
                .setTitle(week_jp + "曜日 " + period + "限 で検索されています")
                .setSingleChoiceItems(selecter_list, -1){ dialog, which ->
                    index = which
                }
                .setPositiveButton("確定"){ dialog, which ->
                    if(list.isNotEmpty()){
                        val data = list[index] as Map<String, Any>
                        val class_id = data["course_id"] as String

                        add_user_timetable(week_to_day+period, class_id)
                    }
                }
                .setNegativeButton("空き授業"){dialog, which ->
                    delete_user_timetable(semester_id, week_to_day+period,)
                }
                .setNeutralButton("授業をつくる"){dialog, which ->
                    val i = Intent(context, Create_courceActivity::class.java)
                    context.startActivity(i)
                    //create_course_dialog()
                }
        dialog.create().show()

    }

    fun create_course_dialog(){
        Log.d(TAG, "create_course -> call")
        val dialog_layout = LayoutInflater.from(context).inflate(R.layout.dialog_add_class_editer, null)

        val dialog = AlertDialog.Builder(context)
                .setTitle("授業登録")
                .setView(dialog_layout)
                .setPositiveButton("追加"){dialog, which ->

                    val week_to_day = dialog_layout.week_to_day_edit_textview.text.toString()
                    val period = dialog_layout.period_edittextview.text.toString()
                    var lecture_name = dialog_layout.lecture_neme_edittext.text.toString()
                    val teacher_name = dialog_layout.teacher_name_edittext.text.toString()
                    var class_name = dialog_layout.class_name_edittext.text.toString()

                    class_name = str_num_normalization(class_name)
                    lecture_name = str_num_normalization(lecture_name)

                    if(week_to_day.isNotEmpty() && period.isNotEmpty() &&
                            lecture_name.isNotEmpty() && teacher_name.isNotEmpty() &&
                            class_name.isNotEmpty()){

                        Log.d(TAG, "未入力なし")

                        if(period.toInt() < 6){

                            if(week_to_day_symbol_list_jp_short.find{it == week_to_day} != null){
                                Log.d(TAG, "edit:" + week_to_day)
                                Log.d(TAG, "登録へ")

                                val week_symbol = week_to_day_symbol_chenger(week_to_day)
                                val teacher_list = str_to_array(teacher_name)

                                val data = hashMapOf(
                                        "semester_id" to semester!!,
                                        "week_to_day" to week_symbol + period,
                                        "course" to lecture_name,
                                        "lecturer" to teacher_list,
                                        "room" to class_name
                                )
                                //登録へ
                                create_course_university_timetable(data)

                            }else{
                                Log.d(TAG, "曜日が不正")
                                Toast.makeText(context, "曜日は漢字一文字にしてください", Toast.LENGTH_LONG).show()
                            }

                        }else{
                            //オーバー
                            Log.d(TAG, "時限数が5以上:${period}")
                            Toast.makeText(context, "5限目まで対応しています\n" +
                                    "入力された6限目以上のため登録されていません\n" +
                                    "入力された値: ${period}", Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(context, "未入力の場所があります", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "未入力あり")
                    }
                }
                .setNeutralButton("破棄"){dialog, which ->
                    false
                }

        dialog.show()
    }

}

class task_dialog(val context: Context){

    val TAG = "task_dialog"

    var day: String? = null
    var time: String? = null
    var subject_data: MutableMap<String, String> = mutableMapOf()
    var semester_id_data: String? = null
    var semester: String? = null

    val task_dialog_second = LayoutInflater.from(context).inflate(
            R.layout.dialog_add_task,
            null
    )

    fun course_selecter_dialog(class_datas: ArrayList<Any>){

        var class_name_list = arrayOf<String>()
        var class_id_list = arrayOf<String>()
        var class_week_to_day_list = arrayOf<String>()

        for(item in class_datas){
            val item_data = item as Map<String, String>
            class_name_list += item_data["course"]!!
            class_id_list += item_data["course_id"]!!
            class_week_to_day_list += item_data["week_to_day"]!!
        }


        //Log.d(TAG, "これがないとなぜか動かない　semester: $semester_name")
        //Log.d(TAG, "これがないとなぜか動かない　semester: $semester")

        var select_point: Int? = null

        val builder = AlertDialog.Builder(context)
                .setTitle("追加する課題の授業を選択")
                .setSingleChoiceItems(class_name_list, -1){ dialog, which ->
                    select_point = which
                }
                .setPositiveButton("確定"){ dialog, which ->
                    if(select_point != null) {

                        val data =hashMapOf<String, String>(
                                "class_name" to class_name_list[select_point!!],
                                "class_id" to class_id_list[select_point!!],
                                "week_to_day" to class_week_to_day_list[select_point!!]
                        )
                        subject_data = data
                        Log.d(TAG, "class_id1: ${class_id_list[select_point!!]}")
                        create_task_dialog()
                    }
                }
                .setNegativeButton("戻る"){ dialog, which ->
                }

        builder.show()

    }

    fun day_cheack(today: List<String>, set_day: List<String>): Boolean {
        val today_y = today[0].toInt()
        val today_m = today[1].toInt()
        val today_d = today[2].toInt()

        val set_y = set_day[0].toInt()
        val set_m = set_day[1].toInt()
        val set_d = set_day[2].toInt()

        if(today_y > set_y){
            return false
        }else if(today_y < set_y){
            return true
        }


        if(today_m > set_m){
            return false
        }else if(today_m < set_m){
            return true
        }

        if(today_d > set_d){
            return false
        }else{
            return true
        }

    }

    fun create_task_dialog(){
        task_dialog_second.add_task_semester_textView.text = semester

        task_dialog_second.add_day_button.setOnClickListener {
            set_deadline_day()
        }
        task_dialog_second.add_time_button.setOnClickListener {
            time_picker()
        }
        task_dialog_second.dialog_deadline_day.text = day
        task_dialog_second.dialog_deadline_time.text = time
        task_dialog_second.dialog_subject.text = subject_data["class_name"]

        val dialog = AlertDialog.Builder(context)
                .setTitle("課題追加")
                .setView(task_dialog_second)
                .setPositiveButton("確定") { dialog, which ->

                    val title = task_dialog_second.dialog_assignment_special_notes.text.toString()
                    val note = task_dialog_second.dialog_assignment_special_notes.text.toString()

                    if(task_dialog_second.dialog_deadline_day.text.isNotEmpty() &&
                            task_dialog_second.dialog_deadline_time.text.isNotEmpty() &&
//                            subject_data["class"] != null &&
                            title != ""){

                        val set_day = task_dialog_second.dialog_deadline_day.text.toString().split("/")

                        val d = Date() // 現在時刻
                        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
                        //val today = LocalDate.now().toString().split("-")

                        //Log.d("hoge", "set day ->${set_day}")
                        //Log.d("hoge", "today ->${today}")

                        //if(day_cheack(today, set_day)){
                        if(true){
                            val data = hashMapOf(
                                    "day" to task_dialog_second.dialog_deadline_day.text.toString(),
                                    "time" to task_dialog_second.dialog_deadline_time.text.toString(),
                                    "class" to subject_data,
                                    "task_title" to title,
                                    "note" to note
                            )
                            Log.d(TAG, "set -> data: ${data}")


                            //追加処理
                            Toast.makeText(context, "課題を追加しました", Toast.LENGTH_SHORT)
                            firedb_task(context).create_task(data)
                        }else{
                            Toast.makeText(context, "締め切り日が無効です", Toast.LENGTH_SHORT)
                        }
                    }else{
                        false
                        //create_task_dialog(day!!, time!!, title, note)
                        Toast.makeText(context, "空の部分があります", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("破棄") { dialog, which ->
                    false
                }
        dialog.create().show()
    }

    fun create_task_dialog(day: String, time: String, task_name: String, other: String){
        task_dialog_second.add_task_semester_textView.text = semester

        task_dialog_second.add_day_button.setOnClickListener {
            set_deadline_day()
        }
        task_dialog_second.add_time_button.setOnClickListener {
            time_picker()
        }
        task_dialog_second.dialog_deadline_day.setText(day)
        task_dialog_second.dialog_deadline_time.setText(time)
        task_dialog_second.dialog_subject.text = subject_data["class_name"]
        task_dialog_second.dialog_assignment_special_notes.setText(task_name)
        task_dialog_second.dialog_assignment_special_notes.setText(other)

        val dialog = AlertDialog.Builder(context)
            .setTitle("課題追加")
            .setView(task_dialog_second)
            .setPositiveButton("確定") { dialog, which ->

                val title = task_dialog_second.dialog_assignment_special_notes.text.toString()
                val note = task_dialog_second.dialog_assignment_special_notes.text.toString()

                if(task_dialog_second.dialog_deadline_day.text.isNotEmpty() &&
                    task_dialog_second.dialog_deadline_time.text.isNotEmpty() &&
//                            subject_data["class"] != null &&
                    title != ""){

                    val set_day = task_dialog_second.dialog_deadline_day.text.toString().split("/")

                    val d = Date() // 現在時刻
                    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
                    //val today = LocalDate.now().toString().split("-")

                    //Log.d("hoge", "set day ->${set_day}")
                    //Log.d("hoge", "today ->${today}")

                    //if(day_cheack(today, set_day)){
                    if(true){
                        val data = hashMapOf(
                            "day" to task_dialog_second.dialog_deadline_day.text.toString(),
                            "time" to task_dialog_second.dialog_deadline_time.text.toString(),
                            "class" to subject_data,
                            "task_title" to title,
                            "note" to note
                        )
                        Log.d(TAG, "set -> data: ${data}")


                        //追加処理
                        Toast.makeText(context, "課題を追加しました", Toast.LENGTH_SHORT)
                        firedb_task(context).create_task(data)
                    }else{
                        Toast.makeText(context, "締め切り日が無効です", Toast.LENGTH_SHORT)
                    }
                }else{
                    false
                    create_task_dialog(day!!, time!!, title, note)
                    Toast.makeText(context, "空の部分があります", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("破棄") { dialog, which ->
                false
            }
        dialog.create().show()
    }



    fun set_deadline_day(){
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(context,
                { view, year, month, dayOfMonth -> //setした日付を取得して表示
                    calendar.set(year, month, dayOfMonth)
                    val dfInputeDate = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                    val strInputDate = dfInputeDate.format(calendar.time)
//                    Log.d("hoge", "time: ${calendar.time} -> show")
                    day = strInputDate
                    Log.d(TAG, "day: ${day} -> set")
                    task_dialog_second.dialog_deadline_day.text = day
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        )
        datePickerDialog.show()
    }

    fun time_picker(){
        val calender = Calendar.getInstance()

        val timePicker = TimePickerDialog(context,
                { view, hour, minute ->
//                    Log.d("hoge", "time: ${hour} -> get")
                    calender.set(2000,1,5,hour, minute)
                    val dfInputdata = SimpleDateFormat("HH:mm")
                    val strInputDate = dfInputdata.format(calender.time)
//                    Log.d("hoge", "time: ${strInputDate} -> show")
                    time = strInputDate
//                    time = ("%2:${minute}").format(hour)
//                    time = "${hour}:${minute}"
                    Log.d(TAG, "time: ${time} -> set")
                    task_dialog_second.dialog_deadline_time.text = time
                },
                calender.get(Calendar.HOUR_OF_DAY),
                calender.get(Calendar.MINUTE),
                true
        )
        timePicker.show()

    }

}