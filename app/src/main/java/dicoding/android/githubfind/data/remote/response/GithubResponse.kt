package dicoding.android.githubfind.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseSearch(
    @field:SerializedName("items")
    val items: ArrayList<SimpleUser>
)

data class User(

    @field:SerializedName("bio")
    val bio: String?,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("public_repos")
    val publicRepos: Int,
)

data class SimpleUser(

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("login")
    val login: String
)
