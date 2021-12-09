package jp.hika019.kerkar_university.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import jp.hika019.kerkar_university.tagHoge
import jp.hika019.kerkar_university.timetable.CreateTimetableActivity

class Select_timetableActivity_VM: ViewModel() {

    private val TAG = "Select_timetableActivity_VM"+ tagHoge

    fun create_tt(context: Context){
        Log.d("hoge", "create_tt -> call")
        val i = Intent(context, CreateTimetableActivity::class.java)
        context.startActivity(i)
    }
}