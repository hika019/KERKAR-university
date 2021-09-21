package jp.hika019.kerkar_university

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import jp.hika019.kerkar_university.Home.Home_task_list_CustomAdapter
import jp.hika019.kerkar_university.Task.task_cmp_list_CustomAdapter
import jp.hika019.kerkar_university.Task.task_notcmp_list_CustomAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import jp.hika019.kerkar_university.Course_detail.Course_detail_CustomAdapter
import jp.hika019.kerkar_university.Setup.SetupActivity
import jp.hika019.kerkar_university.Timetable.Create_timetableActivity
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import jp.hika019.kerkar_university.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_task.view.*


//要リファクタリング

private val TAG = "firedb"

val settings = FirebaseFirestoreSettings.Builder()
        .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
        .build()

var firedb = FirebaseFirestore.getInstance()



fun login_cheack(): Boolean {
    val cheack_user = Firebase.auth.currentUser
    Log.d(TAG, "login check: ${cheack_user != null}")
    return cheack_user != null
}

open class firedb_setup(): firedb_col_doc(){
    val TAG = "firedb_setup" +TAG_hoge

    fun start(context: Context) {
        Log.d(TAG, "start() -> call")
        //Log.d(TAG, "local_uid: "+ local_uid)


        val auth = Firebase.auth
        Log.d(TAG, "uid: ${auth.uid}")
        if (auth.uid != null){
            uid = auth.uid
            check_user_data(context)
        }else{

            runBlocking {
                create_user(context)
            }
            //Log.d("Main", "uid: "+ uid)
            Log.d(TAG, uid.toString())

            //firedb_register_login(this).get_university_list_LoadActivity()
        }
    }

