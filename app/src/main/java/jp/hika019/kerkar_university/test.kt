package jp.hika019.kerkar_university

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.test.view.*

class test: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.test, container, false)
        val constraintLayout = view.findViewById<View>(R.id.test_bg)

        val table = TableLayout(context)

        val tableRow = TableRow(context)
        tableRow.layoutParams = LinearLayout.LayoutParams(400, 700)
        tableRow.setVerticalGravity(CENTER)

        val text = TextView(context)
        text.text = "aaaaaaa"
        //text.layoutParams = LinearLayout.LayoutParams(200, 50)
        tableRow.addView(text)

        table.addView(text)



        //Thread.sleep(200)
        return view
    }
}