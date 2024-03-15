package dicoding.android.githubfind.data

import android.util.Log
import dicoding.android.githubfind.data.remote.response.SimpleUser
import dicoding.android.githubfind.data.remote.retrofit.ApiService
import dicoding.android.githubfind.data.remote.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import dicoding.android.githubfind.BuildConfig


class UserRepository @Inject constructor(
    private val apiService: ApiService
)  {
    suspend fun searchUserByUsername(q: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
            emit(Result.Loading)
            try {
                val response = apiService.searchUsername(q,API)
                val users = response.items.map { item ->
                    SimpleUser(
                        login = item.login,
                        avatarUrl = item.avatarUrl
                    )
                }
                emit(Result.Success(ArrayList(users)))
            } catch (e: Exception) {
//                Log.d(TAG, "searchUserByUsername: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
    }

    fun getUserFollowing(id: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)
        try {
            val users = apiService.getUserFollowing(id,API)
//            Log.d(TAG, "getUserFollowing: ${users.toString()}")
            emit(Result.Success(users))
        } catch (e: Exception) {
//            Log.d(TAG, "getUserFollowing: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowers(id: String): Flow<Result<ArrayList<SimpleUser>>> = flow {
        emit(Result.Loading)
        try {
            val users = apiService.getUserFollowers(id,API)
//            Log.d(TAG, "getUserFollowers: ${users.toString()}")
            emit(Result.Success(users))
        } catch (e: Exception) {
//            Log.d(TAG, "getUserFollowers: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserDetail(id: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try {
            val user = apiService.getUserDetail(id,API)
            emit(Result.Success(user))
        } catch (e: Exception) {
//            Log.d(TAG, "getUserDetail: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
    companion object {
        private val TAG = UserRepository::class.java.simpleName
        private val API: String = BuildConfig.API_KEY
    }

}