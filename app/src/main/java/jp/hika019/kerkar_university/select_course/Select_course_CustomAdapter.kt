package jp.hika019.kerkar_university.select_course

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.hika019.kerkar_university.R
import kotlinx.android.synthetic.main.item_select_course.view.*

class Select_course_CustomAdapter(
    private val list: ArrayList<Map<String, String>>,
    private val context: Context?
) : RecyclerView.Adapter<Select_course_CustomAdapter.CustomViewHolder>() {



    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val course_name = view.item_select_course_name
        val course_lecture = view.item_select_course_lecturer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_select_course, parent, false)
        return  CustomViewHolder(item)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val course_data = list[position]

        holder.course_name.text = course_data["course_name"]
        holder.course_lecture.text = course_data["course_lecturer"]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}