package dicoding.android.githubfind.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import dicoding.android.githubfind.R

class UIHelper {
    companion object {
        fun ShapeableImageView.setImageGlide(context: Context, url: String) {
            Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .into(this)
        }
    }
}