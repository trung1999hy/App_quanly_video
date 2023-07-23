package com.example.valut.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.valut.R
import com.example.valut.databinding.ActivityMediaSelectBinding
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant
import com.example.valut.ui.ultis.Constant.INDEX_0
import com.example.valut.ui.ultis.Constant.KEY_PATH
import com.example.valut.ui.ultis.Constant.PERMISSIONS_EXTERNAL_STORAGE
import com.example.valut.ui.ultis.Constant.REQUEST_CODE_PERMISSION
import com.example.valut.ui.ultis.Constant.TYPE
import com.example.valut.ui.ultis.DialogUtils.dialogNotification
import com.example.valut.ui.ultis.FileUtils
import com.example.valut.ui.ultis.ItemClickDialogListener
import java.io.File
import java.util.Collections

class MediaSelectActivity : PermissionActivity() {
    private var binding: ActivityMediaSelectBinding? = null
    private var type = 0
    private val typeFile = 0
    private val isSortByName = false
    private val isSortByTime = false
    private var key: String? = null
    private var viewModel: MediaVaultViewModel? = null
    private var mediaAdapter: MediaSelectAdapter? = null
    private var mediaVaults: List<MediaVault>? = null
    private var item: MediaVault? = null
    private var arrayListCheck = ArrayList<MediaVault>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaSelectBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel = ViewModelProvider(this).get(MediaVaultViewModel::class.java)
        initView()
        listenerData()
        initAction()
    }

    private fun initView() {
        type = intent.getIntExtra(TYPE, TypeMedia.ALL)
        key = intent.getStringExtra(KEY_PATH)
        setTitleToolBar()
        if (type != TypeMedia.ALL) {
            if (key == null) {
                finish()
            }

        }
        setRecyclerView(binding!!.rvList, this)
    }

    private fun initAction() {
        binding!!.materialToolbar.setNavigationOnClickListener { view: View? -> finish() }
        binding!!.selectAll.setOnClickListener { mediaAdapter!!.setCheckAll(binding!!.selectAll.isChecked) }
        binding!!.trash.setOnClickListener { view: View? -> clearListMediaSelect() }
        binding!!.btnOk.setOnClickListener { view: View? -> restoreListSelect() }

    }

    private fun clearListMediaSelect() {
        val list = mediaAdapter!!.listCheck
        if (list.size > 0) {
            dialogNotification(this@MediaSelectActivity, getString(R.string.title_delete_media),
                getString(R.string.content_remove_media),
                object : ItemClickDialogListener {
                    override fun tvOk() {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            checkPermissionFileAll(object : CallBackPermission {
                                override fun onOpenSetting() {
                                    super.onOpenSetting()
                                }

                                override fun onPermissionNotAllow() {
                                    super.onPermissionNotAllow()
                                }

                                override fun onAllPermissionAllow() {
                                    viewModel!!.removeListMediaVault(list)
                                }
                            })
                        } else {
                            if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                                viewModel!!.removeListMediaVault(list)
                            } else requestPermission(
                                PERMISSIONS_EXTERNAL_STORAGE,
                                REQUEST_CODE_PERMISSION
                            )
                        }
                    }
                })
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun restoreListSelect() {
        val list = mediaAdapter!!.listCheck
        if (list.size > 0) {
            dialogNotification(this@MediaSelectActivity,
                getString(R.string.titile_restore),
                String.format(getString(R.string.content_restore), list.size.toString() + ""),
                object : ItemClickDialogListener {
                    override fun tvOk() {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            checkPermissionFileAll(object : CallBackPermission {
                                override fun onAllPermissionAllow() {
                                    viewModel!!.restoreListVault(list)
                                }
                            })
                        } else {
                            if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                                viewModel!!.restoreListVault(list)
                            } else requestPermission(
                                PERMISSIONS_EXTERNAL_STORAGE,
                                REQUEST_CODE_PERMISSION
                            )
                        }
                    }
                })
        }
    }

    private fun listenerData() {
        viewModel!!.getAllMedia(type).observe(this) { mediaVaults: List<MediaVault> ->
            this.mediaVaults = mediaVaults
            Collections.reverse(this.mediaVaults)
            checkListMedia(this.mediaVaults!!)
            if (type != TypeMedia.ALL) {
                if (File(key).parent == FileUtils.createFolderMediaVault(type))
                    this.mediaVaults = viewModel!!.filterDataByPath(key!!, this.mediaVaults!!)
            }
            mediaAdapter!!.submitList(this.mediaVaults)
            mediaAdapter!!.notifyDataSetChanged()
            if (type == TypeMedia.ALL) {
                if (mediaVaults.size > 0) {
                    binding!!.shapeBarLayout12.visibility = View.VISIBLE
                } else {
                    binding!!.shapeBarLayout12.visibility = View.GONE
                }
            }
        }
    }

    private fun checkListMedia(mediaVaults: List<MediaVault?>) {
        if (arrayListCheck.size > 0) {
            for (mediaVault in arrayListCheck) {
                val i = mediaVaults.indexOf(mediaVault)
                if (i >= INDEX_0) {
                    mediaVaults[i]!!.isCheck = true
                }
            }
        }
    }

    private fun setTitleToolBar() {
        if (type == TypeMedia.SOUND_TYPE) {
            binding!!.materialToolbar.title = getString(R.string.title_sound)
        } else if (type == TypeMedia.IMAGE_TYPE) {
            binding!!.materialToolbar.title = getString(R.string.title_image)
        } else if (type == TypeMedia.VIDEO_TYPE) {
            binding!!.materialToolbar.title = getString(R.string.title_video)
        } else if (type == TypeMedia.ALL) {
            binding!!.materialToolbar.title = getString(R.string.title_all_media)
        }
    }

    fun setRecyclerView(recyclerView: RecyclerView, context: Context?) {
        mediaAdapter = MediaSelectAdapter({ item ->
            this@MediaSelectActivity.item = item
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
        }, {

            binding?.selectAll?.isChecked = mediaAdapter!!.checkAll()
        })
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
            arrayListCheck = mediaAdapter!!.listCheck
            if (item!!.type == TypeMedia.SOUND_TYPE) {
                val intent = Intent(this, DetailMusicActivity::class.java)
                intent.putExtra(Constant.KEY_MEDIA_VAULT, item)
                startActivity(intent)
            } else if (item!!.type == TypeMedia.IMAGE_TYPE) {
                val intent = Intent(this, DetailImageActivity::class.java)
                intent.putExtra(Constant.KEY_MEDIA_VAULT, item)
                startActivity(intent)
            } else if (item!!.type == TypeMedia.VIDEO_TYPE) {
                val intent = Intent(this, DetailVideoActivity::class.java)
                intent.putExtra(Constant.KEY_MEDIA_VAULT, item)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2222 && resultCode == RESULT_OK) {
            val list = data!!.getStringArrayListExtra("output")
            if (list!!.size > 0) {
                for (item in list) {
                    viewModel!!.changFile(item, null, typeFile)
                }
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
}