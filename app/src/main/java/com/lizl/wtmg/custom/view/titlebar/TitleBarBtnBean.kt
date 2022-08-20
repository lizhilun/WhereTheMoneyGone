package com.lizl.wtmg.custom.view.titlebar

open class TitleBarBtnBean {
    open class BaseBtnBean

    class ImageBtnBean(var imageRedId: Int, val onBtnClickListener: () -> Unit) : BaseBtnBean()

    class TextBtnBean(var text: String, val onBtnClickListener: () -> Unit) : BaseBtnBean()
}