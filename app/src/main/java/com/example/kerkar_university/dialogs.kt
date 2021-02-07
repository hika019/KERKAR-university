package com.example.kerkar_university

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_add_class_editer.view.*
import kotlinx.android.synthetic.main.dialog_add_university.view.*

fun error_college_upload_dialog(context: Context){
    val messege = "ユーザー情報が正しくアップロードされなかった可能性があります。"
    val dialog = AlertDialog.Builder(context)
            .setTitle("アップロードエラー")
            .setMessage(messege)
            .setPositiveButton("OK"){ dialog, which -> false}
            .show()
}

fun select_semester_dialog(view: View, context: Context, semester_list: Array<String>, semester_id_list: Array<String>){

    Log.d("dialog", "select_semester_dialog -> call")

    var semester_id = ""
    val dialog = AlertDialog.Builder(context)
            .setTitle("学期選択")
            .setSingleChoiceItems(semester_list, -1){dialog, which ->
                semester_id = semester_id_list[which]
            }
            .setPositiveButton("OK"){ dialog, which ->
                if(semester_id != ""){
                    firedb_semester(context, view).change_user_semester(semester_id)
                }else{
                    false
                }
            }
    dialog.show()
}

class register_dialog(val context: Context, val uid: String){
    val TAG = "register_dialog"

    fun select_univarsity_rapper(){
        Log.d(TAG, "select_univarsity_rapper -> call")
        firedb_register_login(context).get_university_list(uid)
    }


    fun select_univarsity(university_name_list: Array<String>, university_id_list:Array<String>) {

        Log.d(TAG, "select_univarsity_dialog -> call")
        var university:String? = null
        var university_id: String? = null
//        val list = localdb.get_tmp()

        val dialog = AlertDialog.Builder(context)
                .setTitle("大学の選択")
                .setSingleChoiceItems(university_name_list, -1){ dialog, which ->
                    university = university_name_list[which]
                    university_id = university_id_list[which]

//                    Toast.makeText(context, university_name_list[which], Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("確定"){ dialog, which ->
//                    Toast.makeText(context, university.toString(), Toast.LENGTH_SHORT).show()
                    if(university != null && university_id != null){
                        val firedb = firedb_register_login(context)
                        firedb.create_user_data(uid, university.toString(), university_id.toString())



                        val i = Intent(context, MainActivity::class.java)
                        context.startActivity(i)
                    }else{
                        Toast.makeText(context, "大学の選択/追加をしてください", Toast.LENGTH_LONG).show()
                    }
                }
                .setNegativeButton("大学を追加"){ dialog, which->
                    create_university()
                }
        dialog.create().show()
    }

    fun create_university(){

        Log.d(TAG, "create_university_dialog -> call")
        val dialog_layout = LayoutInflater.from(context).inflate(R.layout.dialog_add_university, null)
        val firedb = firedb_register_login(context)

        val dialog = AlertDialog.Builder(context)
                .setTitle("大学を追加")
                .setView(dialog_layout)
                .setPositiveButton("登録"){ dialog, which ->
                    val university_name = dialog_layout.univarsity_edittext.text.toString()
                    firedb.create_university_collection(university_name)


                    val i = Intent(context, MainActivity::class.java)
                    context.startActivity(i)

                }
                .setNegativeButton("戻る"){ dialog, which ->
                    select_univarsity_rapper()
                }

        dialog.create().show()
    }

}

class timetable_dialog(val context: Context){
    private val TAG = "timetable_dialog"

    val firedb_timetable = firedb_timetable(context)

    fun timetable_data_dialog(week: String, time: Int, message: String?){

        val week_jp = week_to_day_jp_chenger(week)

        val dialog = AlertDialog.Builder(context)
                .setTitle("${week_jp}曜日 ${time}限の授業")
                .setMessage(message)
                .setPositiveButton("授業登録") { dialog, which ->

                    //登録画面
                    firedb_timetable.get_course_list(week, time)
                    false
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
        var index: Int = 0

        val dialog = AlertDialog.Builder(context)
                .setTitle(week_jp + "曜日 " + period + "限 で検索されています")
                .setSingleChoiceItems(selecter_list, -1){ dialog, which ->
                    index = which
                }
                .setPositiveButton("確定"){ dialog, which ->
                    val data = list[index] as Map<String, Any>
                    val class_id = data["course_id"] as String

                    firedb_timetable.add_user_timetable(semester_id, week_to_day+period, class_id)

                    false

                }
                .setNegativeButton("空き授業"){dialog, which ->
                    false

                }
                .setNeutralButton("授業をつくる"){dialog, which ->
                    firedb_timetable(context).create_course(week_to_day, period)
                    false
                }
        dialog.create().show()

    }

    fun add_timetable(week: String, period: Int, semester_id: String, semester: String){
        val dialog_layout = LayoutInflater.from(context).inflate(R.layout.dialog_add_class_editer, null)
        dialog_layout.semester_textView.text = semester

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
                            val week_to_day_symnbol_list = listOf("月", "火", "水", "木", "金")

                            if(week_to_day_symnbol_list.find{it == week_to_day} != null){
                                Log.d(TAG, "edit:" + week_to_day)
                                Log.d(TAG, "登録へ")

                                val week_symbol = week_to_day_symbol_chenger(week_to_day)
                                val teacher_list = str_to_array(teacher_name)

                                val data = hashMapOf(
                                        "semester_id" to semester_id,
                                        "week_to_day" to week_symbol + period,
                                        "course" to lecture_name,
                                        "lecturer" to teacher_list,
                                        "room" to class_name
                                )
                                //登録へ
                                firedb_timetable(context).create_university_timetable(data)

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

        dialog.show()
    }


}