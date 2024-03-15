package dicoding.android.githubfind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dicoding.android.githubfind.data.remote.response.SimpleUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dicoding.android.githubfind.data.Result
import dicoding.android.githubfind.data.UserRepository
import dicoding.android.githubfind.data.remote.retrofit.ApiConfig

class FollowersViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val repository = UserRepository(apiService)

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    private val _followers = MutableStateFlow<Result<ArrayList<SimpleUser>>>(Result.Loading)
    val followers = _followers.asStateFlow()

    fun getUserFollowers(username: String) {
        _followers.value = Result.Loading
        viewModelScope.launch {
            repository.getUserFollowers(username).collect {
                _followers.value = it
            }
        }

        _isLoaded.value = true
    }
}