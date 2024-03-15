package dicoding.android.githubfind.ui.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dicoding.android.githubfind.R
import dicoding.android.githubfind.adapter.SectionsPagerAdapter
import dicoding.android.githubfind.data.UserObject
import dicoding.android.githubfind.data.remote.response.User
import dicoding.android.githubfind.databinding.ActivityDetailUserBinding
import dicoding.android.githubfind.ui.viewmodel.DetailViewModel
import kotlinx.coroutines.launch
import dicoding.android.githubfind.data.Result
import dicoding.android.githubfind.utils.UIHelper.Companion.setImageGlide

@Suppress("DEPRECATION")
class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!

    private var username: String? = null
    private var profileUrl: String? = null
    private var userDetail: UserObject? = null
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        username = intent.extras?.get(EXTRA_DETAIL) as String


        setContentView(binding.root)
        setViewPager()
        setToolbar("Profile")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    detailViewModel.userDetail.collect { result ->
                        onDetailUserReceived(result)
                    }
                }
                launch {
                    detailViewModel.isLoaded.collect { loaded ->
                        if (!loaded) detailViewModel.getDetailUser(username ?: "")
                    }
                }
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }

    private fun setViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        viewPager.adapter = SectionsPagerAdapter(this, username!!)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }


    private fun onDetailUserReceived(result: Result<User>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Error -> {
                errorOccurred()
                showLoading(false)
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
            is Result.Success -> {
                result.data.let { user ->
                    parseUserDetail(user)
                    val userObject = UserObject(
                        user.login,
                        user.avatarUrl
                    )

                    userDetail = userObject
                    profileUrl = user.htmlUrl
                }

                showLoading(false)
            }
        }
    }

    private fun errorOccurred() {
        binding.apply {
            detailContainer.visibility = View.INVISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLoading.visibility = View.VISIBLE
            binding.detailContainer.visibility = View.INVISIBLE
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.detailContainer.visibility = View.VISIBLE
        }
    }

    private fun parseUserDetail(user: User) {
        binding.apply {
            detailUserLogin.text = user.login
            detailUserName.text = user.name
            followersCount.text = user.followers.toString()
            followingCount.text = user.following.toString()
            detailUserAvatar.setImageGlide(this@DetailUserActivity, user.avatarUrl)
        }
    }



    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onDestroy() {
        _binding = null
        username = null
        profileUrl = null
        super.onDestroy()
    }
}