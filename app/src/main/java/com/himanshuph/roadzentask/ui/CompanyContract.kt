package com.himanshuph.roadzentask.ui

import com.himanshuph.roadzentask.BasePresenter
import com.himanshuph.roadzentask.BaseView
import com.himanshuph.roadzentask.data.model.RequestDetails

interface CompanyContract {

    interface View: BaseView {
        fun showLoading()
        fun showCompanyFormView(requestDetails: RequestDetails)
        fun showError()
    }

    interface Presenter: BasePresenter<View> {
        fun getCompanyViewInfo()
    }
}