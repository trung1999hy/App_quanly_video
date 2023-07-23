package com.example.valut.ui.ultis

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.example.valut.databinding.DialogNotificaBinding

object DialogUtils {
    @JvmStatic
     fun dialogNotification(
        context: Context?,
        title: String?,
        content: String?,
        itemClickDialogListener: ItemClickDialogListener
    ) {
        val binding = DialogNotificaBinding.inflate(LayoutInflater.from(context))
        binding.title.text = title
        binding.content.text = content
        val dialog = Dialog(context!!)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnCan.setOnClickListener { dialog.dismiss() }
        binding.btnOk.setOnClickListener {
            itemClickDialogListener.tvOk()
            dialog.dismiss()
        }
        binding.btnCan.setOnClickListener { view: View? ->
            itemClickDialogListener.tvCancel()
            dialog.dismiss()
        }
        dialog.show()
    }

    @JvmStatic
    fun dialogNotification(
        context: Context,
        icon: Int,
        title: String?,
        content: String?,
        itemClickDialogListener: ItemClickDialogListener
    ) {
        val binding = DialogNotificaBinding.inflate(LayoutInflater.from(context))
        binding.title.text = title
        binding.content.text = content
        if (icon != -1) {
            binding.imageView5.setImageDrawable(context.getDrawable(icon))
            binding.imageView5.visibility = View.VISIBLE
        } else {
            binding.imageView5.visibility = View.GONE
        }
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnCan.setOnClickListener { dialog.dismiss() }
        binding.btnOk.setOnClickListener {
            itemClickDialogListener.tvOk()
            dialog.dismiss()
        }
        binding.btnCan.setOnClickListener { view: View? ->
            itemClickDialogListener.tvCancel()
            dialog.dismiss()
        }
        dialog.show()
    }


}
interface ItemClickDialogListener {
    fun tvOk()
    fun tvCancel() {}
}