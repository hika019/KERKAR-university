package jp.hika019.kerkar_university.task

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.firedb_task
import jp.hika019.kerkar_university.TaskDialogNew
import kotlinx.android.synthetic.main.item_task_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TaskNotCompListCustomAdapter(
        private val list: ArrayList<Map<String, Any>>,
        private val context: Context?)
    : RecyclerView.Adapter<TaskNotCompListCustomAdapter.CustomViewHolder>() {

    lateinit var listener: OnItemClickListener

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val day = view.assignment_activity_info_day_textview
        val lecture_title = view.assignment_activity_info_title_textview
        val assignment_details = view.assignment_activity_info_details_textview
    }

    // getItemCount onCreateViewHolder onBindViewHolderを実装
    // 上記のViewHolderクラスを使ってViewHolderを作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_task_fragment, parent, false)
        return CustomViewHolder(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //ここで挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val task_data = list[position]
        val class_data = task_data["class_data"] as Map<String, Any?>

        val time_limit_timestamp = task_data.get("time_limit") as Timestamp
        val couse = class_data["course"] as String

        //Log.d("hogee", "time: $timestamp")
        val timelimit_date = time_limit_timestamp.toDate()
        val cal = Calendar.getInstance()
        cal.time = timelimit_date
        val df = SimpleDateFormat("MM/dd HH:mm")


        holder.day.text = df.format(cal.time)+"まで"
        holder.lecture_title.text = "${couse}"
        holder.assignment_details.text = "${task_data["task_name"]}"
        //タップ
        holder.view.setOnClickListener {
            Log.d("AssignmentActivity", "select assignment item: $position")

            //表示する内容
            val class_data = list[position]

            task_nocomp_ditail_dialog(context!!, class_data, position)
            Log.d("assignment_list", list.toString())

        }
    }

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: String)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    // Itemを追加する
    fun addListItem (item: String) {
        notifyDataSetChanged() // これを忘れるとRecyclerViewにItemが反映されない
    }

    // Itemを削除する
    private fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged() // これを忘れるとRecyclerViewにItemが反映されない
    }


    fun task_nocomp_ditail_dialog(context: Context, task_data: Map<String, Any>, position: Int){

        val hoge = TaskDialogNew()
        hoge.taskDetailDialog(context, task_data)
                .setPositiveButton("OK") { dialog, which ->

                }
                .setNeutralButton("提出済みにする") {dialog, which ->
                    Log.d("Assignment", "$position　を提出済みにする")
//                    addListItem(list[position])
                    val class_data = list[position] as Map<String, Any>
//                    firedb_load_task_class(context).task_to_comp(class_data)
                    firedb_task(context).taskToComp(class_data)
                    removeItem(position)
                }
                .show()
    }
}