package com.himanshuph.roadzentask.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.himanshuph.roadzentask.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, CompanyDetailRequestFragment.newInstance(), CompanyDetailRequestFragment.TAG)
                    .commit()
    }

    fun onShowHideFragment(fragmentManager: FragmentManager, toShow: Fragment?, toHide: Fragment?) {
        if (toHide == null) {
            fragmentManager
                    .beginTransaction()
                    .show(toShow)
                    .commit()
            toShow?.onHiddenChanged(false)
        } else {
            toHide.onHiddenChanged(true)
            fragmentManager
                    .beginTransaction()
                    .hide(toHide)
                    .show(toShow)
                    .commit()
            toShow?.onHiddenChanged(false)
        }
    }

    fun onAddAndHide(fragmentManager: FragmentManager, toAdd: Fragment, toHide: Fragment?) {

        if (toHide == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container, toAdd, toAdd.javaClass.simpleName)
                    .commit()
            toAdd.onHiddenChanged(false)
        } else {
            toHide.onHiddenChanged(true)
            fragmentManager
                    .beginTransaction()
                    .hide(toHide)
                    .add(R.id.container, toAdd, toAdd.javaClass.simpleName)
                    .commit()
            toAdd.onHiddenChanged(false)
        }

    }
}
