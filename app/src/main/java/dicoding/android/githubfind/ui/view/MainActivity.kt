package dicoding.android.githubfind.ui.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dicoding.android.githubfind.adapter.ListUserAdapter
import dicoding.android.githubfind.data.remote.response.SimpleUser
import dicoding.android.githubfind.databinding.ActivityMainBinding
import dicoding.android.githubfind.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import dicoding.android.githubfind.data.Result
import dicoding.android.githubfind.ui.view.DetailUserActivity.Companion.EXTRA_DETAIL

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel: MainViewModel by viewModels()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.users.collect { result ->
                        showSearchingResult(result)
                        }
                    }
                }
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.searchUserByUsername(query ?: "")
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }


    private fun errorOccurred() {
        Toast.makeText(this@MainActivity, "An Error is Occurred", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
            binding.userList.visibility = View.GONE
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.userList.visibility = View.VISIBLE
        }
    }

    private fun showSearchingResult(result: Result<ArrayList<SimpleUser>>) {

        when(result) {
            is Result.Loading -> {
                binding.errorMessage.visibility = View.GONE
                binding.errorImage.visibility = View.GONE
                showLoading(true)
                lifecycleScope.launch {
                    kotlinx.coroutines.delay(8000)
                    if (binding.pbLoading.visibility == View.VISIBLE) {
                        showLoading(false)
                        showError()
                    }
                }
            }
            is Result.Error -> {
                showLoading(false)
                showError()
            }
            is Result.Success -> {
                if (result.data.isEmpty()) {
                    showLoading(false)
                    showError()
                }else{
                    binding.errorMessage.visibility = View.GONE
                    binding.errorImage.visibility = View.GONE

                    val listUserAdapter = ListUserAdapter(result.data)

                    binding.userList.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = listUserAdapter
                        setHasFixedSize(true)
                    }

                    listUserAdapter.setOnItemClickCallback(object :
                        ListUserAdapter.OnItemClickCallback {
                        override fun onItemClicked(user: SimpleUser) {
                            goToDetailUser(user)
                        }

                    })
                    showLoading(false)
                }
            }
        }
    }

    private fun goToDetailUser(user: SimpleUser) {
        Intent(this@MainActivity, DetailUserActivity::class.java).apply {
            putExtra(EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }

    private fun showError() {
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorImage.visibility = View.VISIBLE
        binding.userList.visibility = View.GONE
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}