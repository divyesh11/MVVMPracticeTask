package com.example.mvvmpracticetask.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.example.mvvmpracticetask.constants.AppConstants
import com.example.mvvmpracticetask.constants.DatabaseConstants
import com.example.mvvmpracticetask.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = databaseBuilder(
        app,
        AppDatabase::class.java,
        DatabaseConstants.DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideAppSharedPreference(@ApplicationContext context: Context) =
        context.getSharedPreferences(
            AppConstants.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.getUserDao()
}