package com.example.kerkar_university

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
                        Log.d("hoge", "semester: ${item.id}")


                    }
                    Log.d("hoge", "semester: ${semester}")
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

    fun get_course_data(week_to_day: String, period: Int){
        var str: String? = null

        if(login_cheack()){
            val uid = get_uid()
            firedb.collection("user")
                    .document(uid)
                    .get()
                    .addOnSuccessListener {

                        val semester = it.getString("semester")

                        if (semester != null) {
                            firedb.collection("user")
                                    .document(uid)
                                    .collection("semester")
                                    .document(semester)
                                    .get()
                                    .addOnSuccessListener {
                                        Log.d(TAG, "get_course_data${week_to_day + period} -> success")
                                        val raw_data = it.get(week_to_day+period)

                                        if (raw_data != null){
                                            val map_data = raw_data as Map<String, Any>
                                            str = "教科: ${map_data["course"]}\n" +
                                                    "教室: ${map_data["room"]}\n"

                                            val teacher = map_data["lecturer"] as List<String>
                                            if(teacher.size > 1){
                                                str += "講師: ${teacher[0]} ...他"
                                            }else{
                                                str += "講師: ${teacher[0]}"
                                            }
                                        }else{
                                            str = "授業が登録されていません"
                                        }

                                    }
                                    .addOnFailureListener {}
                        }





                    }
                    .addOnFailureListener {
                        Log.e(TAG, "get_course_data -> failure")
                    }
        }
    }


}
