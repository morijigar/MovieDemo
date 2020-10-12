package com.example.apifetch

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.apifetch.base.BaseActivity
import com.example.apifetch.databinding.ActivityMainBinding
import com.example.apifetch.fragment.MainFragment
import com.example.apifetch.viewmodel.MainActivityViewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainActivityViewModel
        get() = ViewModelProvider(this).get(MainActivityViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentReplaceBackPress(true, MainFragment.newInstance("", ""))

    }


}
