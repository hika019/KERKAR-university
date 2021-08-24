package jp.hika019.kerkar_university.Setup

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.hika019.kerkar_university.R
import kotlinx.android.synthetic.main.activity_create_university.*

class CreateUniversityActivity: AppCompatActivity() {
    private val TAG = "CreateUniversityActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_university)

        create_university_button.setOnClickListener {
            if(university_name_edittext.text.isNullOrEmpty() || university_name_edittext.text.equals("")){
                Toast.makeText(this, "大学名を入力してください", Toast.LENGTH_SHORT).show()
            }else{
                val university_name = university_name_edittext.text
                val instance = setup()
                instance.create_university(this, university_name.toString())
            }
        }

    }
}