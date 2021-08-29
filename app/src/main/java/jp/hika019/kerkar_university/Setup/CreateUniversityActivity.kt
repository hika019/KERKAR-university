package jp.hika019.kerkar_university.Setup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.hika019.kerkar_university.R
import kotlinx.android.synthetic.main.activity_create_university.*

class CreateUniversityActivity: AppCompatActivity() {
    private val TAG = "CreateUniversityActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_university)


        university_name_edittext.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus){
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }



        create_university_button.setOnClickListener {
            val radio_id = radioGroup.checkedRadioButtonId
            val radio_index = radioGroup.indexOfChild(radioGroup.findViewById<RadioButton>(radio_id))
            Log.d(TAG, "aaa$radio_index")

            if(university_name_edittext.text.isNullOrEmpty() || university_name_edittext.text.equals("")){
                Toast.makeText(this, "大学名を入力してください", Toast.LENGTH_SHORT).show()
            }else if (radio_index == -1){
                Toast.makeText(this, "学期を選択してください", Toast.LENGTH_SHORT).show()
            }else{
                val university_name = university_name_edittext.text
                val instance = setup()
                instance.create_university(this, university_name.toString(), radio_index+2)
            }
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        create_university_background.requestFocus()
        return super.dispatchTouchEvent(ev)
    }
}