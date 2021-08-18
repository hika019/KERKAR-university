package jp.hika019.kerkar_university.Setup

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.collection.LLRBNode
import jp.hika019.kerkar_university.R
import kotlinx.android.synthetic.main.fragment_set_university.view.*

class Select_university_fragment(): Fragment() {
    val TAG = "Setup_select_university_fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set_university, container, false)

        val hoge= arrayListOf("東大", "京大", "筑波", "奈良先端科学", "東北", "名古屋", "北海道", "中京", "中部", "金沢")

        //view.setBackgroundColor(Color.RED)

        val adapter = Select_university_list_CustopmAdapter(hoge, hoge, view.context)
        val layouManager = LinearLayoutManager(view.context)

        view.select_university_recycle_view.layoutManager = layouManager
        view.select_university_recycle_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        view.select_university_recycle_view.adapter = adapter
        view.select_university_recycle_view.setHasFixedSize(true)

        return view
    }

}