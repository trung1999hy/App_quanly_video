package com.example.valut.ui.base.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

    abstract class BaseDiffUtilItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        // Override this if your item have an id
        return oldItem == newItem
    }


    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}
