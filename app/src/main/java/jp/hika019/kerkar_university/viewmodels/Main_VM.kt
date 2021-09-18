package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.to_home_fragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Main_VM: ViewModel() {

    private val homeFragment = MutableLiveData(false)

    init {
        to_home_fragment.asFlow()
            .onEach {

            }
            .launchIn(viewModelScope)
    }

}