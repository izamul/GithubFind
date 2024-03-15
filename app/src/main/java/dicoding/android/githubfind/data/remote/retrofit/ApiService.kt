package dicoding.android.githubfind.data.remote.retrofit

import dicoding.android.githubfind.data.remote.response.ResponseSearch
import dicoding.android.githubfind.data.remote.response.SimpleUser
import dicoding.android.githubfind.data.remote.response.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun searchUsername(
        @Query("q") q: String,
        @Header("Authorization") token: String
    ): ResponseSearch

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): User

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): ArrayList<SimpleUser>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): ArrayList<SimpleUser>
}
