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
import jp.hika019.kerkar_university.home.HomeTaskListCustomAdapter
import jp.hika019.kerkar_university.task.TaskCompListCustomAdapter
import jp.hika019.kerkar_university.task.TaskNotCompListCustomAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import jp.hika019.kerkar_university.courseDetail.CourseDetailCustomAdapter
import jp.hika019.kerkar_university.selectTimetable.SelectTimetableActivity
import jp.hika019.kerkar_university.setup.SetupActivity
import jp.hika019.kerkar_university.timetable.CreateTimetableActivity
import jp.hika019.kerkar_university.databinding.ActivitySelectCourseBinding
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import jp.hika019.kerkar_university.databinding.FragmentHomeBinding
import jp.hika019.kerkar_university.selectCourse.SelectCourseCustomAdapter
import kotlinx.android.synthetic.main.fragment_task.view.*


//要リファクタリング

const val TAG = "firedb"

val settings = FirebaseFirestoreSettings.Builder()
        .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
        .build()

var firedb = FirebaseFirestore.getInstance()




open class FiredbSetup(): firedbColDoc(){
    val tag = "firedb_setup$tagHoge"

    fun start(context: Context) {
        Log.d(tag, "start() -> call")
        //Log.d(TAG, "local_uid: "+ local_uid)


        val auth = Firebase.auth
        Log.d(tag, "uid: ${auth.uid}")
        if (auth.uid != null){
            uid = auth.uid
            checkUserData(context)
        }else{

            runBlocking {
                createUser(context)
            }
            //Log.d("Main", "uid: "+ uid)
            Log.d(tag, uid.toString())

            //firedb_register_login(this).get_university_list_LoadActivity()
        }
    }

