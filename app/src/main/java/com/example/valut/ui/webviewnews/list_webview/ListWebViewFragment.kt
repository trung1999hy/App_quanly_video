package com.example.valut.ui.webviewnews.list_webview

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.valut.databinding.FragmentListWebViewBinding
import com.example.valut.ui.base.BaseFragment
import com.example.valut.ui.chargecoin.ChargeCoinActivity
import com.example.valut.ui.local.Preferences
import com.example.valut.ui.webviewnews.news.NewActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListWebViewFragment : BaseFragment() {

    companion object {
        fun newInstance() = ListWebViewFragment()
    }

    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentListWebViewBinding
    private lateinit var viewModel: ListWebViewViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var preference: Preferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun init() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ListWebViewViewModel::class.java]
        preference = Preferences.getInstance(requireContext())
        database = FirebaseDatabase.getInstance("https://valut-83f58-default-rtdb.asia-southeast1.firebasedatabase.app/").reference;

        binding.chargeTvCoin.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    ChargeCoinActivity::class.java
                )
            )
        }
        initAdapter()
        setTextHeader()

    }

    private fun setTextHeader() {
        val currentCoin = preference?.getValueCoin()
        binding.chargeTvCoin.text = currentCoin.toString()
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter {
            val dialogRemove: AlertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Mở ")
                setMessage("Bạn dùng 2 vàng để mở chứ ? ")
                setPositiveButton("Đồng ý") { dialog, id ->
                    preference?.apply {
                        if (getValueCoin() >= 2) {
                            setValueCoin(getValueCoin() - 2)

                            Toast.makeText(
                                requireContext(),
                                "Đã thêm  thành công và trù 2 vàng",
                                Toast.LENGTH_SHORT

                            ).show()
                            setTextHeader()
                            val intent = Intent(activity, NewActivity::class.java)
                            intent.putExtra("url", it)
                            startActivity(intent)
                        } else startActivity(
                            Intent(
                                requireContext(),
                                ChargeCoinActivity::class.java
                            )
                        )
                    }

                }
                setNegativeButton("Đóng") { dialog, id ->
                    dialog.dismiss()
                }
            }.create()
            dialogRemove.show()


        }
        binding.recyclerView.adapter = newsAdapter
        getListData()
    }

    private fun getListData() {
        database.child("link").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var dataList: ArrayList<String> = arrayListOf()
                snapshot.children.forEach {
                    it.getValue(String::class.java)?.let { it1 -> dataList.add(it1) }
                }
                newsAdapter.setData(dataList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}