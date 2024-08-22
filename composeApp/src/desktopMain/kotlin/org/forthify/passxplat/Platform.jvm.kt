package org.forthify.passxplat
import org.forthify.passxplat.logic.CredentialStorage
import org.forthify.passxplat.logic.DesktopCredentialsImpl
import org.forthify.passxplat.logic.DesktopFileSave
import org.forthify.passxplat.logic.FileSave
import org.koin.core.module.Module
import org.koin.dsl.module

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun platformModule():Module{
    return module {
        single<FileSave> {
            DesktopFileSave()
        }
        single<CredentialStorage>{
            DesktopCredentialsImpl()
        }
    }
}
