package com.example.kerkar_university.Message

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.kerkar_university.R
import kotlinx.android.synthetic.main.item_message.view.*

class Message_CustomAdapter(private val list: ArrayList<HashMap<String, String>>,
                            private val context: Context?
                            )
    : RecyclerView.Adapter<Message_CustomAdapter.CustomViewHolder>(){
    private val TAG = "Message_CustomAdapter"

    lateinit var listener: AdapterView.OnItemClickListener

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val day = view.item_message_day_textview
        val message = view.item_message_message_textview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_message, parent, false)

        return CustomViewHolder(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val data = list[position] as Map<String, String>

        holder.day.text = data["time"]?.substring(0,10)
        holder.message.text = data["title"]

        holder.view.setOnClickListener{
            Log.d(TAG, "Message_CustomAdapter: click message!")
            val data = list[position]

            message_dialog(context!!, data)
        }
    }

    fun message_dialog(context: Context, data: Map<String, String>){

        val str = "${data["time"]}\n" +
                "${data["title"]}\n" +
                "${data["message"]}"

        val dialog = AlertDialog.Builder(context)
                .setTitle("運営からのお知らせ")
                .setMessage(str)
                .setPositiveButton("OK"){dialog, which ->
                    false
                }
        dialog.create().show()
    }
}