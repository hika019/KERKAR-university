package jp.hika019.kerkar_university.home

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
import kotlinx.android.synthetic.main.item_home_assignment_info.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeTaskListCustomAdapter(private val task_List: ArrayList<Map<String, Any>>,
                                private val context: Context,
                                private val semester: String
                                   )
    : RecyclerView.Adapter<HomeTaskListCustomAdapter.CustomViewHolder>() {

    private val TAG = "Home_fragment_CustomAdapter"

    lateinit var listener: OnItemClickListener

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val day = view.item_homeactivity_assignment_day_textview
        val lecture_title = view.item_homeactivity_assignment_title_textview
        val assignment_details = view.item_homeactivity_assignment_details_textview
    }

    // getItemCount onCreateViewHolder onBindViewHolderを実装
    // 上記のViewHolderクラスを使ってViewHolderを作成
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_home_assignment_info, parent, false)



        return CustomViewHolder(item)
    }

    override fun getItemCount(): Int {
        return task_List.size
    }

    //ここで挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val task_data = task_List[position]
        val class_data = task_data["class_data"] as Map<String, Any>

        Log.d(TAG, "class_data: ${class_data}")
        Log.d(TAG, "task_data: ${task_data}")

        val time_limit_timestamp = task_data["time_limit"] as Timestamp
        val couse = class_data["course"] as String

        val timelimit_date = time_limit_timestamp.toDate()
        val cal = Calendar.getInstance()
        cal.time = timelimit_date
        val df = SimpleDateFormat("MM/dd HH:mm")


        holder.day.text = df.format(cal.time)+"まで"
        holder.lecture_title.text = "${couse}"
        holder.assignment_details.text = "${task_data["task_name"]}"
        //タップ
        holder.view.setOnClickListener {
            Log.d(TAG, "select assignment item: $position")

            val class_data = task_List[position] as Map<String, Any>


            //表示する内容
            val id = "unique_id"
//            val str = "期限: 12/25\n科目: 情報倫理\n詳細: 小課題\n$position"
            home_task_ditail_dialog(context, class_data, position)

            //消す
            //removeItem(position)
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


    // Itemを削除する
    private fun removeItem(position: Int) {
        task_List.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged() // これを忘れるとRecyclerViewにItemが反映されない
    }


    private fun home_task_ditail_dialog(context: Context, task_data:Map<String, Any>, position: Int){

        val hoge = TaskDialogNew()
        hoge.taskDetailDialog(context, task_data)
            .setPositiveButton("OK") { dialog, which ->

            }
            .setNeutralButton("提出済みにする") {dialog, which ->
//                    firedb_load_task_class(context).task_to_comp(class_data)
                Log.d(TAG, "$position　を提出済みにする")

                val class_data = task_List[position] as Map<String, Any>
                Log.d(TAG, "data:${class_data}")
                firedb_task(context).taskToComp(class_data)
                removeItem(position)
            }
            .show()
    }

}