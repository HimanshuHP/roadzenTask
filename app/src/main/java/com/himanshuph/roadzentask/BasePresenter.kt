package com.himanshuph.roadzentask


interface BasePresenter<in V : BaseView> {
    fun attachView(view: V)

    fun detachView()
}