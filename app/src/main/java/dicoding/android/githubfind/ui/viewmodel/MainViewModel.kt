package dicoding.android.githubfind.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dicoding.android.githubfind.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dicoding.android.githubfind.data.Result
import dicoding.android.githubfind.data.remote.response.SimpleUser
import dicoding.android.githubfind.data.remote.retrofit.ApiConfig

class MainViewModel: ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val repository = UserRepository(apiService)
    private val _users = MutableStateFlow<Result<ArrayList<SimpleUser>>>(Result.Loading)
    val users = _users.asStateFlow()

    init {
        searchUserByUsername("")
    }

    fun searchUserByUsername(query: String) {
        _users.value = Result.Loading
        viewModelScope.launch {
            repository.searchUserByUsername(query).collect {
                _users.value = it
            }
        }
    }
}