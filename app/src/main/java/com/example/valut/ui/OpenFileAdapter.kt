package com.example.valut.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.stranger.base.recyclerview.BaseViewHolder
import com.example.valut.R
import com.example.valut.databinding.ItemLayoutMediaBinding
import com.example.valut.ui.base.recyclerview.BaseDiffUtilItemCallback
import com.example.valut.ui.base.recyclerview.BaseRecyclerAdapter
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.TimeUtils

class OpenFileAdapter(val callBack : (MediaVault)-> Unit) :
    BaseRecyclerAdapter<MediaVault, OpenFileAdapter.ViewHolder>(DiffCallBack()) {

    class DiffCallBack : BaseDiffUtilItemCallback<MediaVault>() {
        override fun areItemsTheSame(oldItem: MediaVault, newItem: MediaVault): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MediaVault, newItem: MediaVault): Boolean {
            return oldItem == newItem
        }


    }

    inner class ViewHolder(val binding: ViewDataBinding) :
        BaseViewHolder<MediaVault, ViewDataBinding>(binding) {



        override fun bind(mediaVault: MediaVault) {
            if (binding is ItemLayoutMediaBinding) {
                onItemClickListener {
                    callBack.invoke(mediaVault)
                }
                if (mediaVault.type === TypeMedia.SOUND_TYPE) {
                    binding.imageView8.setImageResource(R.drawable.ic_music)
                } else {
                    Glide.with(itemView.context).load(mediaVault.newPath)
                        .into(binding.imageView8)
                }
                binding.textView12.setText(mediaVault.name)
                if (mediaVault.timeModified > 0) binding.textView14.setText(
                    StringBuilder()
                        .append(TimeUtils.compareTimeWithCurrentTime(mediaVault.timeOpen))
                )
                if (mediaVault.duration > 0) binding.textView13.setText(
                    TimeUtils.formatElapsedTime(
                        null,
                        mediaVault.duration / 1000L
                    )
                )
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_layout_media
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding  =  getViewHolderDataBinding(parent, viewType)
        return ViewHolder(binding)
    }
}