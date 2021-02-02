package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.module.backup.FileUtil
import kotlinx.android.synthetic.main.item_backup_file.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BackupFileListAdapter : BaseQuickAdapter<File, BaseViewHolder>(R.layout.item_backup_file)
{
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun convert(helper: BaseViewHolder, item: File)
    {
        with(helper.itemView) {
            tv_file_name.text = item.name
            tv_file_size.text = FileUtil.getFileSize(item)
            tv_file_time.text = formatter.format(item.lastModified())
        }
    }
}