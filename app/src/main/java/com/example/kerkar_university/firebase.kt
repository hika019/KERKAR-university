package com.example.kerkar_university

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_timetable.view.*
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "firedb"
var firedb = FirebaseFirestore.getInstance()

fun login_cheack(): Boolean {
    val cheack_user = Firebase.auth.currentUser
    Log.d(TAG, "login check: ${cheack_user != null}")
    return cheack_user != null
}

fun get_uid(): String{
    return Firebase.auth.currentUser!!.uid
}

class firedb_semester(val context: Context, val view: View){
    val TAG = "firedb_semester"

    fun get_semester_list(){
        Log.d(TAG, "get_semester_list -> call")

        var semester_name_list: Array<String> = arrayOf()
        var semester_id_list: Array<String> = arrayOf()

        firedb.collection("semester")
                .get()
                .addOnSuccessListener {
                    for(item in it){

                        semester_id_list += item.id
                        semester_name_list += item.getString("title")!!
                    }
                    select_semester_dialog(view, context, semester_name_list, semester_id_list)

                }
    }

    fun change_user_semester(semester: String){
        Log.d(TAG, "change_user_semester -> call")
        val uid = get_uid()
        firedb.collection("user")
                .document(uid)
                .update("semester", semester)
                .addOnSuccessListener {
                    firedb.collection("user")
                            .document(uid)
                            .collection("semester")
                            .document(semester)
                    Log.d(TAG, "change_user_semester -> success")
                    get_semester_title()
                }
                .addOnFailureListener {
                    Log.e(TAG, "change_user_semester -> failure")
                }
    }

    fun get_semester_title(){
        firedb.collection("user")
                .document(get_uid())
                .get()
                .addOnSuccessListener {
                    val semester_id = it.getString("semester")

                    if (semester_id != null) {
                        firedb.collection("semester")
                                .document(semester_id)
                                .get()
                                .addOnSuccessListener {
                                    view.semester_textView.text = it.getString("title")
                                }
                    }

                }
    }
}

class firedb_register_login(val context: Context){
    private val TAG = "firedb_register_login"

    fun get_university_list(uid: String){
        var university_name_list: Array<String> = arrayOf()
        var university_id_list: Array<String> = arrayOf()
        Log.d(TAG, "get_university_list -> call")

        Log.d("hoge", "data: ${university_name_list}")
        firedb.collection("university")
                .get()
                .addOnSuccessListener {
                    for (i in it){
                        val university_id = i.id
                        val university_name = i.getString("university")!!

                        university_id_list += university_id
                        university_name_list += university_name
                    }



                    register_dialog(context, uid).select_univarsity(university_name_list, university_id_list)
                }

    }

    fun create_university_collection(university_name: String){
        Log.d(TAG, "create_university_collection -> call")
        val doc_id = firedb.collection("university")
                .document()

        val data = hashMapOf(
                "university" to university_name,
                "university_id" to doc_id.id
        )

        doc_id.set(data)
                .addOnCompleteListener {
                    Log.d(TAG, "create university -> Complete")
                    create_user_data(get_uid(), university_name, doc_id.id)
                }
    }

    fun create_user_data(uid: String, university: String, university_id: String){

        Log.d(TAG, "create_user_data -> call")

        var semester: Array<Int> = arrayOf()
        var nowsemester: String? = null

        firedb.collection("semester")
                .get()
                .addOnSuccessListener {
                    for(item in it){
                        semester += item.id.toInt()
//                        Log.d("hoge", "semester: ${item.id}")


                    }
                    Log.d(TAG, "semester: ${semester}")
                    nowsemester = semester.max().toString()

                    val time = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date())
                    if(nowsemester != null){

                        val data = hashMapOf(
                                "uid" to uid,
                                "university" to university,
                                "create_at" to time,
                                "university_id" to university_id,
                                "semester" to nowsemester
                        )

                        firedb.collection("user")
                                .document(uid)
                                .set(data)
                                .addOnCompleteListener {
                                    Log.d(TAG, "create user_data -> Complete")
                                }
                    }
                }
                .addOnFailureListener {

                }
    }

    fun cheak_user_data(uid: String){

        Log.d(TAG, "cheak_user_data -> call")
        firedb.collection("user")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    val create_at = it.getString("create_at")
                    val semester = it.get("semester")
                    val uid = it.getString("uid")
                    val university = it.getString("university")
                    val university_id = it.getString("university_id")

                    if(create_at != null || semester != null || uid != null || university != null || university_id != null){
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }else{
                        register_dialog(context, get_uid()).select_univarsity_rapper()
                    }

                }
    }

}


