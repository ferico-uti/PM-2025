package com.uti.sqlite.config

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.uti.sqlite.config.Constant.Companion.DB_NAME
import com.uti.sqlite.config.Constant.Companion.DB_VERSION

class Lite(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
//        buat query tabel mahasiswa
        val tb_mahasiswa =
            "CREATE TABLE tb_mahasiswa (id INTEGER PRIMARY KEY AUTOINCREMENT, npm CHAR (8), nama VARCHAR (100), jurusan CHAR (2))"
//        eksekusi query
        db?.execSQL(tb_mahasiswa)

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
//        jika ada versi terbaru ( 1 > 2)
        if (newVersion > oldVersion) {
//            jika varsi db = 2
            if (newVersion == 2) {
                val insert_data = "INSERT INTO tb_mahasiswa VALUES(2,'23567898','Iwan','SI')"
                db?.execSQL(insert_data)
            }
//            jika versi db = 3
            if (newVersion == 3) {
                val delete_data = "DELETE FROM tb_mahasiswa WHERE id = 2"
                db?.execSQL(delete_data)
            }

        }
    }
}