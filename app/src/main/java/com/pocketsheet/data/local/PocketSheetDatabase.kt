package com.pocketsheet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pocketsheet.data.local.entity.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 2, exportSchema = false)
abstract class PocketSheetDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: PocketSheetDatabase? = null

        fun getInstance(context: Context): PocketSheetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PocketSheetDatabase::class.java,
                    "pocket_sheet_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
