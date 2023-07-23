package com.example.valut.ui.webviewnews.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.valut.databinding.ActivityNewBinding

class NewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
        binding.webview.loadUrl(intent.getStringExtra("url") ?: "")
    }
}