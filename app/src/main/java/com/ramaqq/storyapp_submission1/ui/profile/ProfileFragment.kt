package com.ramaqq.storyapp_submission1.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ramaqq.storyapp_submission1.databinding.FragmentProfileBinding
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import com.ramaqq.storyapp_submission1.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var pref: UserPreference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = UserPreference.getInstance(requireActivity().dataStore)

        val viewModel: ProfileViewModel by requireActivity().viewModels()
//        Toast.makeText(requireActivity(), "text", Toast.LENGTH_SHORT).show()
        viewModel.init(pref)

        viewModel.getUser().observe(viewLifecycleOwner){ binding.tvUsername.text = it.userName }
        viewModel.getEmail().observe(viewLifecycleOwner){ binding.tvEmail.text = it}

        binding.laySetting2.isClickable = true
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
/*      log out alternative
        activity?.let {
                Intent(requireContext(), LoginActivity::class.java).also {
                    startActivity(it)
                }
                it.finish()
            }*/
        }
        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }
}