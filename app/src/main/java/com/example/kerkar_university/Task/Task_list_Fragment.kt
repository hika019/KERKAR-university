package com.example.kerkar_university.Task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kerkar_university.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_task_list.view.*


class Task_list_Fragment(): Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_task_list, container, false)

        view.unsubmitted_or_submitted_tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
//                Toast.makeText(context, tab?.text, Toast.LENGTH_SHORT).show()
                if (tab?.text == "提出済課題") {
//                    list(view,  submmitted_list, unsubmmitted_list, frame_context)
                    get_comp_task(view)
                } else {
//                    list(view, unsubmmitted_list, submmitted_list, frame_context)
                    get_notcomp_task(view)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })




        return view
    }


    fun get_comp_task(view: View){

    }

    fun get_notcomp_task(view: View){

    }
}