package jp.hika019.kerkar_university.SelectTimetable

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.tagHoge
import jp.hika019.kerkar_university.FiredbTimetableNew
import kotlinx.android.synthetic.main.item_select_timetable.view.*

class Select_timetable_CustomAdapter(
    private val list: ArrayList<Map<String, Any>>,
    private val context: Context?
) : RecyclerView.Adapter<Select_timetable_CustomAdapter.CustomViewHolder>() {

    private val TAG = "Select_timetable_CustomAdapter" + tagHoge

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tt_name = view.item_select_tt_title
        val tt_semester = view.item_select_tt_semester
        val tt_delete = view.item_select_tt_delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_select_timetable, parent, false)
        return  CustomViewHolder(item)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val tt_data = list[position]
        val year = tt_data["year"] as Long
        val term = tt_data["term"] as Long
        val tt_id = tt_data["timetable_id"] as String

        holder.tt_name.text = tt_data["timetable_name"].toString()
        holder.tt_semester.text = "${year}年度 ${term}学期"

        holder.view.tt_semester_touch.setOnClickListener {
            Log.d(TAG, "click event: $position")
            val hoge_class = FiredbTimetableNew()
            hoge_class.setTimetable(context!!, tt_data)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}