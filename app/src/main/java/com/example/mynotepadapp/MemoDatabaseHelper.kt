package com.example.mynotepadapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MemoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "memoDatabase.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // 테이블 생성
        db?.execSQL(
            """
            CREATE TABLE memos (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                title TEXT, 
                content TEXT, 
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS memos")
        onCreate(db)
    }

    // 메모 추가
    fun insertMemo(title: String, content: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("content", content)
        }
        return db.insert("memos", null, values)
    }

    // 메모 수정
    fun updateMemo(id: Long, title: String, content: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("content", content)
        }
        return db.update("memos", values, "id = ?", arrayOf(id.toString()))
    }

    // 메모 삭제
    fun deleteMemo(id: Long): Int {
        val db = writableDatabase
        return db.delete("memos", "id = ?", arrayOf(id.toString()))
    }

    // 모든 메모 조회
    fun getAllMemos(): List<Memo> {
        val db = readableDatabase
        val cursor = db.query("memos", null, null, null, null, null, "timestamp DESC")
        val memos = mutableListOf<Memo>()

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow("id"))
                val title = getString(getColumnIndexOrThrow("title"))
                val content = getString(getColumnIndexOrThrow("content"))
                val timestamp = getString(getColumnIndexOrThrow("timestamp"))
                memos.add(Memo(id, title, content, timestamp))
            }
        }
        cursor.close()
        return memos
    }
}
