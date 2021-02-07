package com.example.kerkar_university.Task

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.kerkar_university.R
import kotlinx.android.synthetic.main.item_assignment_activity.view.*


class task_notcmp_list_CustomAdapter(private val list: ArrayList<Any>, private val context: Context?)
    : RecyclerView.Adapter<task_notcmp_list_CustomAdapter.CustomViewHolder>() {

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

        val classdata = list[position] as Map<String, Any>
        val task_data = classdata["task"] as Map<String, String>

        val day = task_data["timelimit"] as String
        val couse = classdata["course"] as String


        holder.day.text = day.substring(5,10)
        holder.lecture_title.text = "${couse}"
        holder.assignment_details.text = "${task_data["task_name"]}"
        //タップ
        holder.view.setOnClickListener {
            Log.d("AssignmentActivity", "select assignment item: $position")

            //表示する内容
            val class_data = list[position] as Map<String, Any>
            val task = class_data["task"] as Map<String, String>

            val str = "　期限: ${task["timelimit"]}\n" +
                    "　教科: ${class_data["course"]}\n" +
                    "　詳細: ${task["task_name"]}\n" +
                    "その他: ${task["note"]}"
            task_nocomp_ditail_dialog(context!!, str, position)
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


    fun task_nocomp_ditail_dialog(context: Context, str:String, position: Int){

        AlertDialog.Builder(context)
                .setTitle("課題")
                .setMessage(str)
                .setPositiveButton("OK") { dialog, which ->

                }
                .setNeutralButton("提出済みにする") {dialog, which ->
                    Log.d("Assignment", "$position　を提出済みにする")
//                    addListItem(list[position])
                    val class_data = list[position] as Map<String, Any>
//                    firedb_load_task_class(context).task_to_comp(class_data)
                    removeItem(position)
                }
                .show()
    }
}