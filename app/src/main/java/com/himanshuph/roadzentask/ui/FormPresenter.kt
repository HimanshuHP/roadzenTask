package com.himanshuph.roadzentask.ui

import com.himanshuph.roadzentask.data.DataManager
import com.himanshuph.roadzentask.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class FormPresenter(val dataManager: DataManager, val schedulerProvider: SchedulerProvider): FormContract.Presenter {

    private val mCompositeDisposable = CompositeDisposable()
    var mView: FormContract.View? = null
    override fun attachView(view: FormContract.View) {
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

    override fun getRequesterViewInfo() {
        mView?.showLoading()
        dataManager.loadRequesterDetails()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ requestDetail ->
                    mView?.showRequesterFormView(requestDetail)
                },{
                    mView?.showError()
                })
    }

    override fun detachView() {
        mCompositeDisposable.dispose()
        mView = null
    }
}