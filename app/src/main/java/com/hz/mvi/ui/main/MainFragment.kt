package com.hz.mvi.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hz.mvi.R
import com.hz.mvi.model.BlogPost
import com.hz.mvi.model.User
import com.hz.mvi.ui.main.BlogListAdapter.Interaction
import com.hz.mvi.ui.main.state.MainStateEvent.GetBlogPostEvent
import com.hz.mvi.ui.main.state.MainStateEvent.GetUser
import com.hz.mvi.utils.TopItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), Interaction {

    lateinit var viewModel: MainViewModel
    lateinit var dataStateHandler: DataStateListener
    lateinit var blogListAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        subscribeObservers()
        initRecyclerView()
    }

    fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItem = TopItemDecoration(30)
            addItemDecoration(topSpacingItem)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }

    }

    private fun setUserProperties(user: User) {
        email.text = user.email
        username.text = user.username
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .apply(RequestOptions().override(600, 200))
                .error(R.mipmap.ic_launcher_round)
                .into(image)
        }

    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let { list ->
                println("DEBUG : setting blog post to RV \n $list ")
                blogListAdapter.submitList(list)
            }
            viewState.user?.let {
                println("DEBUG : setting user data  ${it} ")
                setUserProperties(it)
            }

        })

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG : dataState $dataState ")

            // Handling loading and message
            dataStateHandler.onDataStateChange(dataState)

            dataState.data?.let { event ->

                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.blogPosts?.let { blogPosts ->
                        // set blogPost data
                        viewModel.setBlogListData(blogPosts)
                    }

                    mainViewState.user?.let { user ->
                        //set user data
                        viewModel.setUser(user)
                    }
                }

            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_blogs -> triggerGetBlogsEvent()

            R.id.action_get_user -> triggerGetUserEvent()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUser("1"))
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostEvent())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG : $context must implement DataStateListener")
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG : Click $position")
        println("DEBUG : item $item")
    }


}