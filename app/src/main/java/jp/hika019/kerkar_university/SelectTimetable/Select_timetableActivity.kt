package jp.hika019.kerkar_university.SelectTimetable

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.viewmodels.Select_timetableActivity_VM
import kotlinx.android.synthetic.main.activity_select_timetable.*

class Select_timetableActivity:AppCompatActivity() {

    private val TAG = "Select_timetableActivity"+ TAG_hoge
    private val viewmodel by viewModels<Select_timetableActivity_VM>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_timetable)
        Log.d(TAG, "Select_timetableActivity -> call")

        toolbar()

        timetablse_select_recycleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        add_imageView5.setOnClickListener {
            viewmodel.create_tt(this)
        }

        createtimetable_finish.observe(this, Observer {
            if (createtimetable_finish.value == true){
                createtimetable_finish.value = false
                if (login_flag){
                    Log.d(TAG, "login flag -> true")
                    val i = Intent(this, MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(i)
                }else{
                    Log.d(TAG, "login flag -> false")
                    Toast.makeText(this, "画面を更新してください", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })

        firedb.collection("user")
            .document(uid!!)
            .collection("timetable")
            .get()
            .addOnSuccessListener {
                Log.d(TAG, "get_tt_list -> success")

                val list = ArrayList<Map<String, Any>>()
                for (doc in it){
                    list.add(doc.data as Map<String, Any>)

                    val adapter = Select_timetable_CustomAdapter(list, this)
                    val layoutManager = LinearLayoutManager(this)
                    timetablse_select_recycleView.adapter = adapter
                    timetablse_select_recycleView.layoutManager = layoutManager
                    timetablse_select_recycleView.setHasFixedSize(true)

                }

            }
            .addOnFailureListener {
                Log.w(TAG, "get_tt_list -> failure", it)
            }



    }

    fun toolbar(){
        val toolbar = toolbar8
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!login_flag){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

