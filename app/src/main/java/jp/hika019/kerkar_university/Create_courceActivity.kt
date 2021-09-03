package jp.hika019.kerkar_university

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_courcet.*

class Create_courceActivity: AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_courcet)

        setToolbar()

        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            finish()
        }



        val period_item = arrayOf("1", "2", "3", "4", "5")
        val week_item =arrayOf("月", "火", "水", "木", "金", "土")



        periodPicker.maxValue = 5
        periodPicker.minValue = 1

        weekPicker.maxValue = 4
        weekPicker.minValue = 0
        weekPicker.displayedValues = week_item

        val linearLayout = findViewById<LinearLayout>(R.id.Create_class_linearLayout)
        var teacher_num = 1

        Add_button.setOnClickListener {
            if (teacher_num <3){
                teacher_num += 1
                val edittext = EditText(this)
                edittext.hint = " 教員名$teacher_num"
                edittext.textSize = 24F
                linearLayout.addView(edittext)
            }else{
                Toast.makeText(this, "上限に達しました", Toast.LENGTH_SHORT).show()
            }

        }
        /*
        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))

         */

        button4.setOnClickListener {
            Log.d("hogee", "call")
            val period = periodPicker.value
            val week = weekPicker.value
            Log.d("hogee", "period: $period /week: ${week_item[week]}")
            finish()
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