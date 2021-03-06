package com.himanshuph.roadzentask.ui

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.himanshuph.roadzentask.R
import com.himanshuph.roadzentask.ui.form.FormFragment
import com.roughike.bottombar.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),OnTabSelectListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar.setDefaultTab(R.id.form_view)
        bottomBar.setOnTabSelectListener(this)
    }


    override fun onTabSelected(tabId: Int) {
        onFragmentChanged(supportFragmentManager, tabId)
    }

    fun onFragmentChanged(fragmentManager: FragmentManager, tabId: Int) {
        try {
            val currentVisible = getVisibleFragment(fragmentManager)
            val formView = getFragmentByTag(fragmentManager, FormFragment.TAG) as FormFragment?
            val locationView = getFragmentByTag(fragmentManager, LocationFragment.TAG) as LocationFragment?
            when (tabId) {
                R.id.form_view -> {
                    if (formView == null)
                        onAddAndHide(fragmentManager, FormFragment.newInstance(), currentVisible)
                    else
                        onShowHideFragment(fragmentManager, formView, currentVisible)

                }
                R.id.map_view -> {
                    if (locationView == null)
                        onAddAndHide(fragmentManager, LocationFragment.newInstance(), currentVisible)
                    else
                        onShowHideFragment(fragmentManager, locationView, currentVisible)
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
                    .add(R.id.content_frame, toAdd, toAdd.javaClass.simpleName)
                    .commit()
            toAdd.onHiddenChanged(false)
        } else {
            toHide.onHiddenChanged(true)
            fragmentManager
                    .beginTransaction()
                    .hide(toHide)
                    .add(R.id.content_frame, toAdd, toAdd.javaClass.simpleName)
                    .commit()
            toAdd.onHiddenChanged(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FormFragment.REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val formView = getFragmentByTag(supportFragmentManager, FormFragment.TAG) as FormFragment?
                        formView?.onActivityResult(requestCode, resultCode, data)

                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {

        fun getFragmentByTag(fragmentManager: FragmentManager, tag: String): Fragment? {
            return fragmentManager.findFragmentByTag(tag)
        }

        fun getVisibleFragment(manager: FragmentManager): Fragment? {
            val fragments = manager.fragments
            if (fragments != null && !fragments.isEmpty()) {
                for (fragment in fragments) {
                    if (fragment != null && fragment.isVisible) {
                        return fragment
                    }
                }
            }
            return null
        }
    }
}
