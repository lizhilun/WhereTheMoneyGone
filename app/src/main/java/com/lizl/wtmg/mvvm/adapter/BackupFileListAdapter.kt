package com.lizl.wtmg.mvvm.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.ItemBackupFileBinding
import com.lizl.wtmg.module.backup.FileUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : BaseQuickAdapter<File, BaseViewHolder>(R.layout.item_backup_file)
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemBackupFileBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: File)
    {
        helper.getBinding<ItemBackupFileBinding>()?.apply {
            tvFileName.text = item.name
            tvFileSize.text = FileUtil.getFileSize(item)
            tvFileTime.text = formatter.format(item.lastModified())
        }
    }
}