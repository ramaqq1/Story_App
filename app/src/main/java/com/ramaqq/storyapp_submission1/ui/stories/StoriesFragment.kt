package com.ramaqq.storyapp_submission1.ui.stories

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramaqq.storyapp_submission1.databinding.FragmentStoriesBinding
import com.ramaqq.storyapp_submission1.di.ViewModelFactory
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import com.ramaqq.storyapp_submission1.ui.addstory.AddStoryActivity
import kotlinx.coroutines.flow.collect

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoriesFragment : Fragment() {
    private lateinit var binding: FragmentStoriesBinding
    private lateinit var pref: UserPreference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = UserPreference.getInstance(requireActivity().dataStore)

//        viewModel = ViewModelProvider(this)[StoriesViewModel::class.java]
//        val adapter = StoriesAdapter()

        var token: String
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel: StoriesViewModel by viewModels { factory }
        viewModel.init(pref)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireActivity(), AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.listStories.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = StoriesPagingAdapter()
//        binding.listStories.adapter = adapter

        binding.listStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.getUser().observe(viewLifecycleOwner) {
            viewModel.getData(it.token).observe(viewLifecycleOwner) { data ->
                if (data != null)
                    adapter.submitData(lifecycle, data)
            }


//        viewModel.getCompleteStories.observe(viewLifecycleOwner) {
//            if (it != null)
//                adapter.submitData(lifecycle, it)
//        }


/*        // old code
        viewModel.getStories.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setListData(it)
                adapter.notifyDataSetChanged()
            }else
                Toast.makeText(requireActivity(), "test", Toast.LENGTH_SHORT).show()
        }
        viewModel.getLoading.observe(viewLifecycleOwner) {
            if (it) binding.progressbar.visibility = View.VISIBLE
            else binding.progressbar.visibility = View.GONE
        }
        viewModel.getError.observe(viewLifecycleOwner){
            if (it){
                binding.connnection.root.visibility = View.VISIBLE
                binding.listStories.visibility = View.GONE
                binding.fabAdd.visibility = View.GONE
            }else{
                binding.connnection.root.visibility = View.GONE
                binding.listStories.visibility = View.VISIBLE
                binding.fabAdd.visibility = View.VISIBLE
            }
        }

        binding.listStories.layoutManager = LinearLayoutManager(requireActivity())
        binding.listStories.setHasFixedSize(true)
        binding.listStories.adapter = adapter*/
        }
    }
}