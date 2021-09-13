package jp.hika019.kerkar_university.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*


class Test_viewmodel: ViewModel() {
    val in_text = MutableLiveData<String>("")

    val message: SharedFlow<String>
        get() = _text

    private val _text = MutableSharedFlow<String>()
    var i = 0


    init {
        in_text.value = i.toString()
    }

    fun hoge(){
        i = i+1
        in_text.value = i.toString()
    }


}