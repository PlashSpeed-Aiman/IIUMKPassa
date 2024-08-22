package org.forthify.passxplat

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.forthify.passxplat.sql.Credentials
import org.forthify.passxplat.sql.IDatabaseDriverFactory

class DatabaseDriverFactory(private val context: Context):IDatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Credentials.Schema, context, "credentials.db")
    }
}