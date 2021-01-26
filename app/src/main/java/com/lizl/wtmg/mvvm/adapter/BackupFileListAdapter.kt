package com.lizl.wtmg.mvvm.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.module.backup.FileUtil
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : BaseQuickAdapter<File, BackupFileListAdapter.ViewHolder>(R.layout.item_backup_file)
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    private var onFileItemClickListener: ((File) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: File)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(file: File)
        {
            itemView.tv_file_name.text = file.name
            itemView.tv_file_size.text = FileUtil.getFileSize(file)
            itemView.tv_file_time.text = formatter.format(file.lastModified())

            itemView.setOnClickListener { onFileItemClickListener?.invoke(file) }
        }
    }

    fun setOnFileItemClickListener(onFileItemClickListener: (File) -> Unit)
    {
        this.onFileItemClickListener = onFileItemClickListener
    }

    fun update(file: File)
    {
        setData(getItemPosition(file), file)
    }
}