package dicoding.android.githubfind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dicoding.android.githubfind.data.UserRepository
import dicoding.android.githubfind.data.remote.response.User
import dicoding.android.githubfind.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import dicoding.android.githubfind.data.Result
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val repository = UserRepository(apiService)

    private val _userDetail = MutableStateFlow<Result<User>>(Result.Loading)
    val userDetail = _userDetail.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    fun getDetailUser(username: String) {
        _userDetail.value = Result.Loading
        viewModelScope.launch {
            repository.getUserDetail(username).collect {
                _userDetail.value = it
            }
        }

        _isLoaded.value = true
    }

}