package com.gunalirezqi.footballclub.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {
    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance = DatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as DatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            FavoriteEvent.TABLE_FAVORITE_EVENT, true,
            FavoriteEvent.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteEvent.EVENT_ID to TEXT + UNIQUE,
            FavoriteEvent.EVENT_DATE to TEXT,
            FavoriteEvent.EVENT_TIME to TEXT,
            FavoriteEvent.EVENT_NAME to TEXT,
            FavoriteEvent.EVENT_HOME_BADGE to TEXT,
            FavoriteEvent.EVENT_HOME_NAME to TEXT,
            FavoriteEvent.EVENT_HOME_SCORE to TEXT,
            FavoriteEvent.EVENT_AWAY_BADGE to TEXT,
            FavoriteEvent.EVENT_AWAY_NAME to TEXT,
            FavoriteEvent.EVENT_AWAY_SCORE to TEXT
        )

        db.createTable(
            FavoriteTeam.TABLE_FAVORITE_TEAM, true,
            FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
            FavoriteTeam.TEAM_BADGE to TEXT,
            FavoriteTeam.TEAM_NAME to TEXT,
            FavoriteTeam.TEAM_DESCRIPTION to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteEvent.TABLE_FAVORITE_EVENT, true)
        db.dropTable(FavoriteTeam.TABLE_FAVORITE_TEAM, true)
    }
}

// Access property for Context
val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)