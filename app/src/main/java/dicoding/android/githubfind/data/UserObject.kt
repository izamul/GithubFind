package dicoding.android.githubfind.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserObject(
    var id: String,
    var avatarUrl: String,
) : Parcelable
