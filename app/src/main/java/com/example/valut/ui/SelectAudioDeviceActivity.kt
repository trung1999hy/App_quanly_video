package com.example.valut.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.valut.R
import com.example.valut.databinding.ActivitySelectAudioBinding
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant
import com.example.valut.ui.ultis.Constant.PERMISSIONS_EXTERNAL_STORAGE
import com.example.valut.ui.ultis.Constant.REQUEST_CODE_PERMISSION
import com.example.valut.ui.ultis.DialogUtils
import com.example.valut.ui.ultis.ItemClickDialogListener

class SelectAudioDeviceActivity : PermissionActivity() {
    private var viewModel: MediaVaultViewModel? = null
    private var binding: ActivitySelectAudioBinding? = null
    private var mediaAdapter: MediaSelectAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAudioBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel =
            ViewModelProvider(this).get<MediaVaultViewModel>(MediaVaultViewModel::class.java)
        setRecyclerView(binding!!.rvList, this)
        listenerData()
        initAction()
    }

    private fun initAction() {
        binding!!.materialToolbar.setNavigationOnClickListener { view: View? -> finish() }

        binding!!.selectAll.setOnClickListener { mediaAdapter!!.setCheckAll(binding!!.selectAll.isChecked) }
        binding!!.btnOk.setOnClickListener { view: View? ->
            val list: ArrayList<MediaVault> = mediaAdapter!!.listCheck
            if (list.size > 0) {
                DialogUtils.dialogNotification(this@SelectAudioDeviceActivity,
                    getString(R.string.title_add_media_to_vault),
                    getString(R.string.content_add_sound),
                    object : ItemClickDialogListener {
                        override fun tvOk() {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                                checkPermissionFileAll(object : CallBackPermission {


                                    override fun onAllPermissionAllow() {
                                        for (mediaVault in list) {
                                            viewModel!!.changFile(
                                                mediaVault.pathCurrent,
                                                mediaVault.single,
                                                TypeMedia.SOUND_TYPE
                                            )
                                        }
                                        finish()
                                    }
                                })
                            } else {
                                if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                                    for (mediaVault in list) {
                                        viewModel!!.changFile(
                                            mediaVault.pathCurrent,
                                            mediaVault.single,
                                            TypeMedia.SOUND_TYPE
                                        )
                                    }
                                    finish()
                                } else requestPermission(
                                    PERMISSIONS_EXTERNAL_STORAGE,
                                    REQUEST_CODE_PERMISSION
                                )
                            }
                        }
                    })
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        code: Int, title: Int,
        content: Int, callBackPermission: CallBackPermission
    ) {
        onRequestPermissionsResult(
            requestCode,
            PERMISSIONS_EXTERNAL_STORAGE,
            REQUEST_CODE_PERMISSION,
            R.string.title_memory_access_rights,
            R.string.title_permission_store,
            object : CallBackPermission {
                override fun onAllPermissionAllow() {
                    val list: ArrayList<MediaVault> = mediaAdapter!!.listCheck
                    if (list.size > 0) {
                        DialogUtils.dialogNotification(
                            this@SelectAudioDeviceActivity,
                            getString(R.string.title_add_media_to_vault),
                            getString(R.string.content_add_sound),
                            object : ItemClickDialogListener {
                                override fun tvOk() {
                                    for (mediaVault in list) {
                                        viewModel!!.changFile(
                                            mediaVault.pathCurrent,
                                            mediaVault.single,
                                            TypeMedia.SOUND_TYPE
                                        )
                                    }
                                    finish()
                                }
                            })
                    }
                }
            })
    }

    private fun listenerData() {
        viewModel!!.getData()
        viewModel!!.listLiveDataSoundDevice.observe(
            this
        ) { mediaVault: ArrayList<MediaVault> ->

            mediaAdapter?.submitList(mediaVault)
            mediaAdapter!!.notifyDataSetChanged()
            binding!!.noData.visibility = if (mediaVault.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    fun setRecyclerView(recyclerView: RecyclerView, context: Context?) {
        mediaAdapter = MediaSelectAdapter({ item ->
            val intent =
                Intent(this@SelectAudioDeviceActivity, DetailMusicActivity::class.java)
            intent.putExtra(Constant.KEY_MEDIA_VAULT, item)
            intent.putExtra(Constant.TYPE, Constant.DEVICE)
            startActivity(intent)
        }, {
            binding!!.selectAll.isChecked = mediaAdapter!!.checkAll()
        })
        recyclerView.adapter = mediaAdapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}