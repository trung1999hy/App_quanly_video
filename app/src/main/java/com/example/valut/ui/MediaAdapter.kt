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

class MediaAdapter(val callback: (MediaClassify) -> Unit) :
    BaseRecyclerAdapter<MediaClassify, MediaAdapter.ViewHolder>(DiffCallBack()) {
    class DiffCallBack : BaseDiffUtilItemCallback<MediaClassify>() {
        override fun areItemsTheSame(oldItem: MediaClassify, newItem: MediaClassify): Boolean {
            return oldItem.key == newItem.key
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MediaClassify, newItem: MediaClassify): Boolean {
            return oldItem == newItem
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            callback.invoke(currentList.get(position))
        }
    }

    private var type = TypeMedia.ALL

    fun setType(type: Int) {
        this.type = type
    }

    inner class ViewHolder(var binding: ViewDataBinding) :
        BaseViewHolder<MediaClassify, ViewDataBinding>(binding) {
        override fun bind(mediaClassify: MediaClassify) {
            onItemClickListener {

            }
            if (binding is ItemLayoutMediaBinding) {
                if (type == TypeMedia.SOUND_TYPE) {
                    (binding as ItemLayoutMediaBinding).imageView8.setImageResource(R.drawable.ic_music)
                } else {
                    Glide.with(itemView.context).load(mediaClassify.image)
                        .into((binding as ItemLayoutMediaBinding).imageView8)
                }
                (binding as ItemLayoutMediaBinding).textView12.text = mediaClassify.title
                (binding as ItemLayoutMediaBinding).textView14.text =
                    mediaClassify.number.toString() + ""
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_layout_media
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = getViewHolderDataBinding(parent, viewType)
        return ViewHolder(binding)
    }
}