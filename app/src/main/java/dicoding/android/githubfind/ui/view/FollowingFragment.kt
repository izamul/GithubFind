package dicoding.android.githubfind.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dicoding.android.githubfind.adapter.SectionsPagerAdapter.Companion.ARGS_USERNAME
import dicoding.android.githubfind.adapter.ListUserAdapter
import dicoding.android.githubfind.ui.viewmodel.FollowingViewModel
import kotlinx.coroutines.launch
import dicoding.android.githubfind.data.Result
import dicoding.android.githubfind.data.remote.response.SimpleUser
import dicoding.android.githubfind.databinding.FragmentFollowingBinding


@Suppress("DEPRECATION")
class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val followingViewModel: FollowingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARGS_USERNAME) ?: ""
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                followingViewModel.following.collect {
                    onFollowingResultReceived(it)
                }
            }
            launch {
                followingViewModel.isLoaded.collect { loaded ->
                    if (!loaded) followingViewModel.getUserFollowing(username)
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLoading.visibility = View.VISIBLE
        else binding.pbLoading.visibility = View.GONE
    }

    private fun onFollowingResultReceived(result: Result<ArrayList<SimpleUser>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                showLoading(false)
            }
            is Result.Success -> {
                showFollowing(result.data)
                showLoading(false)
            }
        }
    }

    private fun showFollowing(users: ArrayList<SimpleUser>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = ListUserAdapter(users)

            binding.followList.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object :
                ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: SimpleUser) {
                    goToDetailUser(user)
                }

            })
        } else binding.tvStatus.visibility = View.VISIBLE
    }

    private fun goToDetailUser(user: SimpleUser) {
        Intent(activity, DetailUserActivity::class.java).apply {
            putExtra(DetailUserActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}