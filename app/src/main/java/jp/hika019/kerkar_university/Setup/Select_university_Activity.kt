package jp.hika019.kerkar_university.Setup


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.firedb
import kotlinx.android.synthetic.main.activity_set_university.*

class Select_university_Activity(): AppCompatActivity() {
    val TAG = "Setup_select_university_fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_university)

        get_university_list()

        new_university.setOnClickListener {
            val instance = setup()
            instance.create_university(this)
        }

    }

    fun get_university_list(){
        Log.d(TAG, "get_university_list -> call")

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

                            Log.d(TAG, university_name_list.toString())

                            Log.d(TAG, "get Universities list")



                            val adapter = Select_university_list_CustopmAdapter(university_name_list, university_id_list, this)
                            val layouManager = LinearLayoutManager(this)

                            select_university_recycle_view.layoutManager = layouManager
                            if (!(university_name_list.isNullOrEmpty())){
                                select_university_recycle_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

                            }
                            select_university_recycle_view.adapter = adapter
                            select_university_recycle_view.setHasFixedSize(true)

                        }
                    }

                }

            }
    }
}