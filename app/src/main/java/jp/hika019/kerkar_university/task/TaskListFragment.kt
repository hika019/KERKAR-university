package jp.hika019.kerkar_university.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.firedb_task
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_task.view.*


class TaskListFragment(): Fragment() {

    val TAG = "Task_list_Fragment"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        view.AssignmentActivity_assignment_recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        get_notcomp_task(view)

        view.unsubmitted_or_submitted_tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
//                Toast.makeText(context, tab?.text, Toast.LENGTH_SHORT).show()
                if (tab?.text == "提出済課題") {
//                    list(view,  submmitted_list, unsubmmitted_list, frame_context)
                    get_comp_task(view)
                } else {
                    //Log.d("hoge", "call")
//                    list(view, unsubmmitted_list, submmitted_list, frame_context)
                    get_notcomp_task(view)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })



        view.add_task_fab.setOnClickListener{
            Log.d(TAG, "add_task_fab -> push")
            context?.let { it -> firedb_task(it).getCourseList() }

        }
        return view
    }


    fun get_comp_task(view: View){
        context?.let { firedb_task(it) }?.getCompTaskList(view)
    }

    fun get_notcomp_task(view: View){
        context?.let { firedb_task(it) }?.getNotCompTaskList(view)
    }
}