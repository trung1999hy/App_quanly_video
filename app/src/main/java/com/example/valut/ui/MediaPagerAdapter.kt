package com.example.valut.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MediaPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private var listFragment: List<Fragment> = arrayListOf()
    private var listTitle: List<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    fun setData(listFragment: List<Fragment>, listTitle: List<String>? = null) {
        this.listFragment = listFragment
        if (listTitle != null) {
            this.listTitle = listTitle
        }
        notifyDataSetChanged()
    }
}