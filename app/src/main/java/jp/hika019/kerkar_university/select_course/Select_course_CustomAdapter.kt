package jp.hika019.kerkar_university.select_course

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.*
import androidx.recyclerview.widget.RecyclerView
import jp.hika019.kerkar_university.*
import kotlinx.android.synthetic.main.item_select_course.view.*

class Select_course_CustomAdapter(
    private val list: ArrayList<Map<String, String>>,
    private val context: Context?
) : RecyclerView.Adapter<Select_course_CustomAdapter.CustomViewHolder>() {

    private val TAG = "Select_course_CustomAdapter" + tagHoge

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val course_name = view.item_select_course_name
        val course_lecture = view.item_select_course_lecturer
        val course_room = view.item_select_course_room
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
        holder.course_room.text = course_data["course_room"]

        holder.view.setOnClickListener {
            Log.d(TAG, "click event: $position")

            dialog(position)
                .setPositiveButton("追加"){_, _ ->
                    val course_id = course_data["course_id"]
                    val hoge = firedb_timetable(context!!)


                    hoge.addUserTimetable(tmp_str, course_id!!)
                    Thread.sleep(200)

                }
                .setNegativeButton("キャンセル"){_, _ ->
                    false
                }
                .create()
                .show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun dialog(position: Int): AlertDialog.Builder {

        val course_data = list[position]
        val course = course_data["course_name"]
        val lecturer = course_data["course_lecturer"]
        val room = course_data["course_room"]


        val message = "$course\n$lecturer\n$room"

        val dialog = AlertDialog.Builder(context)
            .setTitle("確認画面")
            .setMessage(message)

        return dialog
    }
}