package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.data.PreferencesManager
import com.codinginflow.mvvmtodo.data.SortOrder
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.data.TaskDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor (
    private val taskDao:TaskDao,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent=tasksEventChannel.receiveAsFlow()

    val searchQuery= MutableStateFlow("")
    val datastoreflow=preferencesManager.dataStoreFlow //Taking the flow of DataStore



    private val tasksFlow = combine(searchQuery,datastoreflow,
        {searchQuery,datastoreflow-> Pair(searchQuery,datastoreflow)})
        .flatMapLatest { taskDao.getTasks(it.first,it.second.sortOrder,it.second.hideCompleted) }

    // flatmaplatest is the collector and the combine method is actually producing/returning a Flow

    val tasks = tasksFlow.asLiveData()



    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortorder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onAddNewTaskClick()=viewModelScope.launch {                  //Fragment will call this method.Take and send via channel
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onTaskSelected(task: Task)=viewModelScope.launch {          //Fragment will call this method.Take and send via channel
        tasksEventChannel.send(TasksEvent.NavigateToAddEditTaskScreen(task))
    }


    sealed class TasksEvent{
        data class NavigateToAddEditTaskScreen(val task: Task):TasksEvent()
        object NavigateToAddTaskScreen: TasksEvent()
    }


}