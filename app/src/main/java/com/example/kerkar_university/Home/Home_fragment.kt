package com.example.kerkar_university.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kerkar_university.R
import com.example.kerkar_university.firedb_task
import kotlinx.android.synthetic.main.activity_home.view.*

class Home_fragment(): Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)

        view.floatingActionButton.setOnClickListener {
            context?.let { it1 -> firedb_task(it1).get_course_list() }
        }

        return view
    }
}