package com.example.apifetch.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apifetch.BR
import com.example.apifetch.R
import com.example.apifetch.adapter.PostAdapter
import com.example.apifetch.base.BaseFragment
import com.example.apifetch.databinding.FragmentMainBinding
import com.example.apifetch.model.Result
import com.example.apifetch.viewmodel.MainFragmentViewModel
import com.example.myapplication.databse.Database
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>() {

    var adapter: PostAdapter? = null


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var page = 1
    private var availablePages = 10
    private var pos = 0
    private var limit = 10
    private var offset = 50
    private var isOnline = true
    private var isWithOffset = false
    private var selection = 1

    private var listData: MutableList<Result> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main
    override val viewModel: MainFragmentViewModel
        get() = ViewModelProvider(this).get(MainFragmentViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    fun initData() {

        //getDataInit()

        showData()

        viewDataBinding.swipeRefresh.setOnRefreshListener {
            listData.clear()
            page = 1
            pos = 0
            handleDataChange(selection)
        }

        viewDataBinding.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    isWithOffset = false
                    listData.clear()
                    page = 1
                    pos = 0
                    selection = position
                    handleDataChange(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

        val layoutManager = GridLayoutManager(baseActivity, 2)
        viewDataBinding.recycler.layoutManager = layoutManager
        viewDataBinding.recycler.setHasFixedSize(true)

        initScrollListener()

    }

    fun handleDataChange(which: Int) {
        Log.e("---->", "$which")
        viewDataBinding.progress.visibility = View.VISIBLE

        when (which) {
            0 -> {
                callPopular()
            }
            1 -> {
                callTopRated()
            }
            2 -> {
                callUpcoming()
            }

        }
    }

    fun callPopular() {
        if (baseActivity!!.isOnline()) {
            //Api call flow
            isOnline = true
            viewModel.callPopular(page)
        } else {
            //RoomDB flow
            isOnline = false
            viewModel.getPopular(baseActivity!!)
        }
    }

    fun callTopRated() {
        if (baseActivity!!.isOnline()) {
            //Api call flow
            isOnline = true
            viewModel.callTopRated(page)
        } else {
            //RoomDB flow
            isOnline = false
            viewModel.getTopRated(baseActivity!!)
        }
    }

    fun callUpcoming() {
        if (baseActivity!!.isOnline()) {
            //Api call flow
            isOnline = true
            viewModel.callUpcoming(page)
        } else {
            //RoomDB flow
            isOnline = false
            viewModel.getUpcoming(baseActivity!!)
        }
    }


    fun getDataInit() {
        viewDataBinding.progress.visibility = View.VISIBLE
        if (baseActivity!!.isOnline()) {
            //Api call flow
            isWithOffset = false
            isOnline = true
            viewModel.callPopular(page)
        } else {
            //RoomDB flow
            isOnline = false
            isWithOffset = true
            viewModel.getLocalList(offset, limit, baseActivity!!)
        }
    }

    private fun showData() {

        viewModel.listData.observe(viewLifecycleOwner, Observer {
            viewDataBinding.progress.visibility = View.GONE
            viewDataBinding.swipeRefresh.isRefreshing = false
            availablePages = it!!.totalPages
            if (null == adapter) {
                listData.clear()
                listData.addAll(it.results)
                adapter = PostAdapter(baseActivity!!, listData)
                viewDataBinding.recycler.adapter = adapter
            } else {
                //adapter?.updateList(it)
                listData.addAll(it.results)
                adapter?.notifyDataSetChanged()
            }

            isLoading = false
            viewModel.viewModelScope.launch {
                Database.getInstance(baseActivity!!).appDatabase.moviesDao().save(it.results)
            }

        })

        viewModel.listDataLocal.observe(viewLifecycleOwner, Observer {
            viewDataBinding.progress.visibility = View.GONE
            viewDataBinding.swipeRefresh.isRefreshing = false
            if (null == adapter) {
                listData.clear()
                listData.addAll(it)
                adapter = PostAdapter(baseActivity!!, listData)
                viewDataBinding.recycler.adapter = adapter
            } else {
                //adapter?.updateList(it)
                listData.addAll(it)
                adapter?.notifyDataSetChanged()
            }
            isLoading = false
        })


    }

    //No search for now
    /*private fun setSearchFlow() {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewDataBinding.edSearch.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    return@filter !query.isEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (adapter != null) {
                        adapter!!.dataFromLocal(query)
                            .catch {
                                mutableListOf<Result>()
                            }
                    } else {
                        flow {
                            mutableListOf<Result>()
                        }
                    }
                }
                .flowOn(Dispatchers.Default)
                .collect {
                    if (adapter != null) {
                        adapter!!.notifyDataSetChanged()
                    }
                }
        }

    }*/

    var isLoading = false
    private fun initScrollListener() {
        viewDataBinding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.getChildCount();
                    val totalItemCount = layoutManager.getItemCount();
                    val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (page < availablePages) {
                            page++
                            offset = listData.size - 1
                            loadMore()
                            isLoading = true
                        }
                    }
                }
                /* val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                 if (!isLoading) {

                     if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == listData.size - 1) {
                         //bottom of list!
                         if (page < availablePages) {
                             page++
                             offset = listData.size - 1
                             loadMore()
                             isLoading = true
                         }
                     }
                 }*/
            }
        })
    }

    private fun loadMore() {
        handleDataChange(selection)
    }
}