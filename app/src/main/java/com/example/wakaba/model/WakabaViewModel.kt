package com.example.wakaba.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.wakaba.Repository
import com.example.wakaba.room.Wakaba
import com.example.wakaba.room.WakabaDao
import com.example.wakaba.room.WakabaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WakabaViewModel(app:Application):AndroidViewModel(app) {
    private val db=WakabaDatabase.getDatabase(app,viewModelScope)
    private val dao:WakabaDao
    private var allData:LiveData<List<Wakaba>>
    private val repository:Repository
    init {
            dao=db.getDao()
            dao.run {
                allData = getAll()
                repository= Repository(this)
            }
        }
    fun insert(bean:Wakaba)=viewModelScope.launch(Dispatchers.IO) {
        repository.insert(bean)
    }
    fun delete(bean: Wakaba)=viewModelScope.launch(Dispatchers.IO){
        repository.delete(bean)
    }
    fun update(bean: Wakaba)=viewModelScope.launch(Dispatchers.IO) {
        repository.update(bean)
    }
    fun getAllData()=allData
}