class firedb_timetable(val context: Context){
    private val TAG = "firedb_timetable"


    fun create_course(week: String, period: Int){
        firedb.collection("user")
                .document(get_uid())
                .get()
                .addOnSuccessListener {
                    val semester_id = it.getString("semester")

                    if (semester_id != null) {
                        firedb.collection("semester")
                                .document(semester_id)
                                .get()
                                .addOnSuccessListener {
                                    val semester = it.getString("title")

                                    if (semester != null) {
                                        timetable_dialog(context).add_timetable(week, period, semester_id, semester)
                                    }

                                }
                                .addOnFailureListener {
                                    Log.d(TAG, "firedb_timetable.semester2 -> failure")
                                }
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "firedb_timetable.semester1 -> failure")
                }
    }

    fun get_course_list(week: String, period: Int){
        if(login_cheack()){
            Log.d(TAG, "get_course_list: login_check")

            firedb.collection("user")
                    .document(get_uid())
                    .get()
                    .addOnSuccessListener {
                        Log.d(TAG, "get_course_list: get user data -> success")

                        val university_id = it.getString("university_id")
                        val semester_id = it.getString("semester")

                        if (university_id != null && semester_id != null) {
                            firedb.collection("university")
                                    .document(university_id)
                                    .collection("semester")
                                    .document(semester_id)
                                    .collection(week+period)
                                    .get()
                                    .addOnSuccessListener{
                                        Log.d(TAG, "get_course_list: get courses -> success")
                                        
                                        val course_list: ArrayList<Any> = arrayListOf()

                                        for(document in it){
                                            val data = hashMapOf<String, Any>(
                                                    "course_id" to document.id,
                                                    "week_to_day" to document.get("week_to_day") as String,
                                                    "course" to document.get("course") as String,
                                                    "lecturer" to document.get("lecturer") as ArrayList<String>,
                                                    "room" to document.get("room") as String
                                            )
                                            course_list.add(data)
                                        }

                                        timetable_dialog(context).search_timetable_dialog(week, period, course_list, semester_id)
                                    }
                                    .addOnFailureListener {
                                        Log.e(TAG, "get_course_list: get courses -> failure")
                                    }
                        }else{
                            Log.e(TAG, "get_course_list: university_id/semester_id is null")
                        }



                    }
                    .addOnFailureListener {
                        Log.d(TAG, "get_course_list: get user data -> failure")
                    }

        }else{
            Log.e(TAG, "get_course_list: not login")
        }
    }

    fun get_course_data(week_to_day: String, period: Int){
        var str: String? = null

        if(login_cheack()){
            val uid = get_uid()

            val user_doc = firedb.collection("user")
                    .document(uid)
            user_doc.get()
                    .addOnSuccessListener {

                        val semester = it.getString("semester")

                        if (semester != null) {
                            user_doc.collection("semester")
                                    .document(semester)
                                    .get()
                                    .addOnSuccessListener {
                                        Log.d(TAG, "get_course_data${week_to_day + period} -> success")
                                        val raw_data = it.get(week_to_day+period)

                                        if (raw_data != null){
                                            str = course_data_map_to_str(raw_data as Map<String, Any>)
                                        }else{
                                            str = "授業が登録されていません"
                                        }

                                        timetable_dialog(context).timetable_data_dialog(week_to_day, period, str)

                                    }
                                    .addOnFailureListener {
                                        Log.d(TAG, "get_course_data${week_to_day + period} -> failure")
                                    }
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "get_course_data -> failure")
                    }
        }
    }

