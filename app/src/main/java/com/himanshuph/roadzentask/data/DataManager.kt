package com.himanshuph.roadzentask.data

import com.himanshuph.roadzentask.data.model.RequestDetails
import io.reactivex.Observable

interface DataManager {
    fun loadCompanyDetails(): Observable<RequestDetails>
    fun loadRequesterDetails(): Observable<RequestDetails>

}