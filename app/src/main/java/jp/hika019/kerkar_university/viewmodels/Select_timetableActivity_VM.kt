package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import jp.hika019.kerkar_university.TAG_hoge
import jp.hika019.kerkar_university.Timetable.Create_timetableActivity

class Select_timetableActivity_VM: ViewModel() {

    private val TAG = "Select_timetableActivity_VM"+ TAG_hoge

    fun create_tt(context: Context){
        Log.d("hoge", "create_tt -> call")
        val i = Intent(context, Create_timetableActivity::class.java)
        context.startActivity(i)
    }
}