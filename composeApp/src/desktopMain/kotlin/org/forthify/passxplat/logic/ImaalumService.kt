package org.forthify.passxplat.logic

import java.io.File


class DesktopFileSave : FileSave{
    override fun SaveToFile(fileName: String, data: ByteArray) {
        File(fileName).writeBytes(data)

    }

    override fun SaveToFile(fileName: String, data: ByteArray, onComplete: (Boolean) -> Unit) {
        return
    }

}