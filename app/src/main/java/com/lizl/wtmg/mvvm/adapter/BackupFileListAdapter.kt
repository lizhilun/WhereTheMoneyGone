package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.ItemBackupFileBinding
import com.lizl.wtmg.module.backup.FileUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : BaseDBAdapter<File, BaseViewHolder, ItemBackupFileBinding>(R.layout.item_backup_file) {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun bindViewHolder(dataBinding: ItemBackupFileBinding, item: File) {
        with(dataBinding) {
            tvFileName.text = item.name
            tvFileSize.text = FileUtil.getFileSize(item)
            tvFileTime.text = formatter.format(item.lastModified())
        }
    }
}