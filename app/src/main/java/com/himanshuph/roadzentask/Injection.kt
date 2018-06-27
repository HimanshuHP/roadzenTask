package com.himanshuph.roadzentask

import android.content.Context
import com.himanshuph.roadzentask.data.AppDataManager

object Injection {

    fun provideAppDataManager(): AppDataManager{
        return AppDataManager.getInstance()
    }
}