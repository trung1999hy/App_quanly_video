package com.example.valut.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.valut.R
import com.example.valut.databinding.FragmentMediaVaultBinding
import com.example.valut.ui.local.Preferences
import com.example.valut.ui.local.Preferences.Companion.getInstance
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant.PERMISSIONS_EXTERNAL_STORAGE
import com.example.valut.ui.ultis.Constant.REQUEST_CODE_PERMISSION
import com.example.valut.ui.ultis.Constant.TYPE
import com.example.valut.ui.ultis.FileUtils
import java.util.Arrays

class VaultFragment : PermissionFragment() {
    private var binding: FragmentMediaVaultBinding? = null

    private val viewModel: MediaVaultViewModel by viewModels()
    private var preferences: Preferences? = null
    private var typeDiaLog = 0
    private var typeOpen = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaVaultBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = getInstance(requireContext())
        initView()
        initAction()
        listerData()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        onRequestPermissionsResult(requestCode,
            PERMISSIONS_EXTERNAL_STORAGE,
            REQUEST_CODE_PERMISSION,
            R.string.title_memory_access_rights,
            R.string.title_permission_store,
            object : CallBackPermission {
                override fun onAllPermissionAllow() {
                    FileUtils.createFolder()
                    FileUtils.createFolderMediaVault(typeOpen)
                    startActivity(typeOpen)
                }

                override fun onPermissionNotAllow() {}
                override fun onOpenSetting() {
                    openAppSettings()
                }
            })
    }

    private fun startActivity(type: Int) {
        var intent = Intent(requireActivity(), MediaActivity::class.java)
        if (type == TypeMedia.ALL) {
            intent = Intent(requireActivity(), MediaSelectActivity::class.java)
        }
        intent.putExtra(TYPE, type)
        startActivity(intent)
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun initView() {



    }

    private fun listerData() {
        viewModel!!.getAllMedia(TypeMedia.IMAGE_TYPE)
            .observe(viewLifecycleOwner) { mediaVaults: List<MediaVault?> ->
                binding!!.numberImage.text = mediaVaults.size.toString() + ""
            }
        viewModel!!.getAllMedia(TypeMedia.VIDEO_TYPE)
            .observe(viewLifecycleOwner) { mediaVaults: List<MediaVault?> ->
                binding!!.numberVideo.text = mediaVaults.size.toString() + ""
            }
        viewModel!!.getAllMedia(TypeMedia.SOUND_TYPE)
            .observe(viewLifecycleOwner) { mediaVaults: List<MediaVault?> ->
                binding!!.numberSound.text = mediaVaults.size.toString() + ""
            }
        viewModel!!.getAllMedia(TypeMedia.ALL)
            .observe(viewLifecycleOwner) { mediaVaults: List<MediaVault?> ->
                binding!!.numberAll.text = mediaVaults.size.toString() + ""
            }
    }

    private fun initAction() {
        binding!!.image.setOnClickListener { view1: View? ->
            typeOpen = TypeMedia.IMAGE_TYPE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionFileAll(object : CallBackPermission {
                    override fun onAllPermissionAllow() {
                        startActivity(typeOpen)
                    }
                })
            } else {
                if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                    startActivity(typeOpen)
                }
                requestPermission(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION)
            }
        }
        binding!!.video.setOnClickListener { view1: View? ->
            typeOpen = TypeMedia.VIDEO_TYPE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionFileAll(object : CallBackPermission {
                    override fun onAllPermissionAllow() {
                        startActivity(typeOpen)
                    }
                })
            } else {
                if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                    startActivity(typeOpen)
                } else requestPermission(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION)
            }
        }
        binding!!.sound.setOnClickListener { view1: View? ->
            typeOpen = TypeMedia.SOUND_TYPE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionFileAll(object : CallBackPermission {
                    override fun onAllPermissionAllow() {
                        startActivity(typeOpen)
                    }
                })
            } else {
                if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                    startActivity(typeOpen)
                } else requestPermission(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION)
            }
        }
        binding!!.all.setOnClickListener { view1: View? ->
            typeOpen = TypeMedia.ALL
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionFileAll(object : CallBackPermission {
                    override fun onAllPermissionAllow() {
                        startActivity(typeOpen)
                    }
                })
            } else {
                if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                    startActivity(typeOpen)
                } else requestPermission(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION)
            }
        }

//        binding!!.vip.setOnClickListener { view1: View? ->
//            val intent = Intent(activity, InAppActivity::class.java)
//            startActivity(intent)
//        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2222 && resultCode == Activity.RESULT_OK) {
            val list = data!!.getStringArrayListExtra("output")
            if (list!!.size > 0) {
                for (item in list) {
                    viewModel!!.changFile(item, null, typeDiaLog)
                }
            }
        }
    }

    companion object {
        fun newInstance(): VaultFragment {
            return VaultFragment()
        }
    }
}