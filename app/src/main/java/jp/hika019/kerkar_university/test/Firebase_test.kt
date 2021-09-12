package jp.hika019.kerkar_university


import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import jp.hika019.kerkar_university.*

open class firebase_test(){

    private val TAG = "firebase_test"
    val firedb = FirebaseFirestore.getInstance()

    private val user_course_doc =
        firedb.collection("user")
            .document(uid!!)
            .collection("semester")
            .document(semester!!)

    private  val uni_course_doc =
        firedb.collection("university")
            .document(university_id!!)
            .collection("semester")
            .document(semester!!)

    fun get_all_course_id(context: Context){
        Log.d(TAG, "get_all_course_id -> call")

        val tt_id = get_timetable_id(context)

        firedb.collection("user")
            .document(uid!!)
            .collection("timetable")
            .document(tt_id!!)
            .addSnapshotListener{ value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                val hoge = value?.data
                //Log.d("hogee", "hoge: $hoge")

                if (hoge != null){

                    val wtd = hoge["wtd"] as Long
                    week_num = wtd.toInt()

                    val period = hoge["wtd"] as Long
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

        uni_course_doc
            .collection(week_period)
            .document(course_id)
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