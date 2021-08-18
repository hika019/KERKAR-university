package jp.hika019.kerkar_university.Setup

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.hika019.kerkar_university.R
import kotlinx.android.synthetic.main.item_serch_university.view.*


class Select_university_list_CustopmAdapter(
        private val university_name_list: ArrayList<String>,
        private val university_id_list: ArrayList<String>,
        private val context: Context
        )
    : RecyclerView.Adapter<Select_university_list_CustopmAdapter.CustomViewHolder>(){


    private val TAG = "Select_university_list_CustopmAdapter"

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
        return university_id_list!!.size
    }

    //挿入
    override fun onBindViewHolder(
        holder: CustomViewHolder,
        position: Int
    ) {

        val university_name = university_name_list[position]
        val university_id = university_id_list[position]


        if(university_id_list.size-1 != position){
            holder.plus_ic.visibility = View.INVISIBLE
        }

        holder.uni_text.text = university_name



    }

}