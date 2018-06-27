package com.himanshuph.roadzentask.ui

import com.himanshuph.roadzentask.data.DataManager
import com.himanshuph.roadzentask.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class CompanyRequestPresenter(val dataManager: DataManager,val schedulerProvider: SchedulerProvider): CompanyContract.Presenter {

    private val mCompositeDisposable = CompositeDisposable()
    var mView: CompanyContract.View? = null
    override fun attachView(view: CompanyContract.View) {
        mView = view
    }

    override fun getCompanyViewInfo() {
        mView?.showLoading()
        dataManager.loadCompanyDetails()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ requestDetail ->
                    mView?.showCompanyFormView(requestDetail)
                },{
                    mView?.showError()
                })
    }

    override fun detachView() {
        mCompositeDisposable.dispose()
        mView = null
    }
}