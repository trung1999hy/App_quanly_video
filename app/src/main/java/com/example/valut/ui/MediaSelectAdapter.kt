package com.example.valut.ui

import android.annotation.SuppressLint
import android.view.View
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

class MediaSelectAdapter(val callBack: (MediaVault) -> Unit, val itemClickSlelect: () -> Unit) :
    BaseRecyclerAdapter<MediaVault, MediaSelectAdapter.ViewHolder>(DiffCallBack()) {

    class DiffCallBack : BaseDiffUtilItemCallback<MediaVault>() {
        override fun areItemsTheSame(oldItem: MediaVault, newItem: MediaVault): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MediaVault, newItem: MediaVault): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(var binding: ViewDataBinding) :
        BaseViewHolder<MediaVault, ViewDataBinding>(binding) {
        override fun bind(mediaVault: MediaVault) {
            onItemClickListener {
                callBack.invoke(mediaVault)
            }
            if (binding is ItemLayoutMediaBinding) {
                (binding as ItemLayoutMediaBinding).checkBox.visibility = View.VISIBLE
                (binding as ItemLayoutMediaBinding).checkBox.isChecked = mediaVault.isCheck
                (binding as ItemLayoutMediaBinding).checkBox.setOnClickListener(View.OnClickListener {
                    val isChecked: Boolean = !mediaVault.isCheck
                    val i: Int = currentList.indexOf(mediaVault)
                    if (i > -1) {
                        currentList[i].isCheck = isChecked
                        itemClickSlelect.invoke()
                        notifyItemChanged(i)
                    }
                })
                if (mediaVault.type === TypeMedia.SOUND_TYPE) {
                    (binding as ItemLayoutMediaBinding).imageView8.setImageResource(R.drawable.ic_music)
                } else {
                    Glide.with(binding.root.context).load(mediaVault.newPath)
                        .into((binding as ItemLayoutMediaBinding).imageView8)
                }
                (binding as ItemLayoutMediaBinding).textView12.text = mediaVault.name
                if (mediaVault.timeModified > 0) (binding as ItemLayoutMediaBinding).textView14.text =
                    StringBuilder()
                        .append(TimeUtils.getDateFormat(mediaVault.timeModified))
                        .append("  /  ")
                if (mediaVault.duration > 0) (binding as ItemLayoutMediaBinding).textView13.text =
                    TimeUtils.formatElapsedTime(
                        null,
                        mediaVault.duration / 1000L
                    )
            }
        }

    }

    fun setCheckAll(isBoolean: Boolean) {
        for (mediaVault in currentList) {
            if (mediaVault.isCheck !== isBoolean) {
                mediaVault.isCheck = isBoolean
            }
        }
        notifyDataSetChanged()
    }

    fun checkAll(): Boolean {
        for (mediaVault in currentList) {
            if (mediaVault.isCheck === false) {
                return false
            }
        }
        return true
    }


    val listCheck: ArrayList<MediaVault>
        get() {
            val listRemoveMediaVault: ArrayList<MediaVault> = ArrayList<MediaVault>()
            for (mediaVault in currentList) {
                if (mediaVault.isCheck) {
                    listRemoveMediaVault.add(mediaVault)
                }
            }
            return listRemoveMediaVault
        }
    val listData: MutableList<MediaVault>
        get() = currentList

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_layout_media
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = getViewHolderDataBinding(parent, viewType)
        return ViewHolder(binding)
    }
}