package me.reezy.cosmo.statelayout

interface StatePresenter {
    fun show(layout: StateLayout, state: Int)
}