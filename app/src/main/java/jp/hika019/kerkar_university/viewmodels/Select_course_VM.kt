package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.hika019.kerkar_university.tagHoge
import jp.hika019.kerkar_university.searchbar
import android.util.*
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Select_course_VM: ViewModel() {
    val search_str = MutableLiveData<String?>("")
    private val TAG = "Select_course_VM" + tagHoge

    val week_to_day = MutableLiveData("")
    val search_result = MutableLiveData("")

    val searchresult = MutableLiveData("")

    init {
        search_str.asFlow()
            .onEach {
                destroy()
            }
            .launchIn(viewModelScope)
    }

    fun search(){
        searchresult.value = "検索中"
    }

    fun non(){
        searchresult.value = ""
    }

    fun zero(){
        searchresult.value = "検索結果0件"
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