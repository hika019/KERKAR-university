package jp.hika019.kerkar_university.Task

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.firedb_task
import kotlinx.android.synthetic.main.item_assignment_activity.view.*


class task_cmp_list_CustomAdapter(
        private val list: ArrayList<Map<String, Any>>,
        private val context: Context?, private val semester: String)
    : RecyclerView.Adapter<task_cmp_list_CustomAdapter.CustomViewHolder>() {

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
        val item = layoutInflater.inflate(R.layout.item_assignment_activity, parent, false)
        return CustomViewHolder(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //ここで挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val task_data = list[position]
        val class_data = task_data["class_data"] as Map<String, Any>

        val day = task_data["time_limit"] as String
        val couse = class_data["course"] as String


        holder.day.text = day.substring(5,10)
        holder.lecture_title.text = "${couse}"
        holder.assignment_details.text = "${task_data["task_name"]}"
        //タップ
        holder.view.setOnClickListener {
            Log.d("AssignmentActivity", "select assignment item: $position")

            //表示する内容
            val class_data = list[position] as Map<String, Any>

            assigmenment_comp_ditail_dialog(context!!, class_data, position)
//            Log.d("assignment_list", list.toString())

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

    fun assigmenment_comp_ditail_dialog(context: Context, task_data: Map<String, Any>, position: Int){

        val class_data = task_data["class_data"] as Map<String, Any>

        val str = "　期限: ${task_data["time_limit"]}\n" +
                "　教科: ${class_data["course"]}\n" +
                "　詳細: ${task_data["task_name"]}\n" +
                "その他: ${task_data["note"]}"

        AlertDialog.Builder(context)
                .setTitle("課題")
                .setMessage(str)
                .setPositiveButton("OK") { dialog, which ->

                }
                .setNeutralButton("未提出にする") {dialog, which ->
//                    addListItem(list[position])
                    val class_data = list[position]
                    firedb_task(context).task_to_notcomp(class_data)
                    removeItem(position)
                }
                .show()
    }

}