package vip.example.plank

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TestOpenHelper internal constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(
            SQL_CREATE_ENTRIES
        )

        Log.d("debug", "onCreate(SQLiteDatabase db)")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // アップデートの判別
        db.execSQL(
            SQL_DELETE_ENTRIES
        )
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {

        // データーベースのバージョン
        private val DATABASE_VERSION = 2

        // データーベース名
        private val DATABASE_NAME = "TestDB.db"
        private val TABLE_NAME = "testdb"
        private val _ID = "_id"
        private val COLUMN_NAME_TITLE = "date"
        private val COLUMN_NAME_SUBTITLE = "score"
        private val COLUMN_NAME_SUBTITLE2 = "level"
        private val COLUMN_NAME_SUBTITLE3 = "sec"

        private val SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_TITLE + " TEXT," +
                COLUMN_NAME_SUBTITLE + " TEXT," +
                COLUMN_NAME_SUBTITLE2 + " TEXT," +
                COLUMN_NAME_SUBTITLE3 + " INTEGER)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

}
