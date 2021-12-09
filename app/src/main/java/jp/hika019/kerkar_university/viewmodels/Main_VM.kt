package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.toHomeFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Main_VM: ViewModel() {

    private val homeFragment = MutableLiveData(false)

    init {
        toHomeFragment.asFlow()
            .onEach {

            }
            .launchIn(viewModelScope)
    }

}