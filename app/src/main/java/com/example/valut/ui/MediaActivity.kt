package com.example.valut.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.valut.R
import com.example.valut.databinding.ActivityMediaBinding
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant.KEY_PATH
import com.example.valut.ui.ultis.Constant.PERMISSIONS_EXTERNAL_STORAGE
import com.example.valut.ui.ultis.Constant.REQUEST_CODE_PERMISSION
import com.example.valut.ui.ultis.Constant.TYPE
import java.util.Collections


class MediaActivity : PermissionActivity() {
    private var binding: ActivityMediaBinding? = null
    private var viewModel: MediaVaultViewModel? = null
    private var type = 0
    val PICK_IMAGE_REQUEST = 222
    val PICK_VIDEO_REQUEST = 3000
    private var mediaAdapter: MediaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel =
            ViewModelProvider(this).get<MediaVaultViewModel>(MediaVaultViewModel::class.java)
        initView()
        listenerData()
        initAction()
        binding!!.materialToolbar2.setNavigationOnClickListener { finish() }
    }

    private fun listenerData() {
        if (type == TypeMedia.IMAGE_TYPE) {
            viewModel!!.getAllMedia(TypeMedia.IMAGE_TYPE)
                .observe(this) { mediaVaults: List<MediaVault> ->
                    if (mediaVaults.size > 0) {
                        val list: List<MediaVault?> = mediaVaults
                        Collections.reverse(list)
                        mediaAdapter!!.submitList(
                            viewModel!!.getListFolder(
                                type,
                                list as MutableList<MediaVault>
                            )
                        )
                        mediaAdapter!!.notifyDataSetChanged()
                        binding!!.shapeBarLayout12.visibility = View.VISIBLE
                    } else {
                        mediaAdapter!!.notifyDataSetChanged()
                        binding!!.shapeBarLayout12.visibility = View.GONE
                    }
                }
        } else if (type == TypeMedia.VIDEO_TYPE) {
            viewModel!!.getAllMedia(TypeMedia.VIDEO_TYPE)
                .observe(this) { mediaVaults: List<MediaVault> ->
                    if (mediaVaults.size > 0) {

                        val list: List<MediaVault?> = mediaVaults
                        Collections.reverse(list)
                        mediaAdapter!!.submitList(
                            viewModel!!.getListFolder(
                                type,
                                list as MutableList<MediaVault>
                            )
                        )
                        mediaAdapter!!.notifyDataSetChanged()
                        binding!!.shapeBarLayout12.visibility = View.VISIBLE
                    } else {
                        mediaAdapter!!.notifyDataSetChanged()
                        binding!!.shapeBarLayout12.visibility = View.GONE
                    }
                }
        } else if (type == TypeMedia.SOUND_TYPE) {
            viewModel!!.getAllMedia(TypeMedia.SOUND_TYPE)
                .observe(this) { mediaVaults: List<MediaVault> ->
                    if (mediaVaults.size > 0) {
                        val list: List<MediaVault?> = mediaVaults
                        Collections.reverse(list)
                        mediaAdapter!!.submitList(
                            viewModel!!.getListFolder(
                                type,
                                list as MutableList<MediaVault>
                            )
                        )
                        mediaAdapter!!.notifyDataSetChanged()
                        binding!!.shapeBarLayout12.visibility = View.VISIBLE
                    } else {
                        mediaAdapter!!.notifyDataSetChanged()
                        binding!!.shapeBarLayout12.visibility = View.GONE
                    }
                }
        }
    }

    private fun initView() {
        type = intent.getIntExtra(TYPE, TypeMedia.ALL)
        setTitleToolBar()
        setRecyclerView(binding!!.rvListAlbum, this)

    }

    private fun setTitleToolBar() {
        if (type == TypeMedia.SOUND_TYPE) {
            binding!!.materialToolbar2.title = getString(R.string.title_sound)
        } else if (type == TypeMedia.IMAGE_TYPE) {
            binding!!.materialToolbar2.title = getString(R.string.title_image)
        } else if (type == TypeMedia.VIDEO_TYPE) {
            binding!!.materialToolbar2.title = getString(R.string.title_video)
        } else if (type == TypeMedia.ALL) {
            binding!!.materialToolbar2.title = getString(R.string.title_all_media)
        }
    }

    private fun initAction() {
        setFabClick()
    }

    private fun setRecyclerView(recyclerView: RecyclerView, context: Context) {
        mediaAdapter = MediaAdapter { item ->
            val intent = Intent(this@MediaActivity, MediaSelectActivity::class.java)
            intent.putExtra(TYPE, type)
            intent.putExtra(KEY_PATH, item.key)
            startActivity(intent)
        }
        mediaAdapter!!.setType(type)
        recyclerView.setAdapter(mediaAdapter)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setFabClick() {
        binding!!.plus.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                checkPermissionFileAll(object : CallBackPermission {
                    override fun onAllPermissionAllow() {
                        startLibrary()
                    }
                })
            } else {
                if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                    startLibrary()
                } else requestPermission(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            viewModel!!.changFile(data.data?.let { getFilePathFromUri(this, it) }, null, type)
            Toast.makeText(this, data.data?.let { getFilePathFromUri(this, it) }, Toast.LENGTH_LONG)
                .show()
        } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            viewModel!!.changFile(data.data?.let { getFilePathFromUri(this, it) }, null, type)
        }
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (type == TypeMedia.IMAGE_TYPE) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor!!.getColumnIndex(filePathColumn[0])
            filePath = cursor!!.getString(columnIndex)
        } else if (type == TypeMedia.VIDEO_TYPE) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null);
            if (cursor != null) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
            }
        }
        return filePath
    }

    fun startLibrary() {
        if (type == TypeMedia.SOUND_TYPE) {
            val intent = Intent(this@MediaActivity, SelectAudioDeviceActivity::class.java)
            startActivity(intent)
        } else if (type == TypeMedia.IMAGE_TYPE) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_VIDEO_REQUEST)
        } else if (type == TypeMedia.VIDEO_TYPE) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_VIDEO_REQUEST)
        }
    }
}