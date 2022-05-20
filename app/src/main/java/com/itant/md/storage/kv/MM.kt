package com.itant.md.storage.kv

import android.text.TextUtils
import androidx.annotation.Keep
import com.tencent.mmkv.MMKV
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * MMKV封装
 * @param name key
 * @param default 默认值
 * @param preferenceName 配置文件名
 */
@Keep
class MM<T>(val name: String, private val default: T, private val mode: Int = MMKV.SINGLE_PROCESS_MODE, private val preferenceName:String? = null) : ReadWriteProperty<Any?, T> {

    private val mKeyValue: MMKV by lazy {
        if (TextUtils.isEmpty(preferenceName)) {
            MMKV.defaultMMKV()
        } else {
            MMKV.mmkvWithID(name, mode)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValue(name, value)
    }

    private fun putValue(name: String, value: T) = with(mKeyValue) {
        when (value) {
            is Long -> encode(name, value)
            is String -> encode(name, value)
            is Int -> encode(name, value)
            is Boolean -> encode(name, value)
            is Double -> encode(name, value)
            is Float -> encode(name, value)
            else -> encode(name, serialize(value))
        }
    }

    fun getValue(name: String, default: T): T = with(mKeyValue) {
        val res = when (default) {
            is Long -> decodeLong(name, default)
            is String -> decodeString(name, default)
            is Int -> decodeInt(name, default)
            is Boolean -> decodeBool(name, default)
            is Double -> decodeDouble(name, default)
            is Float -> decodeFloat(name, default)
            else -> {
                val stringValue: String? = getString(name, "")
                if (TextUtils.isEmpty(stringValue)) {
                    null
                } else {
                    try {
                        deSerialization<T>(stringValue!!)
                    } catch (e: Exception) {
                        default
                    }
                }
            }
        } ?: return@with default
        return try {
            res as T
        } catch (e: Exception) {
            default
        }
    }

    /**
     * 删除全部数据
     */
    fun clearPreference() {
        mKeyValue.edit().clear().commit()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        mKeyValue.edit().remove(key).commit()
    }

    /**
     * 序列化对象
     * @return
     */
    private fun <A> serialize(obj: A): String? {
        var serStr: String?

        ByteArrayOutputStream().use { byteArrayOutputStream ->
            ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
                objectOutputStream.writeObject(obj)
                serStr = byteArrayOutputStream.toString("ISO-8859-1")
                serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
            }
        }

        return serStr
    }

    /**
     * 反序列化对象
     * @param str
     */
    @Throws(Exception::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}