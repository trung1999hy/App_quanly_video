package com.example.valut.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.valut.R
import com.example.valut.databinding.FragmentHistoryBinding
import com.example.valut.ui.base.BaseFragment
import java.util.Arrays


class HistoryFragment : BaseFragment() {
    private var mediaPagerAdapter: MediaPagerAdapter? = null
    private lateinit var binding: FragmentHistoryBinding
    private val listFragments = Arrays.asList(
        OpenFileFragment.newInstance(TypeMedia.ALL),
        OpenFileFragment.newInstance(TypeMedia.IMAGE_TYPE),
        OpenFileFragment.newInstance(TypeMedia.VIDEO_TYPE),
        OpenFileFragment.newInstance(TypeMedia.SOUND_TYPE)
    )

    companion object {

        @JvmStatic
        fun newInstance() =
            HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun init() {
        val listTitles = Arrays.asList(
            getString(R.string.title_all_media),
            getString(R.string.title_image),
            getString(R.string.title_video),
            getString(R.string.title_sound)
        )
        mediaPagerAdapter = MediaPagerAdapter(childFragmentManager)
        binding!!.viewPager.adapter = mediaPagerAdapter
        binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)
        mediaPagerAdapter!!.setData(listFragments, listTitles)
    }

    override fun initData() {

    }

    override fun initAction() {

    }
}