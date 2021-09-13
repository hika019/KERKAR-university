package jp.hika019.kerkar_university.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*


class Test_viewmodel: ViewModel() {
    val in_text = MutableLiveData<String>("")
    val button_enable = MutableLiveData(false)
    val in_num = MutableLiveData(0)

    val message: SharedFlow<String>
        get() = _text

    private val _text = MutableSharedFlow<String>()
    var i = 0


    init {
        in_text.asFlow()
            .onEach {
                Log.d("hoge", "aa: $it")
            }
            .launchIn(viewModelScope)

    }

    fun add(){
        i = i+1
        in_num.value = i
        Log.d("hoge", "aa: ${in_text.value}")

        if (10<=i)
            button_enable.value = true
    }


}