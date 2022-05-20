package com.itant.md.manager

import com.itant.md.R
import com.itant.md.bean.NoteBean
import com.itant.md.storage.db.DbManager
import com.j256.ormlite.table.TableUtils
import com.miekir.common.context.GlobalContext
import com.miekir.common.log.L

/**
 * 日志管理者
 */
object NoteManager {
    private const val TAG = "NoteManager"

    /**
     * 消息实体-数据库
     */
    private val noteDao = DbManager.ormConfig.getDao(NoteBean::class.java)

    /**
     * 新增或者更新
     */
    @Synchronized
    fun createOrUpdateLocal(noteBean: NoteBean): Boolean {
        noteBean.updateTimeMillis = System.currentTimeMillis()
        return try {
            noteDao.createOrUpdate(noteBean)
            true
        } catch (e: Exception) {
            L.e(TAG, "${GlobalContext.getContext().getString(R.string.update_failed)}${e.message}")
            false
        }
    }

    /**
     * 删除
     */
    @Synchronized
    fun deleteLocal(noteBean: NoteBean) {
        try {
            noteDao.delete(noteBean)
        } catch (e: Exception) {
            L.e(TAG, "${GlobalContext.getContext().getString(R.string.del_failed)}${e.message}")
        }
    }

    /**
     * todo 删除所有本地日志，用户退出登录后删除所有的本地日志
     */
    @Synchronized
    fun deleteAllLocalNote() {
        try {
            TableUtils.dropTable<NoteBean, Any>(DbManager.ormConfig.connectionSource, NoteBean::class.java, true)
            TableUtils.createTableIfNotExists(DbManager.ormConfig.connectionSource, NoteBean::class.java)
        } catch (e: Exception) {
            L.e(TAG, "${GlobalContext.getContext().getString(R.string.update_failed)}${e.message}")
        }
    }

    /**
     * 获取数据库所有本地笔记，按修改时间倒序
     */
    fun getLocalList(): List<NoteBean> {
        val accountList: MutableList<NoteBean> = mutableListOf()
        try {
            val list = noteDao.queryForAll()
            if (list != null) {
                accountList.addAll(list)
            }
        } catch (e: Exception) {
            L.e(TAG, "${GlobalContext.getContext().getString(R.string.get_failed)}${e.message}")
        }

        accountList.sortByDescending { it.updateTimeMillis }
        return accountList
    }
}