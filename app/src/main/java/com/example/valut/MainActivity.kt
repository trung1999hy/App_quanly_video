package com.example.valut

import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import com.example.valut.databinding.ActivityMainBinding
import com.example.valut.ui.HistoryFragment
import com.example.valut.ui.MediaPagerAdapter
import com.example.valut.ui.VaultFragment
import com.example.valut.ui.base.BaseActivity
import com.example.valut.ui.webviewnews.list_webview.ListWebViewFragment
import java.util.Arrays

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var mediaPagerAdapter: MediaPagerAdapter? = null

    private val listFragments = Arrays.asList(
        VaultFragment.newInstance(),
        HistoryFragment.newInstance(), ListWebViewFragment.newInstance()
    )

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun init() {
        mediaPagerAdapter = MediaPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = mediaPagerAdapter
        mediaPagerAdapter?.setData(listFragments, null)
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.bottomNavigationView.selectedItemId = when (position) {
                    0 -> R.id.vault
                    1 -> R.id.history
                    2 -> R.id.news
                    else -> R.id.vault
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        binding.bottomNavigationView.setOnItemSelectedListener() {
            binding.viewPager.currentItem = when (it.itemId) {
                R.id.vault -> 0
                R.id.history -> 1
                R.id.news -> 2
                else -> 0
            }
            true
        }
    }

}