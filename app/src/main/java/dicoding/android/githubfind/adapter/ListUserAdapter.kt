package dicoding.android.githubfind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dicoding.android.githubfind.data.remote.response.SimpleUser
import dicoding.android.githubfind.databinding.UserSearchItemBinding
import dicoding.android.githubfind.utils.UIHelper.Companion.setImageGlide

class ListUserAdapter(private val listUser: ArrayList<SimpleUser>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: UserSearchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = UserSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.apply {
            titleUser.text = user.login
            imageUser.setImageGlide(holder.itemView.context, user.avatarUrl)
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(user: SimpleUser)
    }
}
