package org.forthify.passxplat.logic

interface IPermissionHandler {
    fun requestPermission(permission: String, onResult: (Boolean) -> Unit)
    fun hasPermission(permission: String): Boolean
}