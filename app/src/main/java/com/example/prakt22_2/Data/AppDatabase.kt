package com.example.prakt22_2.Data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.example.prakt22_2.Converter.Converter

@Database(entities = [DataEntity::class], version = 2)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)  // Добавьте миграцию
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Добавляем миграцию с версии 1 на версию 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Пример миграции — добавление нового столбца, если необходимо
                database.execSQL("ALTER TABLE DataEntity ADD COLUMN newColumn TEXT")
            }
        }
    }
}

