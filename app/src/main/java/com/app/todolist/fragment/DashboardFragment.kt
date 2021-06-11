package com.app.todolist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.todolist.R
import com.app.todolist.databinding.FragmentDashboardBinding
import com.app.todolist.helper.BaseFragment
import com.app.todolist.helper.Constants
import com.app.todolist.helper.VPDashBoardAdapter

class DashboardFragment : BaseFragment(), View.OnClickListener {

    lateinit var adapter: VPDashBoardAdapter
    lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        initViews()
        setListener()
        setUpPager()
        return binding.root
    }

    private fun setUpPager() {
        adapter = VPDashBoardAdapter(childFragmentManager)
        adapter.addFragment(HomeFragment(), "HomeFragment")
        adapter.addFragment(ListFragment(), "MyBlastFragment")
        binding.vpDash.adapter = adapter
        if (Constants.IS_HOME == 0) binding.rlHome.performClick()
        else binding.rlList.performClick()
    }

    private fun initViews() {
        binding.isHome = true
    }

    private fun setListener() {
        binding.rlHome.setOnClickListener(this)
        binding.rlList.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlHome -> {
                binding.isHome = true
                Constants.IS_HOME = 0
                binding.vpDash.setCurrentItem(0, true)
            }
            R.id.rlList -> {
                binding.isHome = false
                Constants.IS_HOME = 1
                binding.vpDash.setCurrentItem(1, true)
            }
        }
    }

}