package jp.hika019.kerkar_university.Course_detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.TAG_hoge
import kotlinx.android.synthetic.main.item_course_detail_activity.view.*

class Course_detail_CustomAdapter(
    private val list: List<Int>,
    private val context: Context?
    ):RecyclerView.Adapter<Course_detail_CustomAdapter.CustomViewHolder>() {


    private val TAG = "Course_detail_CustomAdapter" + TAG_hoge

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val day = view.item_course_detail_task_limit_day_textview
        val time = view.item_course_detail_task_limit_time_textview
        val course_name = view.item_course_detail_course_name_textview
        val check = view.item_course_detail_task_done_igview
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_course_detail_activity, parent, false)
        return CustomViewHolder(item)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //val data = list[position] as Map<String, String>
        //holder.course_name.text = data["title"]
    }

    override fun getItemCount(): Int {
        return list.size
    }

}