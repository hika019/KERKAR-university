package jp.hika019.kerkar_university.walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import jp.hika019.kerkar_university.R

class walkthrough1: Fragment() {

    var walkThroughType : WalkThroughType? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.walkthrough_fragment1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(WalkThroughTypeKey) }?.apply {
            walkThroughType = WalkThroughType.values().mapNotNull {
                if (getInt(WalkThroughTypeKey) == it.ordinal) it else null}.first()
//            initWalkThroughPage(view)
        }

    }

//    private fun initWalkThroughPage(argView : View) {
//
//        val linearLayout : FrameLayout = argView.findViewById(R.id.frame_layout)
//        val imageView : ImageView = argView.findViewById(R.id.imageView)
//        val textView : TextView = argView.findViewById(R.id.title)
//
//        when (walkThroughType){
//            WalkThroughType.First -> {
//                linearLayout.setBackgroundResource(R.color.walk_through_1)
//                imageView.setImageResource(R.mipmap.fragment1)
//                textView.text = getText(R.string.first_fragment_title)
//            }
//            WalkThroughType.Second -> {
//                linearLayout.setBackgroundResource(R.color.walk_through_2)
//                imageView.setImageResource(R.mipmap.fragment2)
//                textView.text = getText(R.string.second_fragment_title)
//            }
//            WalkThroughType.Third -> {
//                linearLayout.setBackgroundResource(R.color.walk_through_3)
//                imageView.setImageResource(R.mipmap.fragment3)
//                textView.text = getText(R.string.third_fragment_title)
//            }
//        }
//
//    }
}