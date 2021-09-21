package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.hika019.kerkar_university.TAG_hoge
import jp.hika019.kerkar_university.searchbar
import android.util.*
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Select_course_VM: ViewModel() {
    val search_str = MutableLiveData<String?>("")
    private val TAG = "Select_course_VM" + TAG_hoge

    val week_to_day = MutableLiveData("")

    init {
        search_str.asFlow()
            .onEach {
                destroy()
            }
            .launchIn(viewModelScope)
    }

    fun destroy(){
        Log.d(TAG, "destroy -> call")
        val hoge = search_str.value
        if (hoge?.takeLast(1).equals("\n")){
            search_str.value = hoge?.substring(0, hoge.length-1)
        }
        searchbar.value = search_str.value
    }
}