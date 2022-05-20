package com.itant.md.bean

import androidx.annotation.Keep
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

// todo 网络同步
//1，本地和服务器图标，用于指示当前是否同步到服务器了
//2，本地操作一次，就插入一个任务到本地数据库，网络变化，或定时，或resume，或用户全局的时候，去拉查询并执行
//3，以服务器为主，插入本地后，返回主界面才去服务器拉，并且对比服务器的版本号

/**
 * 笔记实体
 *
 * 同步逻辑（服务器版本为准）：先覆盖本地，再上传本地新建的到服务器
 * A登录了
 * B登录了
 * B离线，增加123
 * A增加456，同步到服务器
 * B联网，下拉，直接用服务器的456覆盖本地
 *
 * 登录账号之后，本地的自动同步上去
 *
 * @date 2022-5-9 22:04
 * @author 詹子聪
 */
@Keep
@DatabaseTable
class NoteBean {

    /**
     * 是否已选中
     */
    var isChecked = false

    /**
     * 日志创建时间，以这个为本地日志ID
     */
    @DatabaseField(id = true)
    var createTimeMillis = 0L

    /**
     * 上次更新时间戳
     */
    @DatabaseField
    var updateTimeMillis = 0L

    /**
     * 是否文件夹
     */
    @DatabaseField
    var folder = false

    /**
     * 标题（文件名）
     */
    @DatabaseField
    var noteTitle = ""

    /**
     * 密码
     */
    @DatabaseField
    var password = ""

    /**
     * 文本内容
     */
    @DatabaseField
    var content = ""

    /**
     * todo 本地文本是否有改动，传到服务器成功后，该字段要设置为false，从服务器拉取成功时，该字段也要设置为false
     */
    @DatabaseField
    var hasChanged = true

    /**
     * todo 版本号。保存到服务器成功之后，要+1并更新到本地，如果为0说明是未同步过的新文档
     * 从服务器同步时，如果服务器的版本号高，则直接覆盖本地内容
     */
    @DatabaseField
    var version = 0L

    /**
     * 是否已经和服务器同步
     */
    @DatabaseField
    var syncWithServer = false
}