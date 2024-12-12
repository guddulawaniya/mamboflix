package com.mamboflix.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.mamboflix.BaseFragment
import com.mamboflix.R
import com.mamboflix.databinding.FragmentCommingsoonBinding

class CommingsoonFragment : BaseFragment() {

    lateinit var binding: FragmentCommingsoonBinding


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_commingsoon, container, false)
        //   (activity as HomeActivity).changeIcon(false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // (requireActivity() as HomeActivity).binding!!.llAppBarDashboard.toolbar.title = "......"

    }
}