    fun create_university_timetable(data: Map<String, Any>){

        val course_data = mutableMapOf(
                "week_to_day" to data["week_to_day"] as String,
                "course" to data["course"] as String,
                "lecturer" to data["lecturer"] as List<String>,
                "room" to data["room"] as String
        )

        firedb.collection("user")
                .document(get_uid())
                .get()
                .addOnSuccessListener {

                    val university_id = it.getString("university_id")
                    Log.d(TAG, "create_university_timetable: get universiyt_id -> success")

                    val doc = firedb.collection("university")
                            .document(university_id!!)
                            .collection("semester")
                            .document(data["semester_id"] as String)
                            .collection(data["week_to_day"] as String)
                            .document()

                    course_data["course_id"] = doc.id

                    doc.set(course_data, SetOptions.merge())
                            .addOnSuccessListener {
                                Log.d(TAG, "create_university_timetable: set data -> success")

                                add_user_timetable(data["semester_id"] as String, data["week_to_day"] as String, doc.id)

                            }
                            .addOnFailureListener{
                                Log.d(TAG, "create_university_timetable: set data -> failure")
                            }

                }
                .addOnFailureListener{
                    Log.e(TAG, "create_university_timetable: get universiyt_id -> failure")
                }
    }

    fun add_user_timetable(semester_id: String, week_to_day: String, classId: String){
        val uid = get_uid()

        firedb.collection("user")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    Log.d(TAG, "add_user_timetable: get university_id -> Success")
                    val universiyt_id = it.getString("university_id")


                    firedb.collection("university")
                            .document(universiyt_id!!)
                            .collection("semester")
                            .document(semester_id)
                            .collection(week_to_day)
                            .document(classId)
                            .get()
                            .addOnSuccessListener {
                                Log.d(TAG, "add_user_timetable: get university_id -> success")

                                //コピーとる
                                val time = it.getString("week_to_day")
                                val course = it.getString("course")
                                val id = it.getString("course_id")
                                val lecturer = it.get("lecturer")
                                val room = it.getString("room")


                                val in_data = hashMapOf(
                                        "week_to_day" to time,
                                        "course_id" to id,
                                        "lecturer" to lecturer,
                                        "course" to course,
                                        "room" to room
                                )

                                val data = hashMapOf(
                                        time.toString() to in_data
                                )

                                firedb.collection("user")
                                        .document(uid)
                                        .collection("semester")
                                        .document(semester_id)
                                        .set(data, SetOptions.merge())
                                        .addOnSuccessListener {
                                            Log.d(TAG, "add course to user: ${id} -> success")
                                        }
                                        .addOnFailureListener{
                                            Log.d(TAG, "add course to user: ${id} -> failure")
                                        }

                            }
                            .addOnFailureListener {
                                Log.d(TAG, "add_user_timetable: get university_id -> failure")
                            }


                }
                .addOnFailureListener {
                    Log.d(TAG, "add_user_timetable: get university_id -> Failure")
                }
    }

    fun delete_user_timetable(semester_id: String, week_to_day: String){
        val uid = get_uid()

        val updates = hashMapOf<String, Any>(
                week_to_day to FieldValue.delete()
        )

        firedb.collection("user")
                .document(uid)
                .collection("semester")
                .document(semester_id)
                .update(updates).addOnCanceledListener {
                    Log.d(TAG, "delete_user_timetable: $week_to_day -> complete")
                }
    }


}

class firedb_task(val context: Context){
    private val TAG = "firedb_task"

