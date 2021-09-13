package jp.hika019.kerkar_university.test

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import jp.hika019.kerkar_university.R
import android.util.*
import jp.hika019.kerkar_university.databinding.TestFragmentBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class Test_activity: Fragment() {
    private val viewModel by viewModels<Test_viewmodel>()
    private lateinit var binding: TestFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View =
        TestFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }.also {
            viewModel.message.onEach {
                Log.d("hogee", "atext: $it")
            }.launchIn(lifecycleScope)
        }.root

}