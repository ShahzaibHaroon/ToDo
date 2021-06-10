package com.app.todolist

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.app.todolist.databinding.ActivityMainBinding
import com.app.todolist.fragment.DashboardFragment
import com.app.todolist.helper.ActivityBase

class MainActivity : ActivityBase() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initViews()
        callFragment(R.id.mainContainer, DashboardFragment(), null)
    }

    private fun initViews() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            setStatusBarColor(ContextCompat.getColor(this, R.color.white), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }

}