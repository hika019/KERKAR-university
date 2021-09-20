package jp.hika019.kerkar_university.Timetable

import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.databinding.ActivityCreateTimetableBinding
import jp.hika019.kerkar_university.viewmodels.Create_timetable_VM
import kotlinx.android.synthetic.main.activity_create_timetable.*
import java.text.SimpleDateFormat
import java.util.*

class Create_timetableActivity : AppCompatActivity() {
    private val TAG = "Create_timetable" +TAG_hoge

    private val viewmodel by viewModels<Create_timetable_VM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityCreateTimetableBinding>(
            this, R.layout.activity_create_timetable
        )
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this
        Log.d(TAG, "Create_timetableActivity -> show")


        //setContentView(R.layout.activity_create_timetable)

        setToolbar()
        binding.periodPicer.minValue = 4
        binding.periodPicer.maxValue = 8


        viewmodel.radioGroup.observe(this, Observer {
            val term = term_radioGroup.indexOfChild(term_radioGroup.findViewById<RadioButton>(viewmodel.radioGroup.value!!))
            viewmodel.term.value = term
        })

        viewmodel.create_button_enable.observe(this, Observer {
            if (viewmodel.create_button_enable.value == true){
                binding.createTimetableButton.setBackgroundResource(R.drawable.setup_button_bg)
            }
        })

        createtimetable_finish.observe(this, Observer {
            if (createtimetable_finish.value == true){
                createtimetable_finish.value = false
                finish()
            }
        })



        //学期の選択
        firedb.collection("university")
            .document(university_id!!)
            .get()
            .addOnSuccessListener {
                val term = it.getLong("term")?.toInt()

                for(i in 1..term!!){
                    val radio = RadioButton(this)
                    radio.text = "${i}学期"
                    radio.minHeight = 48
                    term_radioGroup.addView(radio)
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "学期の取得に失敗しました\n" +
                        "通信環境を見直してください", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "get term -> failure: ", it)
            }





    }

    fun setToolbar(){
        val toolbar = toolbar4
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (semester != null) {
            finish()
        }else{
            Toast.makeText(this, "時間割を作成してください", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}