package jp.hika019.kerkar_university.test

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.firebase_test.get_all_course_data
import jp.hika019.kerkar_university.test_course_id
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class testviewmodel: ViewModel() {

    var wed2:MutableStateFlow<String?>? = MutableStateFlow<String?>(null)

    init {
        /*
        test_course_id
            .onEach { set() }
            .launchIn(viewModelScope)

         */
        get_all_course_data()

        viewModelScope.launch {
            test_course_id.collect {
                set()

            }
        }

    }

    fun set(){
        val tmp  = test_course_id.value
        Log.d("hoge", "wed2: ${tmp?.get("wed2")}")
    }

    fun get(): String? {
        return wed2 as String?
    }


}