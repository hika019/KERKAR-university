package jp.hika019.kerkar_university.viewmodels

import androidx.lifecycle.*
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Load_VM: ViewModel() {

    var period = MutableLiveData<Int>(-1)


    init {
        timetable_id.asFlow()
            .onEach {
                update_period()
                period.asFlow()
                    .onEach {
                        update_home_fragment()
                    }
                    .launchIn(viewModelScope)
            }
            .launchIn(viewModelScope)
    }

    fun update_period(){
        timetable_id.value?.let {
            firedb.collection("user")
                .document(uid!!)
                .collection("timetable")
                .document(it)
                .get()
                .addOnSuccessListener {
                    period.value = it.getLong("period")!!.toInt()
                }
                .addOnFailureListener {

                }
        }
    }

    fun update_home_fragment(){
        if (period.value != -1)
            toHomeFragment.value = true
    }

}