package jp.hika019.kerkar_university.test


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.hika019.kerkar_university.firebase_test
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
        firebase_test.get_course_id()
        viewModelScope.launch {
            test_course_id.collect {
                set()
            }
        }
    }

    fun set(){
        wed2 = test_course_id.value?.get("wed2") as?  MutableStateFlow<String?>
    }


}