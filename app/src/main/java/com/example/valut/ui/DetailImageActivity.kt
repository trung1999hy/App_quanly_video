package com.example.valut.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.valut.R
import com.example.valut.databinding.ActivityDetailImageBinding
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant
import com.example.valut.ui.ultis.Constant.PERMISSIONS_EXTERNAL_STORAGE
import com.example.valut.ui.ultis.Constant.REQUEST_CODE_PERMISSION
import com.example.valut.ui.ultis.DialogUtils
import com.example.valut.ui.ultis.ItemClickDialogListener
import java.util.Date

class DetailImageActivity : PermissionActivity() {
    private var binding: ActivityDetailImageBinding? = null
    private var mediaVault: MediaVault? = null
    private var viewModel: MediaVaultViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailImageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel =
            ViewModelProvider(this).get<MediaVaultViewModel>(MediaVaultViewModel::class.java)
        binding!!.materialToolbar3.setNavigationOnClickListener { view: View? -> finish() }
        binding!!.trash.setOnClickListener { view: View? ->
            DialogUtils.dialogNotification(this@DetailImageActivity,
                getString(R.string.title_delete_media),
                getString(R.string.content_remove_media),
                object : ItemClickDialogListener {
                    override fun tvOk() {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            checkPermissionFileAll(object : CallBackPermission {

                                override fun onAllPermissionAllow() {
                                    mediaVault?.let { viewModel!!.removeMediaVault(it) }
                                    finish()
                                }
                            })
                        } else {
                            if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                                mediaVault?.let { viewModel!!.removeMediaVault(it) }
                                finish()
                            } else requestPermission(
                                PERMISSIONS_EXTERNAL_STORAGE,
                                REQUEST_CODE_PERMISSION
                            )
                        }
                    }
                })
        }
        mediaVault = intent.getSerializableExtra(Constant.KEY_MEDIA_VAULT) as MediaVault?
        if (mediaVault != null) {
            val currentTimeInMillis = Date().time
            mediaVault!!.timeOpen = currentTimeInMillis
            viewModel!!.updateItemMediaVault(mediaVault)
            Glide.with(this).load(mediaVault!!.newPath).into(binding!!.image)
            binding!!.materialToolbar3.setTitle(mediaVault!!.name)
        } else finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, PERMISSIONS_EXTERNAL_STORAGE,
            REQUEST_CODE_PERMISSION,
            R.string.title_memory_access_rights,
            R.string.title_permission_store,
            object : CallBackPermission {
                override fun onAllPermissionAllow() {
                    mediaVault?.let { viewModel!!.removeMediaVault(it) }
                    finish()
                }
            })
    }
}