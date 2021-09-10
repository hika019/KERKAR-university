package jp.hika019.kerkar_university


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import jp.hika019.kerkar_university.*

object firebase_test{
    val firedb = FirebaseFirestore.getInstance()

    fun get_course_id(){

        Log.d("hogee", "call")

        firedb.collection("user")
            .document(uid!!)
            .collection("semester")
            .document(semester!!)
            .addSnapshotListener{ value, error ->
                if (error != null){
                    return@addSnapshotListener
                }

                val hoge = value?.data

                Log.d("hogee", "hoge: $hoge")

                if (hoge != null){
                    for (week in week_to_day_symbol_list){
                        for(period in period_list){
                            val week_period = week+period.toString()
                            val tmp = hoge.get(week_period) as? Map<String, String>
                            //Log.d("hogee", "aaa$tmp")

                            var data = mutableMapOf<String, String?>(week_period to null)
                            if (tmp != null){
                                data = mutableMapOf(week_period to tmp["course_id"])
                                test_course_id.value = data
                                Log.d("hogee", "data: ${test_course_id.value}")
                            }


                        }
                    }
                }

            }



    }


}