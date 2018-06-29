package com.himanshuph.roadzentask.ui.form

import android.location.Address
import com.google.android.gms.location.LocationRequest
import com.himanshuph.roadzentask.BasePresenter
import com.himanshuph.roadzentask.BaseView
import com.himanshuph.roadzentask.data.model.RequestDetails
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

interface FormContract {

    interface View: BaseView {
        fun showLoading()
        fun showCompanyFormView(requestDetails: RequestDetails)
        fun showRequesterFormView(requestDetails: RequestDetails)
        fun updateAddressView(address: Address)
        fun showError()
    }

    interface Presenter: BasePresenter<View> {
        fun getCompanyViewInfo()
        fun getRequesterViewInfo()
        fun observerAddressChanges(locationProvider: ReactiveLocationProvider,locationRequest: LocationRequest?)
    }
}