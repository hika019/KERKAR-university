package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.*
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Load_VM: ViewModel() {

    var period = MutableLiveData<Int>(-1)


    init {
        update_period()
        period.asFlow()
            .onEach {
                update_home_fragment()
            }
            .launchIn(viewModelScope)
    }

    fun update_period(){
        firedb.collection("user")
            .document(uid!!)
            .collection("timetable")
            .document(timetable_id!!)
            .get()
            .addOnSuccessListener {
                period.value = it.getLong("period")!!.toInt()
            }
            .addOnFailureListener {

            }
    }

    fun update_home_fragment(){
        if (period.value != -1)
            to_home_fragment.value = true
    }

}