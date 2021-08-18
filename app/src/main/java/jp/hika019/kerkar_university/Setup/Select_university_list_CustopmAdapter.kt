package jp.hika019.kerkar_university.Setup

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.setup_dialog
import kotlinx.android.synthetic.main.item_serch_university.view.*


class Select_university_list_CustopmAdapter(
        private val university_name_list: Array<String>,
        private val university_id_list: Array<String>,
        private val context: Context
        )
    : RecyclerView.Adapter<Select_university_list_CustopmAdapter.CustomViewHolder>(){


    private val TAG = "Select_university_list_CustopmAdapter"
    lateinit var listener: OnItemClickListener

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val uni_text = view.university_name
        val plus_ic = view.item_serch_university_plus_ic
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_serch_university, parent, false)

        return CustomViewHolder(item)
    }


    override fun getItemCount(): Int {
        return university_id_list.size
    }

    //挿入
    override fun onBindViewHolder(
        holder: CustomViewHolder,
        position: Int
    ) {

        val university_name = university_name_list[position]
        val university_id = university_id_list[position]


        if(university_name_list.size-1 != position){
            holder.plus_ic.visibility = View.INVISIBLE
        }else{
            holder.plus_ic.visibility = View.VISIBLE
        }
        holder.uni_text.text = university_name

        holder.view.setOnClickListener {
            val instance = setup()
            if(university_name.equals("大学を追加")){
                //大学を作る
                instance.create_university(context)
            }else{
                instance.create_user_data(context, university_name, university_id)
            }
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