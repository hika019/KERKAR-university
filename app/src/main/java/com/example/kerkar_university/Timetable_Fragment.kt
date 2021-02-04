package com.example.kerkar_university

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_timetable.view.*

class Timetable_Fragment(): Fragment() {
    val TAG = "Timetable_Fragment"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_timetable, container, false)

        val firedb_semester = firedb_semester(view.context, view)

        firedb_semester.get_semester_title()


        view.semester_select_button.setOnClickListener{
            Log.d(TAG, "semester_select_button -> push")
            val firedb_semester = firedb_semester(view.context, view)
            firedb_semester.get_semester_list()
        }

        return view
    }
}