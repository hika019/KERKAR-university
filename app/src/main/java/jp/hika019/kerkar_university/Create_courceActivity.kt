package jp.hika019.kerkar_university

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.generateViewId
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_courcet.*
import kotlinx.android.synthetic.main.activity_create_university.*

class Create_courceActivity: AppCompatActivity() {

    private val TAG = "Create_courceActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_courcet)

        setToolbar()

        val period_item = arrayOf("1", "2", "3", "4", "5")
        val week_item =arrayOf("月", "火", "水", "木", "金", "土")


        periodPicker.maxValue = 5
        periodPicker.minValue = 1

        weekPicker.maxValue = 5
        weekPicker.minValue = 1
        weekPicker.displayedValues = week_item


        val linearLayout = findViewById<LinearLayout>(R.id.Create_class_linearLayout)
        var teacher_num = 1
        var editText_id_list = arrayListOf<Int>()
        editText_id_list.add(R.id.teacherName_editTextText)

        class_edittext.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }

        teacherName_editTextText.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }



        Add_button.setOnClickListener {
            if (teacher_num <3){
                teacher_num += 1
                val edittext = EditText(this)
                edittext.hint = " 教員名$teacher_num"
                edittext.textSize = 24F
                edittext.maxEms = 10
                edittext.maxLines = 1
                edittext.maxLines = 10
                edittext.id = generateViewId()
                editText_id_list.add(edittext.id)
                linearLayout.addView(edittext)

                edittext.setOnFocusChangeListener { v, hasFocus ->
                    if(!hasFocus){
                        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    }
                }

            }else{
                Toast.makeText(this, "上限に達しました", Toast.LENGTH_SHORT).show()
            }

        }
        /*
        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))

         */

        CreateButton.setOnClickListener {
            /*
            Log.d(TAG, "call")
            val course_name = course_name_edittext.text.toString()
            val class_name = class_edittext.text.toString()
            val period = periodPicker.value
            val week = weekPicker.value
            val teacher_list = arrayListOf<String>()
            val hoge = teacherName_editTextText.text.toString()
            Log.d(TAG, "period: $period /week: ${week_item[week]}")
            Log.d(TAG, "授業名: "+ course_name)
            Log.d(TAG, "教室: "+ class_name)
            for (item_id in editText_id_list){
                val tmp_edittext = findViewById<EditText>(item_id)
                Log.d(TAG, "教師: "+tmp_edittext.text.toString())
                teacher_list.add(tmp_edittext.text.toString())
            }

            if (!(teacher_list.isNullOrEmpty()) && !(course_name.isNullOrEmpty()) && !(class_name.isNullOrEmpty()) && hoge != ""){
                Log.d(TAG, (week_to_day_symbol_list[week] + period).toString())
                val data = hashMapOf(
                    "semester_id" to semester!!,
                    "week_to_day" to (week_to_day_symbol_list[week] + period),
                    "course" to course_name,
                    "lecturer" to teacher_list,
                    "room" to class_name
                )
                val hoge = firedb_timetable(this)
                hoge.create_course_university_timetable(data)
                finish()
            }else{
                Toast.makeText(this, "未入力があります", Toast.LENGTH_SHORT).show()
            }

             */
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        /*
        // android.R.id.home に戻るボタンを押した時のidが取得できる
        if (item.itemId == android.R.id.home) {
            // 今回はActivityを終了させている
            finish()
        }

         */
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar(){
        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}