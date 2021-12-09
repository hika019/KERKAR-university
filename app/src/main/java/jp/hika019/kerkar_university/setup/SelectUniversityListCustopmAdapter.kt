package jp.hika019.kerkar_university.setup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.check_position
import kotlinx.android.synthetic.main.item_select_university.view.*


class SelectUniversityListCustopmAdapter(
        private val university_name_list: Array<String>,
        )
    : RecyclerView.Adapter<SelectUniversityListCustopmAdapter.CustomViewHolder>(){

    private val TAG = "Select_university_list_CustopmAdapter"
    lateinit var listener: OnItemClickListener

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        var uni_text = view.university_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_select_university, parent, false)

        return CustomViewHolder(item)
    }


    override fun getItemCount(): Int {
        return university_name_list.size
    }

    //挿入
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val university_name = university_name_list[position]
        //val university_id = university_id_list[position]

        holder.uni_text.isChecked = position == check_position

        holder.uni_text.text = university_name
        holder.uni_text.setOnClickListener{

            check_position = position
            notifyDataSetChanged()
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

}