    val week_to_day_symbol_list = listOf("sun", "mon", "tue", "wen", "thu", "fri", "sat")
    val period_list:List<Int> = List(5){it +1}

    fun get_course_list(){
        if(login_cheack()){
            Log.d(TAG, "get_course_list: login_check")

            val uid = get_uid()

            val user_doc = firedb.collection("user")
                    .document(get_uid())


            user_doc.get()
                    .addOnSuccessListener {
                        Log.d(TAG, "firedb_task.get_course_list: get user data -> success")
                        val semester_id = it.getString("semester")

                        if (semester_id != null) {

                            var semester: String? = null

                            firedb.collection("semester")
                                    .document(semester_id)
                                    .get()
                                    .addOnSuccessListener {
                                        semester = it.getString("title")
                                    }

                            user_doc.collection("semester")
                                    .document(semester_id)
                                    .get()
                                    .addOnSuccessListener {
                                        Log.d(TAG, "firedb_task.get_course_list: get user timetable -> success")

                                        var class_name_array: Array<String> = arrayOf()
                                        var class_id_array: Array<String> = arrayOf()
                                        var class_week_to_day_array: Array<String> = arrayOf()

                                        for( week in week_to_day_symbol_list){
                                            for(period in period_list){

                                                val week_period = week + period

                                                val raw_data = it.get(week_period)

                                                if (raw_data != null){
                                                    val data = raw_data as Map<String, Any>
                                                    class_name_array +=  data["course"] as String
                                                    class_id_array +=  data["course_id"] as String
                                                    class_week_to_day_array += data["week_to_day"] as String
                                                }


                                            }
                                        }

                                        task_dialog(context).course_selecter_dialog(class_name_array, class_id_array, class_week_to_day_array, semester_id, semester)

                                    }
                                    .addOnFailureListener{
                                        Log.d(TAG, "firedb_task.get_course_list: get user timetable -> failure")
                                    }

                        }else{
                            Log.e(TAG, "firedb_task.get_course_list: university_id/semester_id is null")
                        }



                    }
                    .addOnFailureListener {
                        Log.d(TAG, "firedb_task.get_course_list: get user data -> failure")
                    }

        }else{
            Log.e(TAG, "firedb_task.get_course_list: not login")
        }
    }

    fun create_task(task_data: Map<String, Any> , semester_id: String){
        if(login_cheack()){
            firedb.collection("user")
                    .document(get_uid())
                    .get()
                    .addOnSuccessListener {
                        Log.d(TAG, "firedb_task -> create_task -> get user data -> success")

                        val university_id = it.getString("university_id")

                        val class_data = task_data["class"] as Map<String, Any>
                        val class_id = class_data["class_id"] as String
                        val week_to_day = class_data["week_to_day"] as String


                        if(university_id != null && week_to_day != null && class_id != null){
                            val task_doc = firedb.collection("university")
                                    .document(university_id)
                                    .collection("semester")
                                    .document(semester_id)
                                    .collection(week_to_day)
                                    .document(class_id)
                                    .collection("task")
                                    .document()

                            val timelimit = "${task_data["day"]} ${task_data["time"]}"

                            val set_data = hashMapOf(
                                    "task_id" to task_doc.id,
                                    "timelimit" to timelimit,
                                    "task_name" to task_data["task_title"],
                                    "note" to task_data["note"]
                            )

                            task_doc.set(set_data)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "add task -> success")
                                        Toast.makeText(context, "課題が追加されました。", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Log.d(TAG, "add task -> failure")
                                        Toast.makeText(context, "課題が追加できませんでした。", Toast.LENGTH_SHORT).show()
                                    }
                        }else{
                            Log.w(TAG, "firedb_task -> create_task -> university_id is null")
                        }
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "firedb_task -> create_task -> get user data -> failure")
                    }
        }else{
            Log.w(TAG, "firedb_task -> create_task -> not login")
        }
    }
}
