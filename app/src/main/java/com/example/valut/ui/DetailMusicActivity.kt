package com.example.valut.ui

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.ViewModelProvider
import com.example.valut.R
import com.example.valut.databinding.ActivityDetailMusicBinding
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant
import com.example.valut.ui.ultis.Constant.PERMISSIONS_EXTERNAL_STORAGE
import com.example.valut.ui.ultis.Constant.REQUEST_CODE_PERMISSION
import com.example.valut.ui.ultis.DialogUtils
import com.example.valut.ui.ultis.ItemClickDialogListener
import com.example.valut.ui.ultis.TimeUtils
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import java.util.Date

class DetailMusicActivity : PermissionActivity() {
    private lateinit var binding: ActivityDetailMusicBinding
    private var player: SimpleExoPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isFromUser = false
    private var mediaVault: MediaVault? = null
    private var mediaItem: MediaItem? = null
    private var viewModel: MediaVaultViewModel? = null
    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMusicBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        try {
            viewModel = ViewModelProvider(this).get(MediaVaultViewModel::class.java)
            mediaVault = getIntent().getSerializableExtra(Constant.KEY_MEDIA_VAULT) as MediaVault
            type = getIntent().getStringExtra(Constant.TYPE)
            binding.materialToolbar3.setNavigationOnClickListener { view -> finish() }
            player = SimpleExoPlayer.Builder(this).build()
            if (mediaVault != null) if (TextUtils.isEmpty(mediaVault?.newPath) || mediaVault?.newPath == null) mediaItem =
                mediaVault?.pathCurrent?.let { MediaItem.fromUri(it) } else {
                mediaItem = MediaItem.fromUri(mediaVault?.newPath!!)
            } else finish()
            if (!TextUtils.isEmpty(type) || type != null) {
                if (type == Constant.DEVICE) binding?.trash?.setVisibility(View.GONE)
            } else {
                val currentTimeInMillis = Date().time
                mediaVault?.timeOpen = currentTimeInMillis
                viewModel?.updateItemMediaVault(mediaVault)
            }
            binding?.titleName?.setText(mediaVault!!.name)
            if (!TextUtils.isEmpty(mediaVault!!.single) || mediaVault != null) binding.titleArtist.setText(
                mediaVault!!.single
            )
            player!!.setMediaItem(mediaItem!!)
            player!!.repeatMode = Player.REPEAT_MODE_ALL
            player!!.prepare()
            player!!.play()
            binding?.ivTogetherMedia?.setImageLevel(1)
            timePosition
            playbackController()
            setOnChangeProgressbar()
            setOnClickTrash()
        } catch (throwable: Throwable) {
//
        }
    }

    private val timePosition: Unit
        private get() {
            val runnable: Runnable = object : Runnable {
                override fun run() {
                    if (player != null) {
                        val currentPosition = player!!.currentPosition
                        val duration = player!!.duration
                        var progress = 0.0
                        if (currentPosition < duration) {
                            if (duration > 0) {
                                progress = currentPosition.toDouble() / duration
                            }
                            binding?.seekbarMedia?.setProgress((progress * 100).toInt())
                            binding?.tvProcessTime?.setText(
                                TimeUtils.convertMillisecondToTime(
                                    player!!.currentPosition
                                )
                            )
                            binding?.tvEndTime?.setText(TimeUtils.convertMillisecondToTime(player!!.duration))
                        }
                        handler.postDelayed({ this.run() }, 1000L)
                    }
                }
            }
            handler.post(runnable)
        }

    private fun playbackController() {
        binding?.ivTogetherMedia?.setOnClickListener(View.OnClickListener {
            if (player!!.isPlaying) {
                player!!.pause()
                binding.ivTogetherMedia.setImageLevel(0)
            } else {
                player!!.play()
                binding.ivTogetherMedia.setImageLevel(1)
            }
        })
    }

    private fun setOnChangeProgressbar() {
        binding.seekbarMedia.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                isFromUser = fromUser
                val msec = seekBar.progress
                val a = (msec * player!!.duration / 100).toDouble()
                binding.tvProcessTime.setText(TimeUtils.convertMillisecondToTime(a.toLong()))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (isFromUser) {
                    isFromUser = false
                    val msec = seekBar.progress
                    val a = (msec * player!!.duration / 100).toDouble()
                    player!!.seekTo(a.toLong())
                    binding.tvProcessTime.setText(TimeUtils.convertMillisecondToTime(a.toLong()))
                }
            }
        })
    }

    private fun setOnClickTrash() {
        binding.trash.setOnClickListener { view ->
            DialogUtils.dialogNotification(
                this@DetailMusicActivity, getString(R.string.title_delete_media),
                getString(R.string.content_remove_media),
                object : ItemClickDialogListener {
                    override fun tvOk() {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                            checkPermissionFileAll(object : CallBackPermission {
                                override fun onAllPermissionAllow() {
                                    mediaVault?.let { viewModel?.removeMediaVault(it) }
                                    finish()
                                }
                            })
                        } else {
                            if (checkPermissions(PERMISSIONS_EXTERNAL_STORAGE)) {
                                mediaVault?.let { viewModel?.removeMediaVault(it) }
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

    override fun onStop() {
        super.onStop()
        try {
            if (player != null) player!!.pause()
        } catch (throwable: Throwable) {
//
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (player != null) {
                player!!.release()
                player = null
            }
        } catch (throwable: Throwable) {
//
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode,
            PERMISSIONS_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION,
            R.string.title_memory_access_rights,
            R.string.title_permission_store, object : CallBackPermission {
                override fun onAllPermissionAllow() {
                    mediaVault?.let { viewModel?.removeMediaVault(it) }
                    finish()
                }
            })
    }
}