    private fun createUser(context: Context) {
        Log.d(tag, "create_user() -> call")
        val auth = Firebase.auth
        runBlocking{
            auth.signInAnonymously()
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Log.d(tag, "signInAnonymously -> success")
                        Log.d(tag, auth.uid.toString())

                        try{
                            uid = auth.uid
                            Log.d(tag, "create_user -> end")
                            checkUserData(context)
                            return@addOnCompleteListener

                        }catch (e: Exception){
                            Toast.makeText(context, "error: "+e.message, Toast.LENGTH_LONG)
                            Log.w(tag, "error: "+e.message)
                        }

                    }else{
                        Toast.makeText(context, "error: Can't sign in", Toast.LENGTH_LONG)
                        Log.w(tag, "signInAnonymously -> failure")
                    }
                }
        }

    }

    fun createUniversity(context: Context, university_name: String, term: Int){
        Log.d(tag, "create_university -> call")

        val docId = firedb.collection("university")
            .document()

        val data = hashMapOf(
            "university" to university_name,
            "university_id" to docId.id,
            "term" to term
        )

        docId.set(data)
            .addOnCompleteListener{
                Log.d(tag, "create university -> Complete")
                if (it.isSuccessful) {
                    Log.d(tag, "create university -> Successful")
                    createUserData(context, university_name, docId.id)
                }
                else Log.w(tag, "create university -> Failure")
            }
    }

    fun createUserData(context: Context, university: String, university_id: String){
        Log.d(tag, "create_user_data -> call")

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

        userDoc()
            .set(data)
            .addOnCompleteListener {
                Log.d(tag, "create user_data -> Complete")


                val intent = Intent(context, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
            }
    }

    private fun checkUserData(context: Context){

        Log.d(tag, "cheak_user_data -> call")
        userDoc()
            .get()
            .addOnSuccessListener { it ->
                val data = it.data
                Log.d(tag, "cheak_user_data: get data")

                if (data != null){
                    Log.d(tag, "data is not null")
                    val university = data["university"] as? String
                    //semester = data["semester"] as? String

                    uid = Firebase.auth.uid
                    university_id = data["university_id"] as? String

                    //val local_timetableId = get_timetable_id(context)


                    if(uid != null && university != null && university_id != null){
                        val timetableId = getTimetableId(context)

                        Log.d(tag, "tt: $timetableId")
                        if(timetableId != null){
                            userDocTt(timetableId)
                                .get()
                                .addOnSuccessListener {
                                    val data = it.data
                                    if (data != null){
                                        Log.d(tag, "data isnot null")
                                        val year = data?.get("year")
                                        val term = data?.get("term")
                                        semester = "$year-$term"
                                        timetable_id.value = timetableId
                                        val tmp = data?.get("period") as? Long
                                        period_num = tmp?.toInt()!!
                                        Log.d(tag, "semester: $semester")
                                        Log.d(tag, "period_num: $period_num")
                                        Log.d(tag, "timetableId: $timetableId")

                                        Log.d(tag, "画面遷移(to home)")

                                        val intent = Intent(context, MainActivity::class.java)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        context.startActivity(intent)
                                    }else{

                                        Toast.makeText(context, "データの取得に失敗しました", Toast.LENGTH_SHORT).show()
                                    }

                                }
                                .addOnFailureListener {
                                    uid = Firebase.auth.uid
                                    val intent = Intent(context, CreateTimetableActivity::class.java)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    context.startActivity(intent)
                                }

                        }else{
                            val i = Intent(context, SelectTimetableActivity::class.java)
                            context.startActivity(i)
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
                Log.w(tag, "cheak_user_data -> Failure")
            }
    }


}

open class firedbColDoc(){

    fun userDoc(): DocumentReference {
        val tmp = firedb.collection("user")
            .document(uid!!)
        return tmp
    }

    fun userCollectionTt(): CollectionReference {
        return userDoc().collection("timetable")
    }

    fun userDocTt(timetableId: String): DocumentReference {
        val tmp = userCollectionTt()
        return tmp.document(timetableId)
    }


    private fun uni(): DocumentReference {
        val tmp = firedb.collection("university")
            .document(university_id!!)
        return tmp
    }

    private fun uniSemesterCollection(): CollectionReference {
        return uni()
            .collection("semester")
    }

    private fun uniSemesterDoc(): DocumentReference {
        val tmp = uniSemesterCollection()
        return tmp.document(semester!!)
    }

    private fun uniCourseCollection(week:String, period: Int): CollectionReference {
        return uniCourseCollerction(week+period)
    }

    fun uniCourseCollerction(week_and_period: String): CollectionReference {
        val tmp = uniSemesterDoc()
        return tmp.collection(week_and_period)
    }

    fun uniCourseDoc(week_to_day: String, course_id: String): DocumentReference {
        val tmp = uniCourseCollerction(week_to_day)
        return tmp.document(course_id)
    }

    fun uniCourseDoc(week:String, period: Int, course_id: String): DocumentReference {
        val tmp = uniCourseCollection(week, period)
        return tmp.document(course_id)
    }

    fun uniTaskCol(week_to_day: String, course_id: String): CollectionReference {
        val tmp =uniCourseDoc(week_to_day, course_id)
        return tmp.collection("task")
    }

    fun uniTaskCol(week:String, period: Int, course_id: String): CollectionReference {
        val tmp =uniCourseDoc(week, period, course_id)
        return tmp.collection("task")
    }
}

open class firedb_timetable(context: Context){
    private val tag = "firedb_timetable$tagHoge"
    open val context: Context = context

    fun getCourseList(week: String, period: Int){
        Log.d(tag, "get_course_list -> call")
        val tmpClass = TimetableDialog(context)
        if (university_id != null && semester != null) {
            firedb.collection("university")
                .document(university_id!!)
                .collection("semester")
                .document(semester!!)
                .collection(week+period)
                .get()
                .addOnSuccessListener{
                    Log.d(tag, "get_course_list: get courses -> success")

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

                    tmpClass.searchTimetableDialog(week, period, course_list, semester!!)
                }
                .addOnFailureListener {
                    Log.w(tag, "get_course_list: get courses -> failure")
                }
        }else{
            Log.w(tag, "get_course_list: university_id/semester_id is null")
        }
    }

    fun createCourseUniversityTimetable(data: Map<String, Any>){
        Log.d(tag, "create_course_university_timetable -> call")

        val courseData = mutableMapOf(
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

        courseData["course_id"] = doc.id

        doc.set(courseData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(tag, "create_university_timetable: set data -> success")

                addUserTimetable(data["week_to_day"] as String, doc.id)
            }
            .addOnFailureListener{
                Log.d(tag, "create_university_timetable: set data -> failure")
            }
    }

    fun addUserTimetable(week_to_day: String, classId: String){
        Log.d(tag, "add_user_timetable -> call")
        val data = hashMapOf(
            week_to_day to hashMapOf(
                "course_id" to classId
            )
        )

        val timetableId = getTimetableId(context)

        firedb.collection("user")
            .document(uid!!)
            .collection("timetable")
            .document(timetableId!!)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(tag, "add course to user: $classId -> success")
                local_timetable[week_to_day] = classId
                createTaskFinish.value = true
            }
            .addOnFailureListener{
                Log.d(tag, "add course to user: $classId -> failure")
            }
    }

    fun deleteUserTimetable(week_to_day: String){
        Log.d(tag, "delete_user_timetable -> call")

        val updates = hashMapOf<String, Any>(
                week_to_day to FieldValue.delete()
        )
        firedb.collection("user")
                .document(uid!!)
                .collection("timetable")
                .document(timetable_id.value!!)
                .update(updates)
                .addOnCompleteListener{
                    Log.d(tag, "delete_user_timetable: $week_to_day -> complete")

                    createTimetableFinish.value = true
                }
    }

}


//新規
class FiredbTimetableNew(): firedbColDoc(){
    private val tag = "firedb_timetable_new$tagHoge"

    fun getUserTimetableAllData(context: Context): ListenerRegistration {
        Log.d(tag, "get_user_timetable_all_data -> call")
        val tmp =userDocTt(timetable_id.value!!)
            .addSnapshotListener { value, error ->

                if (error != null){
                    Log.w(tag, "get_timetable_all_data -> error")
                    AlertDialog.Builder(context)
                        .setTitle("エラー")
                        .setMessage("通信エラーが発生しました\n再起動してください")
                        .setPositiveButton("OK"){_, _ ->

                        }.show()
                    return@addSnapshotListener
                }

                Log.d("hoge", "update")
                user_timetable_data_live.value = value?.data
                week_num = value?.getLong("wtd")!!.toInt()
                period_num = value.getLong("period")!!.toInt()
                Log.d(tag, "user_timetable_data_live ${user_timetable_data_live.value}")

            }
        return tmp
    }

    fun get_user_course_data(
        week: String,
        period: Int
    ){
        //Log.d(TAG, "get_user_course_data($week$period) -> call ")
        val weekAndPeriod = "$week$period"
        val userTimetable = user_timetable_data_live.value
        val courseData = userTimetable?.get(weekAndPeriod) as? Map<String, Any?>
        if (courseData != null){
            val courseId = courseData["course_id"] as String
            uniCourseDoc(weekAndPeriod, courseId)
                .get()
                .addOnSuccessListener {
                    val onlineData = it.data
                    Log.d(tag, "get_user_course_data($weekAndPeriod): $onlineData")
                    if (onlineData != null){
                        val courseName = onlineData["course"] as String
                        val lecturer = onlineData["lecturer"] as List<String>
                        val room = onlineData["room"] as String

                        val tmp_data = mapOf(
                            "course" to courseName,
                            "lecturer" to lecturer,
                            "room" to room
                        )
                        var tmp = course_data_live.value
                        if (tmp != null) {
                            tmp.put(weekAndPeriod, tmp_data)
                        }else{
                            tmp = mutableMapOf(weekAndPeriod to tmp_data)
                        }
                        course_data_live.value = tmp

                        Log.d(tag, "course_data_live: ${course_data_live.value}")
                    }

                }
                .addOnFailureListener {

                }
        }
    }

    fun checkUserTimetable(context: Context){
        Log.d(tag, "check_user_timetable -> call")
        userCollectionTt()
            .get()
            .addOnSuccessListener {
                val userTimetableNum = it.documents.size

                val dialog = AlertDialog.Builder(context)
                    .setTitle("時間割の追加")
                    .setMessage("時間割を作成してください")

                    .setPositiveButton("作成"){ _, _ ->
                        val i = Intent(context, CreateTimetableActivity::class.java)
                        context.startActivity(i)
                        false
                    }
                dialog.create()
                    .setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)

                if (userTimetableNum == 0){
                    dialog.show()

                }else{
                    dialog.setMessage("時間割選択画面がまだないので新たに作ってください")
                        .show()
                }
            }
            .addOnFailureListener {

            }
    }

    fun createTimetable(context: Context, tt_data: Map<String, Any?>){
        Log.d(tag, "create_timetable -> call")
        val docId = tt_data["timetable_id"] as String
        val year = tt_data["year"] as Int
        val term = tt_data["term"] as Int
        val week = tt_data["wtd"] as Int
        val period = tt_data["period"] as Int

        userDocTt(docId)
            .set(tt_data)
            .addOnSuccessListener {
                Log.d(tag, "create_timetable -> success")
                setTimetable(context, tt_data)

            }
            .addOnFailureListener {
                Log.w(tag, "create_timetable -> failure")
                Toast.makeText(context, "追加に失敗しました", Toast.LENGTH_SHORT).show()
            }
    }

    fun setTimetable(context: Context, tt_data: Map<String, Any?>){
        Log.d(tag, "set_timetable -> call")
        val docId = tt_data["timetable_id"] as String
        val year = tt_data["year"].toString().toInt()
        val term = tt_data["term"].toString().toInt()
        val week = tt_data["wtd"].toString().toInt()
        val period = tt_data["period"].toString().toInt()


        semester = "$year-$term"
        week_num = week
        period_num = period
        timetable_id.value = docId
        setTimetableId(context, timetable_id.value!!)
        Log.d(tag, "semester: $semester")
        Log.d(tag, "timetable_id: ${timetable_id.value}")

        user_timetable_data_live = MutableLiveData<Map<String, Any?>>()
        course_data_live = MutableLiveData<MutableMap<String, Any?>>()
        Log.d(tag, "user_timetable_data_live: ${user_timetable_data_live.value}")
        Log.d(tag, "course_data_live: ${course_data_live.value}")

        Log.d(tag, "user_timetable_data_live & course_data_live -> clear")

        createTimetableFinish.value = true
    }

    fun getCourseList(binding: ActivitySelectCourseBinding, context: Context, week_to_day: String){
        binding.viewmodel?.search()

        uniCourseCollerction(week_to_day)
            .get()
            .addOnSuccessListener { it ->

                var list = arrayListOf<Map<String, String>>()

                if (it.size() == 0)
                    binding.viewmodel?.zero()

                else{
                    for (doc in it){
                        val data = doc.data
                        val courseName = data["course"] as? String
                        var courseLecturer = ""
                        val tmpCourseLecturer = data["lecturer"] as? List<String>
                        val courseRoom = data["room"] as? String
                        val courseId = data["course_id"]


                        if (tmpCourseLecturer?.size != 1){
                            courseLecturer = tmpCourseLecturer!![0]+"..."
                        }else{
                            courseLecturer = tmpCourseLecturer!![0]
                        }
                        val map = mapOf(
                            "course_name" to courseName,
                            "course_lecturer" to courseLecturer,
                            "course_room" to courseRoom,
                            "course_id" to courseId
                        )

                        list.add(map as Map<String, String>)

                        if (it.size() == list.size){
                            binding.viewmodel?.non()
                            searchbar.observe(binding.lifecycleOwner!!, androidx.lifecycle.Observer {
                                val keyword = searchbar.value
                                var adapter: SelectCourseCustomAdapter? =null

                                if (keyword != null){
                                    val tmp = list.filter{
                                        it["course_name"]!!.contains(keyword) || it["course_lecturer"]!!.contains(keyword)
                                    }
                                    adapter = SelectCourseCustomAdapter(
                                        tmp as ArrayList<Map<String, String>>, context
                                    )
                                }else{
                                    adapter = SelectCourseCustomAdapter(
                                        list, context)
                                }

                                val layoutManager = LinearLayoutManager(context)

                                binding.recycleView.layoutManager = layoutManager
                                binding.recycleView.adapter = adapter
                                binding.recycleView.setHasFixedSize(true)
                            })
                        }
                    }
                }

            }
            .addOnFailureListener {

            }
    }

}


class firedb_task(val context: Context): firedbColDoc(){
    private val tag = "firedb_task$tagHoge"


    fun getCourseList(){
        Log.d(tag, "get_course_list -> call")

        userDocTt(timetable_id.value!!)
            .get()
            .addOnSuccessListener {
                Log.d(tag, "firedb_task.get_course_list: get user data -> success")
                val userData = it.data

                var classDataArray = arrayListOf<Any>()

                var courseNum = 0

                for (week in week_to_day_symbol_list){
                    for (period in period_list){
                        val weekAndPeriod = week + period
                        val classData = userData?.get(weekAndPeriod) as? Map<*, *>
                        if(user_timetable_data_live.value?.get(weekAndPeriod) != null){
                            courseNum += 1
                        }

                        if (classData != null){
                            val classId = classData["course_id"] as String
                            //Log.d("hogee", "class_id: $class_id")

                            uniCourseDoc(weekAndPeriod, classId)
                                .get()
                                .addOnSuccessListener {
                                    val courseName = it.getString("course")!!

                                    val tmp = hashMapOf(
                                        "course_id" to classId,
                                        "week_to_day" to weekAndPeriod,
                                        "course" to courseName
                                    )

                                    classDataArray.add(tmp)
                                    Log.d(tag, "class_data_array: $classDataArray")
                                    if (courseNum == classDataArray.size){
                                        if (classDataArray.isEmpty()){
                                            none_Timetable(context)
                                        }else{
                                            TaskDialog(context).courseSelecterDialog(classDataArray)
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

    fun createTask(task_data: Map<String, Any>){
        Log.d(tag, "create_task -> call")
        firedb.collection("user")
                .document(uid!!)
                .get()
                .addOnSuccessListener {
                    Log.d(tag, "firedb_task -> create_task -> get user data -> success")

                    val universityId = it.getString("university_id")

                    val classData = task_data["class"] as Map<String, Any>
                    val classId = classData["class_id"] as String
                    val weekToDay = classData["week_to_day"] as String


                    if(universityId != null && weekToDay != null && classId != null){
                        val taskDoc = firedb.collection("university")
                                .document(universityId)
                                .collection("semester")
                                .document(semester!!)
                                .collection(weekToDay)
                                .document(classId)
                                .collection("task")
                                .document()

                        val time_limit = "${task_data["day"]} ${task_data["time"]}"

                        val setData = hashMapOf(
                                "task_id" to taskDoc.id,
                                "time_limit" to task_data["time_limit"],
                                "task_name" to task_data["task_title"],
                                "note" to task_data["note"]
                        )

                        taskDoc.set(setData)
                                .addOnSuccessListener {
                                    Log.d(tag, "add task -> success")
                                    Toast.makeText(context, "課題が追加されました。", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Log.d(tag, "add task -> failure")
                                    Toast.makeText(context, "課題が追加できませんでした。", Toast.LENGTH_SHORT).show()
                                }
                    }else{
                        Log.w(tag, "firedb_task -> create_task -> university_id is null")
                    }
                }
                .addOnFailureListener {
                    Log.w(tag, "firedb_task -> create_task -> get user data -> failure")
                }
    }

    fun getNotCompTaskList(view: View){
        Log.d(tag, "get_not_comp_task_list -> call")

        var allCompTask: Array<String> = arrayOf()
        //履修中の授業取得
        var taskList: ArrayList<Map<String, Any>> = arrayListOf()
        for (week in week_to_day_symbol_list) {
            for (period in period_list) {

                val userClassData = user_timetable_data_live.value?.get(week + period) as? MutableMap<String, Any>

                if (userClassData != null){
                    val courseId = userClassData["course_id"] as String
                    val compTask = userClassData["comp_task"] as? ArrayList<String?>
                    allCompTask = addArrayToArray(allCompTask, compTask)
                    Log.d(tag, "user_class_data: $userClassData")


                    //授業の詳細の取得
                    Log.d(tag, "course_id: $courseId")

                    uniCourseDoc(week, period, courseId)
                        .get()
                        .addOnSuccessListener {
                            Log.d(tag, "get 授業のデータ -> success")
                            val classData = it.data
                            Log.d(tag, "class: $classData")
                            if (classData != null) {
                                uniTaskCol(week, period, courseId)
                                    .orderBy("time_limit")
                                    .addSnapshotListener { value, error ->
                                        if(error != null){
                                            Log.w(tag, "get_not_comp_task_list -> error", error)
                                            return@addSnapshotListener
                                        }


                                        //課題取得
                                        for (task_item in value!!.documentChanges) {
                                            when(task_item.type){
                                                DocumentChange.Type.ADDED ->{

                                                    val task_data = task_item.document.data
                                                    val task_id = task_data["task_id"] as? String

                                                    task_data["class_data"] = classData

                                                    if (!(allCompTask.contains(task_id))) {
                                                        taskList.add(task_data)
                                                    }
                                                    Log.d(tag, "data's: $task_data")
                                                    //Log.d(TAG, "datas: $task_list")

                                                    taskList.sortBy { it["time_limit"] as Timestamp }

                                                    //表示
                                                    //Log.d(TAG, "tasks show to recyclerview")
                                                    val adapter = TaskNotCompListCustomAdapter(taskList, context)
                                                    val layoutManager = LinearLayoutManager(context)

                                                    view.AssignmentActivity_assignment_recyclerView.layoutManager = layoutManager
                                                    view.AssignmentActivity_assignment_recyclerView.adapter = adapter
                                                    view.AssignmentActivity_assignment_recyclerView.setHasFixedSize(true)
                                                }
                                            }
                                        }
                                    }
                            } else {
                                Log.d(tag, "get_not_comp_task_list: class_data is null")
                            }
                        }
                }
            }
        }

    }

    fun getCompTaskList(view: View){
        Log.d(tag, "get_comp_task_list -> call")

        var allCompTask: Array<String> = arrayOf()
        //履修中の授業取得
        val taskList: ArrayList<Map<String, Any>> = arrayListOf()
        for (week in week_to_day_symbol_list) {
            for (period in period_list) {
                val userClassData = user_timetable_data_live.value?.get(week + period) as MutableMap<String, Any>?

                //授業のデータ
                if (userClassData != null){
                    val compTask = userClassData["comp_task"] as? ArrayList<String?>
                    val courseId = userClassData["course_id"] as String
                    allCompTask = addArrayToArray(allCompTask, compTask)

                    uniCourseDoc(week, period, courseId)
                        .get()
                        .addOnSuccessListener {
                            Log.d(tag, "get 授業のデータ -> success")
                            val class_data = it.data
                            //履修中の授業取得
                            if (class_data != null) {

                                uniTaskCol(week, period, courseId)
                                    .orderBy("time_limit")
                                    .addSnapshotListener { value, error ->
                                        if (error != null){
                                            Log.w(tag, "get_comp_task_list -> error", error)
                                            return@addSnapshotListener
                                        }


                                        for (task_item in value!!.documentChanges) {
                                            when(task_item.type){
                                                DocumentChange.Type.ADDED ->{
                                                    val task_data = task_item.document.data as MutableMap<String, Any>
                                                    val task_id = task_data["task_id"] as String
                                                    task_data["class_data"] = class_data

                                                    if (allCompTask.contains(task_id)) {
                                                        taskList.add(task_data)
                                                    }

                                                    taskList.sortByDescending { it["time_limit"] as Timestamp }

                                                    //表示
                                                    Log.d(tag, "tasks show to recyclerview")
                                                    val adapter = TaskCompListCustomAdapter(taskList, context, semester!!)
                                                    val layoutManager = LinearLayoutManager(context)

                                                    view.AssignmentActivity_assignment_recyclerView.layoutManager = layoutManager
                                                    view.AssignmentActivity_assignment_recyclerView.adapter = adapter
                                                    view.AssignmentActivity_assignment_recyclerView.setHasFixedSize(true)
                                                }

                                            }

                                        }
                                    }
                            } else {
                                Log.d(tag, "get_comp_task_list: class_data is null")
                            }
                        }
                        .addOnFailureListener {
                            Log.w(tag, "get 授業のデータ -> failure", it)
                        }

                }


            }
        }
    }

    fun getTomorrowNotCompTaskList(view: FragmentHomeBinding){
        Log.d(tag, "get_tomorrow_not_comp_task_list -> call")

        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, 1)
        val tomorrow = cal.time


//            Log.d("hoge", "to_int:${time_limit_int}")
        view.mainTaskInfoRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        var allCompTask: Array<String> = arrayOf()

        for (week in week_to_day_symbol_list) {
            for (period in period_list) {
                val userClassData = user_timetable_data_live.value?.get(week + period) as MutableMap<String, Any>?

                if (userClassData != null) {
                    val courseId = userClassData["course_id"] as String
                    val compTask = userClassData["comp_task"] as ArrayList<String?>
                    allCompTask = addArrayToArray(allCompTask, compTask)
                    //履修中の授業取得
                    uniCourseDoc(week, period, courseId)
                        .get()
                        .addOnSuccessListener {
                            Log.d(tag, "get 授業のデータ -> success")
                            val class_data = it.data

                            if (class_data != null){
                                uniTaskCol(week, period, courseId)
                                    .orderBy("time_limit")
                                    .addSnapshotListener { value, error ->
                                        if (error != null){
                                            Log.w(tag, "get_tomorrow_not_comp_task_list -> error", error)
                                            return@addSnapshotListener
                                        }
                                        //履修中の授業取得
                                        val taskList: ArrayList<Map<String, Any>> = arrayListOf()

                                        for (task_item in value!!.documentChanges) {

                                            when(task_item.type) {
                                                DocumentChange.Type.ADDED -> {
                                                    val taskData = task_item.document.data as MutableMap<String, Any>
                                                    val taskId = taskData["task_id"] as? String
                                                    taskData["class_data"] = class_data

                                                    Log.d(tag, "data: $taskData")

                                                    val time_limit = taskData["time_limit"] as Timestamp

                                                    cal.time = time_limit.toDate()

                                                    val taskTimelimit = cal.time



                                                    if (!(allCompTask.contains(taskId)) && taskTimelimit.before(tomorrow)) {
                                                        taskList.add(taskData)
                                                    }

                                                    taskList.sortBy { it.get("time_limit") as Timestamp }

                                                    //表示
                                                    Log.d(tag, "tasks show to recyclerview")
                                                    val adapter = HomeTaskListCustomAdapter(taskList, context, semester!!)
                                                    val layoutManager = LinearLayoutManager(context)


                                                    view.mainTaskInfoRecyclerview.layoutManager = layoutManager
                                                    view.mainTaskInfoRecyclerview.adapter = adapter
                                                    view.mainTaskInfoRecyclerview.setHasFixedSize(true)
                                                }
                                            }

                                        }
                                    }
                            }
                        }
                        .addOnFailureListener {
                            Log.w(tag, "get 授業のデータ -> failure")
                        }

                } else {
                    Log.d(tag, "get_tomorrow_not_comp_task_list: class_data is null")
                }
            }
        }
    }

    fun taskToComp(class_and_task_data: Map<String, Any>){
        Log.d(tag, "task_to_comp -> call")
        val classData= class_and_task_data["class_data"] as Map<String, Any>
        val taskId = class_and_task_data["task_id"] as String
        val weekToDay = classData["week_to_day"] as String


        userDocTt(getTimetableId(context)!!)
            .get()
            .addOnSuccessListener {
                Log.w(tag, "get course data -> success")
                val online_class_data = it.get(weekToDay) as MutableMap<String, Any?>
                var comp_task = online_class_data["comp_task"] as? ArrayList<String?>


                if (comp_task != null) {
                    comp_task.add(taskId)
                }else{
                    comp_task = arrayListOf(taskId)
                }

                online_class_data["comp_task"] = comp_task

                userDocTt(getTimetableId(context)!!)
                    .update(weekToDay, online_class_data)
                    .addOnSuccessListener {
                        Log.d(tag, "task_to_compe -> success")
                        Toast.makeText(context, "提出済みにしました",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.w(tag, "task_to_compe -> failure")
                        Toast.makeText(context, "提出済みにできませんでした",Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener {
                Log.w(tag, "get course data -> failure")
                Toast.makeText(context, "提出済みにできませんでした",Toast.LENGTH_SHORT).show()
            }


    }

    fun taskToNotcomp(class_and_task_data: Map<String, Any>){
        Log.d(tag, "task_to_notcomp -> call")
        val classData= class_and_task_data["class_data"] as Map<String, Any>
        val taskId = class_and_task_data["task_id"] as String
        val weekToDay = classData["week_to_day"] as String



        userDocTt(getTimetableId(context)!!)
            .get()
            .addOnSuccessListener {
                Log.w(tag, "get course data -> success")
                val onlineClassData = it.get(weekToDay) as MutableMap<String, Any?>
                val compTask = onlineClassData["comp_task"] as MutableList<String>

                compTask -= taskId
                onlineClassData["comp_task"] = compTask

                userDocTt(getTimetableId(context)!!)
                    .update(weekToDay, onlineClassData)
                    .addOnSuccessListener {
                        Log.d(tag, "task_to_notcomp -> success")
                        Toast.makeText(context, "未提出にしました",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.w(tag, "task_to_notcomp -> failure")
                        Toast.makeText(context, "未提出にできませんでした",Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Log.w(tag, "task_to_notcomp -> failure")
                Toast.makeText(context, "未提出にできませんでした",Toast.LENGTH_SHORT).show()
            }

    }
}

class FiredbTaskNew(): firedbColDoc(){
    private val tag = "firedb_task_new" + tagHoge

    fun getCourseTask(recycle_view: RecyclerView, week_to_day: String, course_id: String, context: Context){
        Log.d(tag, "get_course_task -> call")

        val courseDatas = user_timetable_data_live.value?.get(week_to_day) as? Map<String, Any?>
        val compTask = courseDatas?.get("comp_task") as? ArrayList<String>

        uniTaskCol(week_to_day, course_id)
            .orderBy("time_limit")
            .addSnapshotListener { value, error ->
                if (error != null){
                    Log.w(tag, "get_course_task -> erro", error)
                }
                val taskList: ArrayList<Map<String, Any>> = arrayListOf()
                if (value != null) {
                    for (doc in value.documents){
                        val taskData = doc.data

                        if (taskData != null) {
                            taskList.add(taskData)
                        }
                        val adapter = CourseDetailCustomAdapter(taskList, compTask, context)
                        val layoutManager = LinearLayoutManager(context)

                        recycle_view.layoutManager = layoutManager
                        recycle_view.adapter = adapter
                        recycle_view.setHasFixedSize(true)

                    }
                }
            }
    }

    fun createTask(context: Context, week_period: String, courseId: String, data: MutableMap<String, Any?>){
        Log.d(tag, "create_task -> call")
        val doc = uniTaskCol(week_period, courseId)
            .document()

        data["task_id"] = doc.id
        Log.d(tag, "ho: $data")

        doc.set(data)
            .addOnSuccessListener {
                Log.d(tag, "add task -> success")
                Toast.makeText(context, "課題が追加されました", Toast.LENGTH_SHORT).show()
                Thread.sleep(300)
                createTaskFinish.value = true
            }
            .addOnFailureListener {
                Log.w(tag, "add task -> failure")
                Toast.makeText(context, "課題が追加できませんでした", Toast.LENGTH_SHORT).show()
            }
    }
}
