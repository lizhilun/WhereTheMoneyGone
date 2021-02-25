package com.lizl.wtmg.mvvm.model.polymerize

data class PolymerizeGroupModel(val name: String, val info: String, val childList: MutableList<PolymerizeChildModel>) : PolymerizeModel()