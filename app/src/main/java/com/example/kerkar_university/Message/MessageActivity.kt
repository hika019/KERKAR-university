package com.example.kerkar_university.Message

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kerkar_university.R
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_message.view.*

class MessageFragment(): Fragment() {
    private val TAG = "MessageFragment"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_message, container, false)
        var list: ArrayList<HashMap<String, String>> = arrayListOf()
        var firedb = FirebaseFirestore.getInstance()


        firedb.collection("message")
                .addSnapshotListener { snapshots, error ->
                    if(error != null){
                        Log.w(TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }
                    Log.d(TAG, "message -> get")

                    for(item in snapshots!!.documentChanges){
                        if(item.type == DocumentChange.Type.ADDED){
                            val day = item.document.getString("day")
                            val message = item.document.getString("message")

                            val data = hashMapOf(
                                    "day" to day!!,
                                    "message" to message!!
                            )
                            list.add(data)
                        }
                    }
                    Log.d(TAG, "message -> show")
                    val adapter = Message_CustomAdapter(list)
                    val layoutManager = LinearLayoutManager(context)

                    view.message_recycleview.layoutManager = layoutManager
                    view.message_recycleview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                    view.message_recycleview.adapter = adapter
                    view.message_recycleview.setHasFixedSize(true)

                }


//        val list = arrayListOf(data1,data2,data3,data4,data5)









        return view
    }


}