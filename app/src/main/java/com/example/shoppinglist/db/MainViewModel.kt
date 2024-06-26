package com.example.shoppinglist.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.entities.NoteItem
import kotlinx.coroutines.launch

class MainViewModel(database: MainDataBase) : ViewModel() {
    val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    fun insertNote(note: NoteItem?) = viewModelScope.launch {
        if (note != null) {
//            Log.d("MyLog", "В MainViewModel \n note = $note")
//            Log.d("MyLog", "В MainViewModel выполняется dao.insertNote(note)")
            dao.insertNote(note)
        }
    }
    fun deleteNote(id: Int) = viewModelScope.launch {
            dao.deleteNote(id)
    }
    class MainViewModelFactory(private val database: MainDataBase) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}