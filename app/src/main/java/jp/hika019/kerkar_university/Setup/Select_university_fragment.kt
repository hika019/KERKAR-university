package jp.hika019.kerkar_university.Setup


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.firedb
import kotlinx.android.synthetic.main.fragment_set_university.view.*

class Select_university_fragment(): Fragment() {
    val TAG = "Setup_select_university_fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set_university, container, false)

        get_university_list(view)


        return view
    }

    fun get_university_list(view: View){
        var university_name_list: Array<String> = arrayOf()
        var university_id_list: Array<String> = arrayOf()

        firedb.collection("university")
            .addSnapshotListener { documents, error ->

                if(error != null){
                    Log.w(TAG, "get_university_list -> failure", error)
                    return@addSnapshotListener
                }

                for(univer_doc in documents!!.documentChanges){

                    when(univer_doc.type){
                        DocumentChange.Type.ADDED -> {

                            val university_id = univer_doc.document.id
                            val university_name = univer_doc.document.getString("university")!!
                            university_id_list += university_id
                            university_name_list += university_name

                        }
                    }

                }
                university_name_list+="大学を追加"
                university_id_list+="大学を追加"

                Log.d(TAG, university_name_list.toString())

                Log.d(TAG, "get Universities list")

                val adapter = Select_university_list_CustopmAdapter(university_name_list, university_id_list, view.context)
                val layouManager = LinearLayoutManager(view.context)
                view.select_university_recycle_view.layoutManager = layouManager
                if (!(university_name_list.isNullOrEmpty())){
                    view.select_university_recycle_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

                }
                view.select_university_recycle_view.adapter = adapter
                view.select_university_recycle_view.setHasFixedSize(true)
            }
    }
}