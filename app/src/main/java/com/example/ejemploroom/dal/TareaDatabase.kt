package com.example.ejemploroom.dal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TareaEntity::class], version = 1, exportSchema = true)
abstract class TareasDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao

}