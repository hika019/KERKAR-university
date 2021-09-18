package jp.hika019.kerkar_university.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jp.hika019.kerkar_university.*
import jp.hika019.kerkar_university.databinding.ActivityLoadBinding
import jp.hika019.kerkar_university.viewmodels.Load_VM


class load_fragment: Fragment() {

    private val viewmodel by viewModels<Load_VM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = ActivityLoadBinding.inflate(inflater, container, false)
        view.viewmodel = viewmodel
        view.lifecycleOwner = viewLifecycleOwner



        return view.root
    }
}