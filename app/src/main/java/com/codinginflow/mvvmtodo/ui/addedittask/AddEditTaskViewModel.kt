package com.codinginflow.mvvmtodo.ui.addedittask

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.data.TaskDao

class AddEditTaskViewModel(
    @Assisted private val state: SavedStateHandle,
    private val taskDao: TaskDao
): ViewModel() {

    val task= state.get<Task>("task")  // Take the passed argument from 1st screen, If we are editing some task

    /*If the user typed something after "+" icon, we saved it into "taskName" so we are extracting it out
    but if it's null,then maybe the task is being updated but if that too is not the case then " "*/

   /* Also as we did in saved preferences, when we take the input from the user, we add it to savedinstance
   then we pass it to fragment*/

    var taskName= state.get<Task>("taskName")?:task?.name ?:""
    set(value) {
        field=value                        // Assigning taskName the entered value and also saving it to savedInstance
        state.set("taskName",value)
    }

    var taskImportance= state.get<Task>("important")?:task?.important ?:false
        set(value) {
            field=value                        // Assigning taskName the entered value and also saving it to savedInstance
            state.set("taskImportance",value)
        }
}