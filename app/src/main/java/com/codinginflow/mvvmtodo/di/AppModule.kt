package com.codinginflow.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codinginflow.mvvmtodo.data.Task
import com.codinginflow.mvvmtodo.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton
import javax.security.auth.callback.Callback

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app:Application,
    callback: TaskDatabase.Callback
    )= Room.databaseBuilder(app,TaskDatabase::class.java,"task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideTaskDao(db:TaskDatabase)=db.taskDao()

    @Provides
    @Singleton
    fun provideApplicationScope()= CoroutineScope(SupervisorJob())

}