package jp.hika019.kerkar_university.Timetable

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import jp.hika019.kerkar_university.*

import kotlinx.android.synthetic.main.activity_create_timetable.*
import java.text.SimpleDateFormat
import java.util.*

class Create_timetableActivity : AppCompatActivity() {
    private val TAG = "Create_timetable"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_timetable)

        setToolbar()
        period_picer.minValue = 1
        period_picer.maxValue = 6

        var select_year: Int? = null


        val now_year = SimpleDateFormat("yyyy").format(Date()).toInt()

        timetablse_title_textview.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }


        year_textview.setOnClickListener {

            val year_list = Array(4){(now_year-2+it).toString()}
            var select_index: Int? = null

            val dialog = AlertDialog.Builder(this)
                .setTitle("年の選択")
                .setSingleChoiceItems(year_list, -1){ dialog, which ->
                    select_index = which
                }
                .setPositiveButton("確定"){ dialog, which ->
                    select_year = year_list[select_index!!].toInt()
                    year_textview.text = select_year.toString()
                }
            dialog.show()
        }


        for(i in 1..2){
            val radio = RadioButton(this)
            radio.text = "${i}学期"
            radio.minHeight = 48
            term_radioGroup.addView(radio)
        }


        create_timetable_button.setOnClickListener {
            Log.d(TAG, "create_timetable_button -> click")

            val timetable_title = timetablse_title_textview.text.toString()
            val tmp = term_radioGroup.checkedRadioButtonId
            val term = term_radioGroup.indexOfChild(term_radioGroup.findViewById<RadioButton>(tmp))
            var week = 5
            if (saturday_cheack.isChecked){
                week = 6
            }

            val period = period_picer.value

            if (timetable_title != "" && select_year != null && term != -1){

                semester = "$select_year-$term"
                week_num = week
                period_num = period
                Log.d(TAG, "semester: $semester")

                val user_tt_doc = firedb.collection("user")
                    .document(uid!!)
                    .collection("timetable")

                val doc_id = user_tt_doc.document().id
                val tt_data = mapOf(
                    "wtd" to week,
                    "year" to now_year,
                    "term" to term+1,
                    "period" to period,
                    "timetable_id" to doc_id,
                    "timetable_name" to timetable_title
                )

                Log.d("hoge", "$tt_data")

                user_tt_doc
                    .document(doc_id)
                    .set(tt_data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "作成が成功しました", Toast.LENGTH_SHORT).show()

                        set_timetable_id(this, doc_id)
                        set_timetable_semester(this, doc_id)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "作成が失敗しました", Toast.LENGTH_SHORT).show()
                        Log.w(TAG, "create timetable -> failure", it)
                    }

            }else{
                Toast.makeText(this, "未入力があります", Toast.LENGTH_SHORT).show()
            }
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