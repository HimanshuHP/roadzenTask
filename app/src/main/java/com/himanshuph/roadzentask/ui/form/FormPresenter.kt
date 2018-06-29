package com.himanshuph.roadzentask.ui.form

import android.annotation.SuppressLint
import android.location.Address
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.himanshuph.roadzentask.data.DataManager
import com.himanshuph.roadzentask.utils.rx.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

class FormPresenter(val dataManager: DataManager, val schedulerProvider: SchedulerProvider): FormContract.Presenter {

    private val mCompositeDisposable = CompositeDisposable()
    var mView: FormContract.View? = null
    override fun attachView(view: FormContract.View) {
        mView = view
    }

    override fun getCompanyViewInfo() {
        mView?.showLoading()
        mCompositeDisposable.add(dataManager.loadCompanyDetails()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ requestDetail ->
                    mView?.showCompanyFormView(requestDetail)
                },{
                    mView?.showError()
                }))
    }

    override fun getRequesterViewInfo() {
        mView?.showLoading()
        mCompositeDisposable.add(dataManager.loadRequesterDetails()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ requestDetail ->
                    mView?.showRequesterFormView(requestDetail)
                },{
                    mView?.showError()
                }))
    }

    @SuppressLint("MissingPermission")
    override fun observerAddressChanges(locationProvider: ReactiveLocationProvider, locationRequest: LocationRequest?) {
        mCompositeDisposable.add(locationProvider.getUpdatedLocation(locationRequest)
                .flatMap { location -> locationProvider.getReverseGeocodeObservable(location.latitude, location.longitude, 1) }
                .map(object : Function<List<Address>, Address> {
                    override fun apply(addresses: List<Address>): Address {
                        return if (!addresses.isEmpty()) addresses[0] else throw Exception("Address Empty")
                    }
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ address ->
                    mView?.updateAddressView(address)
                }
                        , {t ->
                    Log.e(FormFragment.TAG,t.message)

                }))
    }

    override fun detachView() {
        mCompositeDisposable.dispose()
        mView = null
    }
}