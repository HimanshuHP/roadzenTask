package com.himanshuph.roadzentask.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
}