    fun create_user(context: Context) {
        Log.d(TAG, "create_user() -> call")
        val auth = Firebase.auth
        runBlocking{
            auth.signInAnonymously()
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Log.d(TAG, "signInAnonymously -> success")
                        Log.d(TAG, auth.uid.toString())

                        try{
                            uid = auth.uid
                            Log.d(TAG, "create_user -> end")
                            check_user_data(context)
                            return@addOnCompleteListener

                        }catch (e: Exception){
                            Toast.makeText(context, "error: "+e.message, Toast.LENGTH_LONG)
                            Log.w(TAG, "error: "+e.message)
                        }

                    }else{
                        Toast.makeText(context, "error: Can't sign in", Toast.LENGTH_LONG)
                        Log.w(TAG, "signInAnonymously -> failure")
                    }
                }
        }

    }

    fun create_university(context: Context, university_name: String, term: Int){
        Log.d(TAG, "create_university -> call")

        val doc_id = firedb.collection("university")
            .document()

        val data = hashMapOf(
            "university" to university_name,
            "university_id" to doc_id.id,
            "term" to term
        )

        doc_id.set(data)
            .addOnCompleteListener{
                Log.d(TAG, "create university -> Complete")
                if (it.isSuccessful) {
                    Log.d(TAG, "create university -> Successful")
                    create_user_data(context, university_name, doc_id.id)
                }
                else Log.w(TAG, "create university -> Failure")
            }
    }

    fun create_user_data(context: Context,  university: String, university_id: String){
        Log.d(TAG, "create_user_data -> call")

        //val time = SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date())
        val time = FieldValue.serverTimestamp()

        val data = hashMapOf(
            "uid" to uid,
            "university" to university,
            "create_at" to time,
            "university_id" to university_id,
        )
        if (developer)
            data["developer"] = true

        user_doc()
            .set(data)
            .addOnCompleteListener {
                Log.d(TAG, "create user_data -> Complete")


                val intent = Intent(context, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
            }
    }

    fun check_user_data(context: Context){

        Log.d(TAG, "cheak_user_data -> call")
        user_doc()
            .get()
            .addOnSuccessListener {
                val data = it.data
                Log.d(TAG, "cheak_user_data: get data")

                if (data != null){
                    Log.d(TAG, "data is not null")
                    val create_at = data["create_at"] as? com.google.firebase.Timestamp
                    val university = data["university"] as? String
                    //semester = data["semester"] as? String

                    uid = Firebase.auth.uid
                    university_id = data["university_id"] as? String

                    //val local_timetableId = get_timetable_id(context)


                    if(uid != null && university != null && university_id != null){
                        val timetableId = get_timetable_id(context)

                        if(timetableId != null){
                            user_doc_tt(timetableId)
                                .get()
                                .addOnSuccessListener {
                                    val data = it.data
                                    val year = data?.get("year")
                                    val term = data?.get("term")
                                    semester = "$year-$term"
                                    timetable_id.value = timetableId
                                    val tmp = data?.get("period") as Long
                                    period_num = tmp.toInt()
                                    Log.d(TAG, "semester: $semester")
                                    Log.d(TAG, "period_num: $period_num")
                                    Log.d(TAG, "timetableId: ${timetableId}")

                                    Log.d(TAG, "画面遷移(to home)")

                                    val intent = Intent(context, MainActivity::class.java)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener {
                                    uid = Firebase.auth.uid
                                    val intent = Intent(context, Create_timetableActivity::class.java)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    context.startActivity(intent)
                                }
                        }else{
                            uid = Firebase.auth.uid
                            val intent = Intent(context, SetupActivity::class.java)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            context.startActivity(intent)
                        }

                    }else{
                        uid = Firebase.auth.uid
                        val intent = Intent(context, SetupActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }
                }else{
                    uid = Firebase.auth.uid
                    val intent = Intent(context, SetupActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                }

            }
            .addOnFailureListener {
                Log.w(TAG, "cheak_user_data -> Failure")
            }
    }


}

open class firedb_col_doc(){

    fun user_doc(): DocumentReference {
        val tmp = firedb.collection("user")
            .document(uid!!)
        return tmp
    }

    fun user_collection_tt(): CollectionReference {
        return user_doc().collection("timetable")
    }

    fun user_doc_tt(timetableId: String): DocumentReference {
        val tmp = user_collection_tt()
        return tmp.document(timetableId)
    }

    fun uni(): DocumentReference {
        val tmp = firedb.collection("university")
            .document(university_id!!)
        return tmp
    }

    fun uni_semester_collection(): CollectionReference {
        val tmp = uni()
            .collection("semester")
        return tmp
    }

    fun uni_semester_doc(): DocumentReference {
        val tmp = uni_semester_collection()
        return tmp.document(semester!!)
    }

    fun uni_course_collerction(week:String, period: Int): CollectionReference {
        return uni_course_collerction(week+period)
    }

    fun uni_course_collerction(week_and_period: String): CollectionReference {
        val tmp = uni_semester_doc()
        return tmp.collection(week_and_period)
    }

    fun uni_course_doc(week_to_day: String, course_id: String): DocumentReference {
        val tmp = uni_course_collerction(week_to_day)
        return tmp.document(course_id)
    }

    fun uni_course_doc(week:String, period: Int, course_id: String): DocumentReference {
        val tmp = uni_course_collerction(week, period)
        return tmp.document(course_id)
    }

    fun uni_task_col(week_to_day: String, course_id: String): CollectionReference {
        val tmp =uni_course_doc(week_to_day, course_id)
        return tmp.collection("task")
    }

    fun uni_task_col(week:String, period: Int, course_id: String): CollectionReference {
        val tmp =uni_course_doc(week, period, course_id)
        return tmp.collection("task")
    }
}

class firedb_register_login(override val context: Context): register_dialog(context){
    override val TAG = "MainActivity_firedb_register_login" + TAG_hoge

    fun get_university_list_LoadActivity(){
        var university_name_list: Array<String> = arrayOf()
        var university_id_list: Array<String> = arrayOf()
        Log.d(TAG, "get_university_list -> call")

//        Log.d("hoge", "data: ${university_name_list}")
        runBlocking {
            firedb.collection("university")
                .addSnapshotListener { documents, error ->
                    if(error != null){
                        Log.w(TAG, "get_university_list -> failure", error)
                        return@addSnapshotListener
                    }

                    for(univer_doc in documents!!.documentChanges){
                        val university_id = univer_doc.document.id
                        val university_name = univer_doc.document.getString("university")!!

                        university_id_list += university_id
                        university_name_list += university_name
                    }
                    Log.d(TAG, university_name_list.toString())

                    Log.d(TAG, "get univer list")
                    select_univarsity(university_name_list, university_id_list)
                }
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
                    create_user_data(uid!!, university_name, doc_id.id)
                }
    }

    fun create_user_data(uid: String, university: String, university_id: String){

        Log.d(TAG, "create_user_data -> call")

        val data = hashMapOf(
            "uid" to uid,
            "university" to university,
            "create_at" to Timestamp(Date()),
            "university_id" to university_id,
        )

//        firedb.collection("user")
//            .document(uid)
//            .set(data)
//            .addOnSuccessListener {
//                Log.d(TAG, "create_user_data: set -> success")
//                val i = Intent(context, MainActivity::class.java)
//                context.startActivity()
//            }
//            .addOnFailureListener {
//                Log.w(TAG, "create_user_data: set -> failure", it)
//            }

    }


}

open class firedb_timetable(context: Context){
    private val TAG = "firedb_timetable"+ TAG_hoge
    open val context: Context = context

    fun courses_is_none(){
        Log.d(TAG, "courses_is_none -> call")

        firedb.collection("user")
            .document(uid!!)
            .collection("semester")
            .get()
            .addOnSuccessListener {
                Log.d(TAG, "courses_is_none -> success")

                if(it.isEmpty){
                    none_course(context)
                }
            }
    }

    fun get_course_list(week: String, period: Int){
        Log.d(TAG, "get_course_list -> call")
        val tmp_class = timetable_dialog(context)
        if (university_id != null && semester != null) {
            firedb.collection("university")
                .document(university_id!!)
                .collection("semester")
                .document(semester!!)
                .collection(week+period)
                .get()
                .addOnSuccessListener{
                    Log.d(TAG, "get_course_list: get courses -> success")

                    val course_list: ArrayList<Any> = arrayListOf()

                    for(document in it){
                        val tmpdata = document.data
                        val data = hashMapOf<String, Any>(
                            "course_id" to document.id,
                            "week_to_day" to tmpdata?.get("week_to_day") as String,
                            "course" to tmpdata?.get("course") as String,
                            "lecturer" to tmpdata?.get("lecturer") as ArrayList<String>,
                            "room" to tmpdata?.get("room") as String
                        )
                        course_list.add(data)
                    }

                    tmp_class.search_timetable_dialog(week, period, course_list, semester!!)
                }
                .addOnFailureListener {
                    Log.w(TAG, "get_course_list: get courses -> failure")
                }
        }else{
            Log.w(TAG, "get_course_list: university_id/semester_id is null")
        }
    }

    fun get_course_data(week_and_period: String){
        get_course_data(week_and_period.substring(0, 3), week_and_period.substring(3).toInt())
    }

    fun get_course_data(week_to_day: String, period: Int){
        var str = "授業が登録されていません"
        Log.d(TAG, "get_course_data -> call")
        val user_doc = firedb.collection("user")
                .document(uid!!)

        val dialog_class = timetable_dialog(context)
        val tt_id = get_timetable_id(context)

        if (week_to_day == "sun"){
            dialog_class.sun_timetable_dialog(week_to_day)
        }else

        if (tt_id != null) {
            user_doc.collection("timetable")
                .document(tt_id)
                .get()
                .addOnSuccessListener {
                    Log.d(TAG, "get_course_data(user)${week_to_day + period} -> success")

                    val raw_data = it.get(week_to_day+period) as? Map<String, Any>
                    val course_id = raw_data?.get("course_id") as? String
                    Log.d(TAG, course_id.toString())

                    if (course_id != null) {
                        firedb.collection("university")
                            .document(university_id!!)
                            .collection("semester")
                            .document(semester!!)
                            .collection(week_to_day+period)
                            .document(course_id)
                            .get()
                            .addOnSuccessListener {
                                Log.d(TAG, "get_course_data${week_to_day + period} -> success")
                                val data = it.data
                                if (data != null){
                                    str = course_data_map_to_str(data as Map<String, Any>)
                                }
                                dialog_class.timetable_data_dialog(week_to_day, period, str)
                            }
                            .addOnFailureListener {
                                Log.w(TAG, "get_course_data(university)${week_to_day + period} -> failure")
                                Toast.makeText(context, "通信エラー", Toast.LENGTH_SHORT).show()
                            }
                    }else{
                        dialog_class.timetable_data_dialog(week_to_day, period, str)
                    }
                }
                .addOnFailureListener {
                    Log.w(TAG, "get_course_data(user)${week_to_day + period} -> failure")
                    Toast.makeText(context, "通信エラー", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun create_course_university_timetable(data: Map<String, Any>){
        Log.d(TAG, "create_course_university_timetable -> call")

        val course_data = mutableMapOf(
                "week_to_day" to data["week_to_day"] as String,
                "course" to data["course"] as String,
                "lecturer" to data["lecturer"] as List<String>,
                "room" to data["room"] as String
        )

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

                add_user_timetable(data["week_to_day"] as String, doc.id)
            }
            .addOnFailureListener{
                Log.d(TAG, "create_university_timetable: set data -> failure")
            }
    }

    fun add_user_timetable(week_to_day: String, classId: String){
        Log.d(TAG, "add_user_timetable -> call")
        val data = hashMapOf(
            week_to_day to hashMapOf(
                "course_id" to classId
            )
        )

        val timetableId = get_timetable_id(context)

        firedb.collection("user")
            .document(uid!!)
            .collection("timetable")
            .document(timetableId!!)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "add course to user: ${classId} -> success")
                local_timetable[week_to_day] = classId

            }
            .addOnFailureListener{
                Log.d(TAG, "add course to user: ${classId} -> failure")
            }
    }

    fun delete_user_timetable(week_to_day: String){
        Log.d(TAG, "delete_user_timetable -> call")

        val updates = hashMapOf<String, Any>(
                week_to_day to FieldValue.delete()
        )
        firedb.collection("user")
                .document(uid!!)
                .collection("timetable")
                .document(timetable_id.value!!)
                .update(updates)
                .addOnCompleteListener{
                    course_data_live = MutableLiveData()
                    Log.d(TAG, "delete_user_timetable: $week_to_day -> complete")

                    createtimetable_finish.value = true
                }
    }


}


//新規
class firedb_timetable_new(): firedb_col_doc(){
    private val TAG = "firedb_timetable_new" + TAG_hoge

    fun get_user_timetable_all_data(context: Context){
        Log.d(TAG, "get_user_timetable_all_data -> call")
        user_doc_tt(timetable_id.value!!)
            .addSnapshotListener { value, error ->

                if (error != null){
                    Log.w(TAG, "get_timetable_all_data -> error")
                    AlertDialog.Builder(context)
                        .setTitle("エラー")
                        .setMessage("通信エラーが発生しました\n再起動してください")
                        .setPositiveButton("OK"){_, _ ->

                        }.show()
                    return@addSnapshotListener
                }
                user_timetable_data_live.value = value?.data
                week_num = value?.getLong("wtd")!!.toInt()
                period_num = value?.getLong("period")!!.toInt()
                Log.d(TAG, "user_timetable_data_live ${user_timetable_data_live.value}")

            }
    }

    fun get_user_course_data(
        week: String,
        period: Int
    ){
        //Log.d(TAG, "get_user_course_data($week$period) -> call ")
        val week_and_period = "$week$period"
        val user_timetable = user_timetable_data_live.value
        val course_data = user_timetable?.get(week_and_period) as? Map<String, Any?>
        if (course_data != null){
            val course_id = course_data["course_id"] as String
            uni_course_doc(week_and_period, course_id)
                    .get()
                .addOnSuccessListener {
                    val online_data = it.data
                    Log.d(TAG, "get_user_course_data($week_and_period): $online_data")
                    if (online_data != null){
                        val course_name = online_data["course"] as String
                        val lecturer = online_data["lecturer"] as List<String>
                        val room = online_data["room"] as String

                        val tmp_data = mapOf(
                            "course" to course_name,
                            "lecturer" to lecturer,
                            "room" to room
                        )
                        var tmp = course_data_live.value
                        if (tmp != null) {
                            tmp.put(week_and_period, tmp_data)
                        }else{
                            tmp = mutableMapOf(week_and_period to tmp_data)
                        }
                        course_data_live.value = tmp

                        Log.d(TAG, "course_data_live: ${course_data_live.value}")
                    }

                }
                .addOnFailureListener {

                }
        }
    }

    fun check_user_timetable(context: Context){
        Log.d(TAG, "check_user_timetable -> call")
        if (timetable_id.value != null){
            get_user_timetable_all_data(context)
        }else{
            user_collection_tt()
                .get()
                .addOnSuccessListener {
                    val user_timetable_nume = it.documents.size

                    val dialog = AlertDialog.Builder(context)
                        .setTitle("時間割の追加")
                        .setMessage("時間割を作成してください")

                        .setPositiveButton("作成"){ _, _ ->
                            val i = Intent(context, Create_timetableActivity::class.java)
                            context.startActivity(i)
                            false
                        }
                    dialog.create()
                        .setCanceledOnTouchOutside(false)
                    dialog.setCancelable(false)


                    if (user_timetable_nume == 0){

                        dialog.show()


                    }else{
                        dialog.setMessage("時間割選択画面がないので新たに作ってください")
                            .show()
                    }
                }
                .addOnFailureListener {

                }



        }
    }

    fun create_timetable(context: Context, tt_data: Map<String, Any?>){
        Log.d(TAG, "create_timetable -> call")
        val doc_id = tt_data["timetable_id"] as String
        val year = tt_data["year"] as Int
        val term = tt_data["term"] as Int
        val week = tt_data["wtd"] as Int
        val period = tt_data["period"] as Int

        user_doc_tt(doc_id)
            .set(tt_data)
            .addOnSuccessListener {
                Log.d(TAG, "create_timetable -> success")

                semester = "$year-$term"
                week_num = week
                period_num = period
                timetable_id.value = doc_id
                set_timetable_id(context, timetable_id.value!!)
                Log.d(TAG, "semester: $semester")
                Log.d(TAG, "timetable_id: ${timetable_id.value}")

                user_timetable_data_live = MutableLiveData<Map<String, Any?>>()
                course_data_live = MutableLiveData<MutableMap<String, Any?>>()
                Log.d(TAG, "user_timetable_data_live: ${user_timetable_data_live.value}")
                Log.d(TAG, "course_data_live: ${course_data_live.value}")

                Log.d(TAG, "user_timetable_data_live & course_data_live -> clear")

                createtimetable_finish.value = true

            }
            .addOnFailureListener {
                Log.w(TAG, "create_timetable -> failure")
                Toast.makeText(context, "追加に失敗しました", Toast.LENGTH_SHORT).show()
            }
    }

    fun get_all_course_id(context: Context){
        Log.d(TAG, "get_all_course_id -> call")

        val tt_id = get_timetable_id(context)

        user_doc_tt(tt_id!!)
            .addSnapshotListener{ value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                val hoge = value?.data
                //Log.d("hogee", "hoge: $hoge")

                if (hoge != null){

                    val wtd = hoge["wtd"] as Long
                    week_num = wtd.toInt()
                    val period = hoge["period"] as Long
                    period_num = period.toInt()



                    test_course_data_map = mutableMapOf()
                    for (week in week_to_day_symbol_list){
                        for(period in period_list){
                            val week_period = week+period.toString()
                            val tmp = hoge.get(week_period) as? Map<String, String>
                            //Log.d("hogee", "aaa$tmp")

                            if (tmp != null){
                                //test_course_id_map[week_period!!] = tmp["course_id"]
                                get_course_data(week_period, tmp["course_id"]!!)

                                //data[week_period] = tmp["course_id"]
                                //test_course_id.value = data
                                //Log.d("hogee", "data: ${test_course_id.value}")
                                //Log.d("hogee", "data2: ${data}")

                            }
                        }
                    }
                }
            }
    }

    fun get_course_data(week_period: String, course_id: String){
        Log.d(TAG, "get_course_data -> call")
        Log.d(TAG, "week_period: $week_period, id: $course_id")

        uni_course_doc(week_period, course_id)
            .get()
            .addOnSuccessListener {
                val data = it.data

                if (data != null) {
                    val course_name = data["course"] as String
                    val lecturer = data["lecturer"] as ArrayList<String>

                    val in_data = mapOf<String, Any?>(
                        "course" to course_name,
                        "lecturer" to lecturer,
                        "course_id" to course_id
                    )

                    //Log.d(TAG, "wee: $week_period")
                    //Log.d(TAG, "hoge: $hoge")

                    test_course_data_map.put(week_period, in_data)
                    Log.d(TAG, "course data map: ${test_course_data_map}")
                }
            }
            .addOnFailureListener {
                Log.w(TAG, "get_course_data -> failure: $week_period /$course_id: ", it)
            }
    }

}


class firedb_task(val context: Context): firedb_col_doc(){
    private val TAG = "firedb_task" +TAG_hoge


    fun get_course_list(){
        Log.d(TAG, "get_course_list -> call")

        user_doc_tt(timetable_id.value!!)
            .get()
            .addOnSuccessListener {
                Log.d(TAG, "firedb_task.get_course_list: get user data -> success")
                val user_data = it.data

                var class_data_array = arrayListOf<Any>()

                var course_num = 0

                for (week in week_to_day_symbol_list){
                    for (period in period_list){
                        val week_and_period = week + period
                        val class_data = user_data?.get(week_and_period) as? Map<*, *>
                        if(user_timetable_data_live.value?.get(week_and_period) != null){
                            course_num += 1
                        }

                        if (class_data != null){
                            val class_id = class_data.get("course_id") as String
                            //Log.d("hogee", "class_id: $class_id")

                            uni_course_doc(week_and_period, class_id)
                                .get()
                                .addOnSuccessListener {
                                    val course_name = it.getString("course")!!

                                    val tmp = hashMapOf(
                                        "course_id" to class_id,
                                        "week_to_day" to week_and_period,
                                        "course" to course_name
                                    )

                                    class_data_array.add(tmp)
                                    Log.d(TAG, "class_data_array: $class_data_array")
                                    if (course_num == class_data_array.size){
                                        if (class_data_array.isEmpty()){
                                            none_Timetable(context)
                                        }else{
                                            task_dialog(context).course_selecter_dialog(class_data_array)
                                        }
                                    }
                                }
                            }

                    }
                }


            }
            .addOnFailureListener {

            }


    }

    fun create_task(task_data: Map<String, Any>){
        Log.d(TAG, "create_task -> call")
        firedb.collection("user")
                .document(uid!!)
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
                                .document(semester!!)
                                .collection(week_to_day)
                                .document(class_id)
                                .collection("task")
                                .document()

                        val time_limit = "${task_data["day"]} ${task_data["time"]}"

                        val set_data = hashMapOf(
                                "task_id" to task_doc.id,
                                "time_limit" to task_data["time_limit"],
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
    }

    fun get_not_comp_task_list(view: View){
        Log.d(TAG, "get_not_comp_task_list -> call")


        get_timetable_id(context)?.let {
            user_doc_tt(it)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w(TAG, "get_comp_task_list -> get user class_data -> failure", error)
                        return@addSnapshotListener
                    }

                    Log.d(TAG, "get_not_comp_task_list -> get user class_data -> success")

                    var all_comp_task: Array<String> = arrayOf()
                    //履修中の授業取得
                    var task_list: ArrayList<Map<String, Any>> = arrayListOf()
                    val user_data = value?.data

                    for (week in week_to_day_symbol_list) {
                        for (period in period_list) {

                            val user_class_data = user_data?.get(week + period) as? MutableMap<String, Any>

                            if (user_class_data != null){
                                val course_id = user_class_data["course_id"] as String
                                val comp_task = user_class_data["comp_task"] as? ArrayList<String?>
                                all_comp_task = add_array_to_array(all_comp_task, comp_task)
                                Log.d(TAG, "user_class_data: $user_class_data")


                                //授業の詳細の取得
                                Log.d(TAG, "course_id: $course_id")

                                uni_course_doc(week, period, course_id)
                                    .get()
                                    .addOnSuccessListener {
                                        Log.d(TAG, "get 授業のデータ -> success")
                                        val class_data = it.data
                                        Log.d(TAG, "class: $class_data")
                                        if (class_data != null) {
                                            uni_task_col(week, period, course_id)
                                                .orderBy("time_limit")
                                                .addSnapshotListener { value, error ->
                                                    if(error != null){
                                                        Log.w(TAG, "get_not_comp_task_list -> error", error)
                                                        return@addSnapshotListener
                                                    }

                                                    //課題取得
                                                    for (task_item in value!!) {
                                                        val task_data = task_item.data
                                                        val task_id = task_data["task_id"] as? String

                                                        task_data["class_data"] = class_data

                                                        if (!(all_comp_task.contains(task_id))) {
                                                            task_list.add(task_data)
                                                        }
                                                        Log.d(TAG, "datas: $task_data")
                                                        //Log.d(TAG, "datas: $task_list")

                                                        task_list.sortBy { it.get("time_limit") as Timestamp }

                                                        //表示
                                                        //Log.d(TAG, "tasks show to recyclerview")
                                                        val adapter = task_notcmp_list_CustomAdapter(task_list, context)
                                                        val layoutManager = LinearLayoutManager(context)

                                                        view.AssignmentActivity_assignment_recyclerView.layoutManager = layoutManager
                                                        view.AssignmentActivity_assignment_recyclerView.adapter = adapter
                                                        view.AssignmentActivity_assignment_recyclerView.setHasFixedSize(true)
                                                    }
                                                }


                                        } else {
                                            Log.d(TAG, "get_not_comp_task_list: class_data is null")
                                        }
                                    }
                            }
                        }
                    }
                }
        }


    }

    fun get_comp_task_list(view: View){
        Log.d(TAG, "get_comp_task_list -> call")
        val user_doc = firedb.collection("user")
                .document(uid!!)

        user_doc_tt(get_timetable_id(context)!!)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "get_comp_task_list -> get user class_data -> failure", error)
                    return@addSnapshotListener
                }

                Log.d(TAG, "get_comp_task_list -> get user class_data -> success")
                var all_comp_task: Array<String> = arrayOf()
                //履修中の授業取得
                var task_list: ArrayList<Map<String, Any>> = arrayListOf()
                val user_data = value?.data

                for (week in week_to_day_symbol_list) {
                    for (period in period_list) {
                        val user_class_data = user_data?.get(week + period) as MutableMap<String, Any>?

                        //授業のデータ
                        if (user_class_data != null){
                            val comp_task = user_class_data["comp_task"] as? ArrayList<String?>
                            val course_id = user_class_data["course_id"] as String
                            all_comp_task = add_array_to_array(all_comp_task, comp_task)

                            uni_course_doc(week, period, course_id)
                                .get()
                                .addOnSuccessListener {
                                    Log.d(TAG, "get 授業のデータ -> success")
                                    val class_data = it.data
                                    //履修中の授業取得
                                    if (class_data != null) {

                                        uni_task_col(week, period, course_id)
                                            .orderBy("time_limit")
                                            .addSnapshotListener { value, error ->
                                                if (error != null){
                                                    Log.w(TAG, "get_comp_task_list -> error", error)
                                                    return@addSnapshotListener
                                                }
                                                for (task_item in value!!.documentChanges) {
                                                    val task_data = task_item.document.getData() as MutableMap<String, Any>
                                                    val task_id = task_data["task_id"] as String
                                                    task_data["class_data"] = class_data

                                                    if (all_comp_task.contains(task_id)) {
                                                        task_list.add(task_data)
                                                    }

                                                    task_list.sortByDescending { it.get("time_limit") as Timestamp }

                                                    //表示
                                                    Log.d(TAG, "tasks show to recyclerview")
                                                    val adapter = task_cmp_list_CustomAdapter(task_list, context, semester!!)
                                                    val layoutManager = LinearLayoutManager(context)

                                                    view.AssignmentActivity_assignment_recyclerView.layoutManager = layoutManager
                                                    view.AssignmentActivity_assignment_recyclerView.adapter = adapter
                                                    view.AssignmentActivity_assignment_recyclerView.setHasFixedSize(true)
                                                }
                                            }
                                    } else {
                                        Log.d(TAG, "get_comp_task_list: class_data is null")
                                    }
                                }
                                .addOnFailureListener {
                                    Log.w(TAG, "get 授業のデータ -> failure", it)
                                }

                        }


                    }
                }
            }
    }



    fun get_tomorrow_not_comp_task_list(view: FragmentHomeBinding){
        Log.d(TAG, "get_tomorrow_not_comp_task_list -> call")

        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, 1)
        var tomorrow = cal.time


//            Log.d("hoge", "to_int:${time_limit_int}")
        view.mainTaskInfoRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        user_doc_tt(get_timetable_id(context)!!)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "get_tomorrow_not_comp_task_list -> get user class_data -> failure", error)
                    return@addSnapshotListener
                }
                Log.d(TAG, "get_tomorrow_not_comp_task_list -> get user class_data -> success")

                var all_comp_task: Array<String> = arrayOf()
                //履修中の授業取得
                var task_list: ArrayList<Map<String, Any>> = arrayListOf()
                val user_data = value?.data

                for (week in week_to_day_symbol_list) {
                    for (period in period_list) {
                        val user_class_data = user_data?.get(week + period) as MutableMap<String, Any>?

                        if (user_class_data != null) {
                            val course_id = user_class_data["course_id"] as String
                            val comp_task = user_class_data["comp_task"] as? ArrayList<String?>
                            all_comp_task = add_array_to_array(all_comp_task, comp_task)

                            //履修中の授業取得
                            uni_course_doc(week, period, course_id)
                                .get()
                                .addOnSuccessListener {
                                    Log.d(TAG, "get 授業のデータ -> success")
                                    val class_data = it.data

                                    if (class_data != null){
                                        uni_task_col(week, period, course_id)
                                            .orderBy("time_limit")
                                            .addSnapshotListener { value, error ->
                                                if (error != null){
                                                    Log.w(TAG, "get_tomorrow_not_comp_task_list -> error", error)
                                                    return@addSnapshotListener
                                                }

                                                for (task_item in value!!.documentChanges) {
                                                    var task_data = task_item.document.getData() as MutableMap<String, Any>
                                                    val task_id = task_data["task_id"] as? String
                                                    task_data["class_data"] = class_data

                                                    Log.d(TAG, "data: $task_data")

                                                    val time_limit = task_data["time_limit"] as Timestamp

                                                    cal.time = time_limit.toDate()

                                                    val task_timelimit = cal.time



                                                    if (!(all_comp_task.contains(task_id)) && task_timelimit.before(tomorrow)) {
                                                        task_list.add(task_data)
                                                    }

                                                    task_list.sortBy { it.get("time_limit") as Timestamp }

                                                    //表示
                                                    Log.d(TAG, "tasks show to recyclerview")
                                                    val adapter = Home_task_list_CustomAdapter(task_list, context, semester!!)
                                                    val layoutManager = LinearLayoutManager(context)


                                                    view.mainTaskInfoRecyclerview.layoutManager = layoutManager
                                                    view.mainTaskInfoRecyclerview.adapter = adapter
                                                    view.mainTaskInfoRecyclerview.setHasFixedSize(true)

                                                }
                                            }
                                    }

                                }
                                .addOnFailureListener {
                                    Log.w(TAG, "get 授業のデータ -> failure")
                                }

                        } else {
                            Log.d(TAG, "get_tomorrow_not_comp_task_list: class_data is null")
                        }
                    }
                }
            }
    }

    fun task_to_comp(class_and_task_data: Map<String, Any>){
        Log.d(TAG, "task_to_comp -> call")
        val class_data= class_and_task_data["class_data"] as Map<String, Any>
        val task_id = class_and_task_data["task_id"] as String
        val week_to_day = class_data["week_to_day"] as String


        user_doc_tt(get_timetable_id(context)!!)
            .get()
            .addOnSuccessListener {
                Log.w(TAG, "get course data -> success")
                val online_class_data = it.get(week_to_day) as MutableMap<String, Any?>
                var comp_task = online_class_data["comp_task"] as? ArrayList<String?>


                if (comp_task != null) {
                    comp_task.add(task_id)
                }else{
                    comp_task = arrayListOf(task_id)
                }

                online_class_data["comp_task"] = comp_task

                user_doc_tt(get_timetable_id(context)!!)
                    .update(week_to_day, online_class_data)
                    .addOnSuccessListener {
                        Log.d(TAG, "task_to_compe -> success")
                        Toast.makeText(context, "提出済みにしました",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "task_to_compe -> failure")
                        Toast.makeText(context, "提出済みにできませんでした",Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener {
                Log.w(TAG, "get course data -> failure")
                Toast.makeText(context, "提出済みにできませんでした",Toast.LENGTH_SHORT).show()
            }


    }

    fun task_to_notcomp(class_and_task_data: Map<String, Any>){
        Log.d(TAG, "task_to_notcomp -> call")
        val class_data= class_and_task_data["class_data"] as Map<String, Any>
        val task_id = class_and_task_data["task_id"] as String
        val week_to_day = class_data["week_to_day"] as String



        user_doc_tt(get_timetable_id(context)!!)
            .get()
            .addOnSuccessListener {
                Log.w(TAG, "get course data -> success")
                val online_class_data = it.get(week_to_day) as MutableMap<String, Any?>
                val comp_task = online_class_data["comp_task"] as MutableList<String>

                comp_task -= task_id
                online_class_data["comp_task"] = comp_task

                user_doc_tt(get_timetable_id(context)!!)
                    .update(week_to_day, online_class_data)
                    .addOnSuccessListener {
                        Log.d(TAG, "task_to_notcomp -> success")
                        Toast.makeText(context, "未提出にしました",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "task_to_notcomp -> failure")
                        Toast.makeText(context, "未提出にできませんでした",Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Log.w(TAG, "task_to_notcomp -> failure")
                Toast.makeText(context, "未提出にできませんでした",Toast.LENGTH_SHORT).show()
            }

    }
}

class firedb_task_new(): firedb_col_doc(){
    private val TAG = "firedb_task_new" + TAG_hoge

    fun get_course_task(recycle_view: RecyclerView, week_to_day: String, course_id: String, context: Context){
        Log.d(TAG, "get_course_task -> call")

        val course_datas = user_timetable_data_live.value?.get(week_to_day) as? Map<String, Any?>
        val comp_task = course_datas?.get("comp_task") as? ArrayList<String>

        uni_task_col(week_to_day, course_id)
            .orderBy("time_limit")
            .addSnapshotListener { value, error ->
                if (error != null){
                    Log.w(TAG, "get_course_task -> erro", error)
                }
                var task_list: ArrayList<Map<String, Any>> = arrayListOf()
                if (value != null) {
                    for (doc in value.documents){
                        val task_data = doc.data
                        val task_id = task_data?.get("task_id") as? String

                        if (task_data != null) {
                            task_list.add(task_data)
                        }
                        val adapter = Course_detail_CustomAdapter(task_list, comp_task, context)
                        val layoutManager = LinearLayoutManager(context)

                        recycle_view.layoutManager = layoutManager
                        recycle_view.adapter = adapter
                        recycle_view.setHasFixedSize(true)

                    }
                }
            }
    }

    fun create_task(context: Context, week_period: String, courseId: String, data: MutableMap<String, Any?>){
        Log.d(TAG, "create_task -> call")
        val doc = uni_task_col(week_period, courseId)
            .document()

        data["task_id"] = doc.id
        Log.d(TAG, "ho: $data")

        doc.set(data)
            .addOnSuccessListener {
                Log.d(TAG, "add task -> success")
                Toast.makeText(context, "課題が追加されました", Toast.LENGTH_SHORT).show()
                Thread.sleep(300)
                createtask_finish.value = true
            }
            .addOnFailureListener {
                Log.w(TAG, "add task -> failure")
                Toast.makeText(context, "課題が追加できませんでした", Toast.LENGTH_SHORT).show()
            }
    }
}
