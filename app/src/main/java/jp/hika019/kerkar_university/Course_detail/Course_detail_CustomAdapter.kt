package jp.hika019.kerkar_university.Course_detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.TAG_hoge
import kotlinx.android.synthetic.main.item_course_detail_activity.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Course_detail_CustomAdapter(
    private val list: ArrayList<Map<String, Any>>,
    private val comp_list: ArrayList<String>,
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
        val task_data = list[position] as Map<String, String>

        val task_mame = task_data["task_name"]
        val time_limit_timestamp = task_data.get("time_limit") as Timestamp
        val timelimit_date = time_limit_timestamp.toDate()
        val cal = Calendar.getInstance()
        cal.time = timelimit_date
        val df_day = SimpleDateFormat("yyyy/MM/dd")
        val df_time = SimpleDateFormat("HH:mm")

        holder.day.text = df_day.format(cal.time)
        holder.time.text = df_time.format(cal.time)
        holder.course_name.text = task_mame
        if (comp_list.contains(task_data["task_id"])){
            val drawable = ContextCompat.getDrawable(context!!, R.drawable.outline_done_24)
            drawable!!.setTint(context.getColor(R.color.logo_color))
            holder.check.setImageDrawable(drawable)
        }





    }

    override fun getItemCount(): Int {
        return list.size
    }

}