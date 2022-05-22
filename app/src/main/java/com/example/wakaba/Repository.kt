package com.example.wakaba

import com.example.wakaba.room.Wakaba
import com.example.wakaba.room.WakabaDao

class Repository(private val dao: WakabaDao) {
    fun insert(bean:Wakaba)=dao.insert(bean)
    fun delete(bean:Wakaba)=dao.delete(bean)
    fun update(bean: Wakaba)=dao.update(bean)
}