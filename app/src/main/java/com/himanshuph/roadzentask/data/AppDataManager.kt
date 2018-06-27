package com.himanshuph.roadzentask.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.himanshuph.roadzentask.App
import com.himanshuph.roadzentask.data.model.Question
import com.himanshuph.roadzentask.data.model.RequestDetails
import com.himanshuph.roadzentask.utils.SEED_ASSIGNMENT_1_OPTIONS
import com.himanshuph.roadzentask.utils.SEED_ASSIGNMENT_2_OPTIONS

import com.himanshuph.roadzentask.utils.loadJSONFromAsset
import io.reactivex.Observable


class AppDataManager() : DataManager {

    override fun loadCompanyDetails(): Observable<RequestDetails> {
        return Observable.fromCallable<RequestDetails> {
            val type = object : TypeToken<ArrayList<RequestDetails>>() {}.type
            val requestDetailList : ArrayList<RequestDetails> = Gson().fromJson(loadJSONFromAsset(App.getInstance(), SEED_ASSIGNMENT_1_OPTIONS), type)

            requestDetailList[0]

        }
    }

    override fun loadRequesterDetails(): Observable<RequestDetails> {
        return Observable.fromCallable<RequestDetails> {
            val type = object : TypeToken<ArrayList<RequestDetails>>() {}.type
            val requestDetailList : ArrayList<RequestDetails> = Gson().fromJson(loadJSONFromAsset(App.getInstance(), SEED_ASSIGNMENT_2_OPTIONS), type)
            requestDetailList[0]

        }
    }

    companion object {

        private var INSTANCE: AppDataManager? = null

        @JvmStatic
        fun getInstance(): AppDataManager {
            return INSTANCE ?: AppDataManager()
                    .apply { INSTANCE = this }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}