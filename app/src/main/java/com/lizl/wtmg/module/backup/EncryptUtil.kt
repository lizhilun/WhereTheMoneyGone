package com.lizl.wtmg.module.backup

import android.util.Base64
import java.security.Key
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * 数据加密工具类
 */
object EncryptUtil {
    /**
     * 数据加密
     */
    fun encrypt(content: String, password: String): String {
        //1.创建cipher对象 学习查看api
        val cipher = Cipher.getInstance("DES")
        //2.初始化cirpher（参数1：加密/解密模式）
        val kf = SecretKeyFactory.getInstance("DES")
        val keySpe = DESKeySpec(getPasswordByte(password))
        val key: Key = kf.generateSecret(keySpe)
        //加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key)
        //3.加密/解密
        val encrypt = cipher.doFinal(content.toByteArray())
        //通过Base64解决乱码问题
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }

    /**
     * 数据解密
     */
    fun decrypt(content: String, password: String): String? {
        //1.创建cipher对象 学习查看api
        val cipher = Cipher.getInstance("DES")
        //2.初始化cirpher（参数1：加密/解密模式）
        val kf = SecretKeyFactory.getInstance("DES")
        val keySpe = DESKeySpec(getPasswordByte(password))
        val key: Key = kf.generateSecret(keySpe)
        //解密模式
        cipher.init(Cipher.DECRYPT_MODE, key)
        //base64解码
        val encrypt: ByteArray?
        return try {
            encrypt = cipher.doFinal(Base64.decode(content, Base64.DEFAULT))
            String(encrypt)
        } catch (e: BadPaddingException) {
            null
        }
    }

    /**
     * 获取16位密码（不足16位的后面补0，超过16位的取前16位）的byte数据
     */
    private fun getPasswordByte(password: String): ByteArray {
        val passwordSize = password.length
        var result = ""
        if (passwordSize > 16) {
            result = password.substring(0, 16)
        } else {
            result += password
            for (i in passwordSize until 16) {
                result += "0"
            }
        }

        return result.toByteArray()
    }
}