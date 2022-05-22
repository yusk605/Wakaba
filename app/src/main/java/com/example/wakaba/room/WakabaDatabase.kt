package com.example.wakaba.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Wakaba::class],version = 1)
abstract    class WakabaDatabase :RoomDatabase(){
    abstract fun getDao():WakabaDao

    companion object{
        private var INSTANCE:WakabaDatabase?=null
        fun getDatabase(context: Context,scope: CoroutineScope):WakabaDatabase{
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    WakabaDatabase::class.java,
                    "appDatabase")
                    .fallbackToDestructiveMigration()
                    //.addCallback(CallbackDatabase(scope))
                    .build()
                INSTANCE=instance
                instance
            }
        }
       private class   CallbackDatabase(val scope: CoroutineScope): RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                scope.launch(Dispatchers.IO) {
                    INSTANCE?.let { it.getDao().populateDatabase() }
                }
            }
        }
        private fun  WakabaDao.populateDatabase(){
            (0..10).forEach { _ ->
                this.insert(
                    Wakaba(
                        wakabaNo = 0,
                        wakabaTitle ="ダミー",
                        wakabaContent = "内容",
                        wakabaRating = 5,
                        wakabaDate = "2020/09/23",
                        checkFlag = 0,
                        timeStamp = "2020/01/01"
                ))
            }
        }
    }
}