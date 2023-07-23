package com.example.valut.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.valut.R
import com.example.valut.databinding.FragmentOpenFileRecentlyBinding
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant
import com.example.valut.ui.ultis.Constant.PERMISSIONS_EXTERNAL_STORAGE
import com.example.valut.ui.ultis.Constant.REQUEST_CODE_PERMISSION
import com.example.valut.ui.ultis.Constant.TYPE
import java.util.Collections

class OpenFileFragment : PermissionFragment() {
    private var binding: FragmentOpenFileRecentlyBinding? = null
    private var mediaAdapter: OpenFileAdapter? = null
    private var type = 0
    private var viewModel: MediaVaultViewModel? = null
    private var item: MediaVault? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = requireArguments().getInt(TYPE, TypeMedia.ALL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOpenFileRecentlyBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MediaVaultViewModel::class.java)
        initView()
        listenerData()
    }

    private fun listenerData() {
        viewModel!!.getAllMedia(type)
            .observe(viewLifecycleOwner) { mediaVaults: List<MediaVault> ->
                Collections.reverse(mediaVaults)
                mediaAdapter!!.submitList(viewModel!!.checkTimeOpenFile(mediaVaults))
                mediaAdapter!!.notifyDataSetChanged()
                if (viewModel!!.checkTimeOpenFile(mediaVaults).isEmpty()) {
                    binding!!.noData.visibility = View.VISIBLE
                } else {
                    binding!!.noData.visibility = View.GONE
                }
            }
    }

    private fun initView() {
        setRecyclerView(binding!!.rvList, requireContext())
    }

    fun setRecyclerView(recyclerView: RecyclerView, context: Context?) {
        mediaAdapter = OpenFileAdapter { item: MediaVault? ->
            this@OpenFileFragment.item = item
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                checkPermissionFileAll(object : CallBackPermission {
                    override fun onAllPermissionAllow() {
                        startActivity()
                    }
                })
            } else {
                if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                    startActivity()
                } else requestPermission(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION)
            }
            null
        }
        recyclerView.adapter = mediaAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    fun startActivity() {
        if (item != null) {
            if (item!!.type == TypeMedia.SOUND_TYPE) {
                val intent = Intent(requireActivity(), DetailMusicActivity::class.java)
                intent.putExtra(Constant.KEY_MEDIA_VAULT, item)
                startActivity(intent)
            } else if (item!!.type == TypeMedia.IMAGE_TYPE) {
                val intent = Intent(requireActivity(), DetailImageActivity::class.java)
                intent.putExtra(Constant.KEY_MEDIA_VAULT, item)
                startActivity(intent)
            } else if (item!!.type == TypeMedia.VIDEO_TYPE) {
                val intent = Intent(requireActivity(), DetailVideoActivity::class.java)
                intent.putExtra(Constant.KEY_MEDIA_VAULT, item)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode,
            PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION,
            R.string.title_memory_access_rights,
            R.string.title_permission_store, object : CallBackPermission {
                override fun onAllPermissionAllow() {
                    startActivity()
                }
            })
    }

    companion object {
        fun newInstance(type: Int): OpenFileFragment {
            val fragment = OpenFileFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}