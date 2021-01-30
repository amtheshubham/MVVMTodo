package com.codinginflow.mvvmtodo.data

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder {BY_DATE, BY_NAME}

data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    val dataStore= context.createDataStore("user_preferences")     //Creating DataStore

    val SORTORDER= preferencesKey<String>("sort_order")            //Creating Two Keys
    val HIDECOMPLETED= preferencesKey<Boolean>("hide_completed")   //Creating Two Keys

    //Methods to update preferences

    suspend fun updateSortorder(sortOrder: SortOrder){

        dataStore.edit{preferences->
            preferences[SORTORDER]=sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean){

        dataStore.edit{preferences->
            preferences[HIDECOMPLETED]=hideCompleted
        }
    }

    //Creating Flow for DataStore (which will be executed after updation in DataStore values)

    val dataStoreFlow= dataStore.data.catch { exception ->

        if (exception is IOException) {
            Log.e(TAG, "Error reading preferences", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }

    }.map { preferences->

        val sortorder=SortOrder.valueOf(preferences[SORTORDER]?: SortOrder.BY_DATE.name)
        val hidecompleted=preferences[HIDECOMPLETED]?:false

        FilterPreferences(sortorder, hidecompleted)      //This is like returning an object from flow
    }
}