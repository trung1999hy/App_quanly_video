package com.example.valut.ui

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.example.valut.R
import com.example.valut.databinding.ActivityDetailVideoBinding
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.ultis.Constant
import com.example.valut.ui.ultis.DialogUtils
import com.example.valut.ui.ultis.ItemClickDialogListener
import com.example.valut.ui.ultis.TimeUtils
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import java.util.Date

class DetailVideoActivity : PermissionActivity() {
    private var binding: ActivityDetailVideoBinding? = null
    private var player: SimpleExoPlayer? = null
    private var mediaVault: MediaVault? = null
    private var mediaItem: MediaItem? = null
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var isFromUser = false
    private var viewModel: MediaVaultViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVideoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        try {
            viewModel =
                ViewModelProvider(this).get(MediaVaultViewModel::class.java)
            binding!!.materialToolbar3.setNavigationOnClickListener { view: View? -> finish() }
            mediaVault = intent.getSerializableExtra(Constant.KEY_MEDIA_VAULT) as MediaVault?
            player = SimpleExoPlayer.Builder(this).build()
            if (mediaVault != null) mediaItem =
                mediaVault!!.newPath?.let { MediaItem.fromUri(it) } else finish()
            val currentTimeInMillis = Date().time
            mediaVault?.timeOpen = currentTimeInMillis
            viewModel!!.updateItemMediaVault(mediaVault)
            mediaItem?.let { player?.setMediaItem(it) }
            player?.setRepeatMode(Player.REPEAT_MODE_ALL)
            binding!!.materialToolbar3.setTitle(mediaVault?.name ?: "")
            binding!!.playerView.player = player
            binding!!.playerView.controllerAutoShow = false
            player?.prepare()
            player?.setPlayWhenReady(true)
            binding!!.playbackController.ivTogetherMedia.setImageLevel(1)
            timePosition()
            playbackController()
            setOnChangeProgressbar()
            setOnClickTrash()
        } catch (throwable: Throwable) {
//
        }
    }

    private fun timePosition(){
            val runnable: Runnable = object : Runnable {
                override fun run() {
                    if (player != null) {
                        val currentPosition: Long = player!!.getCurrentPosition()
                        val duration: Long = player!!.getDuration()
                        var progress = 0.0
                        if (currentPosition < duration) {
                            if (duration > 0) {
                                progress = currentPosition.toDouble() / duration
                            }
                            binding!!.playbackController.exoProgress.setProgress((progress * 100).toInt())
                            binding!!.playbackController.exoPosition.setText(
                                TimeUtils.convertMillisecondToTime(
                                    player!!.getCurrentPosition()
                                )
                            )
                            binding!!.playbackController.exoDuration.setText(
                                TimeUtils.convertMillisecondToTime(
                                    player!!.getDuration()
                                )
                            )
                        }
                        handler.postDelayed({ this.run() }, 1000L)
                    }
                }
            }
            handler.post(runnable)
        }

    private fun playbackController() {
        binding!!.playbackController.ivTogetherMedia.setOnClickListener(View.OnClickListener {
            if (player?.isPlaying() == true) {
                player!!.pause()
                binding!!.playbackController.ivTogetherMedia.setImageLevel(0)
            } else {
                player?.play()
                binding!!.playbackController.ivTogetherMedia.setImageLevel(1)
            }
        })
    }

    private fun setOnChangeProgressbar() {
        binding!!.playbackController.exoProgress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                isFromUser = fromUser
                val msec: Int = seekBar.getProgress()
                val a: Double = (msec * player?.getDuration()!! / 100).toDouble()
                binding!!.playbackController.exoPosition.setText(
                    TimeUtils.convertMillisecondToTime(
                        a.toLong()
                    )
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (isFromUser) {
                    isFromUser = false
                    val msec: Int = seekBar.getProgress()
                    val a: Double = (msec * player?.getDuration()!! / 100).toDouble()
                    player?.seekTo(a.toLong())
                    binding!!.playbackController.exoDuration.setText(
                        TimeUtils.convertMillisecondToTime(
                            a.toLong()
                        )
                    )
                }
            }
        })
    }

    private fun setOnClickTrash() {
        binding!!.trash.setOnClickListener { view: View? ->
            DialogUtils.dialogNotification(this@DetailVideoActivity,
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
                            if (checkPermissions(Constant.PERMISSIONS_EXTERNAL_STORAGE)) {
                                mediaVault?.let { viewModel!!.removeMediaVault(it) }
                                finish()
                            } else requestPermission(
                                Constant.PERMISSIONS_EXTERNAL_STORAGE,
                                Constant.REQUEST_CODE_PERMISSION
                            )
                        }
                    }
                })
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            player?.pause()
        } catch (throwable: Throwable) {
//
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {

            player?.release()
            player = null
        } catch (throwable: Throwable) {
//
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode,
            Constant.PERMISSIONS_EXTERNAL_STORAGE, Constant.REQUEST_CODE_PERMISSION,
            R.string.title_memory_access_rights,
            R.string.title_permission_store, object : CallBackPermission {
                override fun onAllPermissionAllow() {
                    mediaVault?.let { viewModel!!.removeMediaVault(it) }
                    finish()
                }
            })
    }
}