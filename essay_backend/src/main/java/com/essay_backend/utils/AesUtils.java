package com.essay_backend.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class AesUtils {
    /**
     * 偏移量，AES 128位数据块对应偏移量为16位
     * AES 128位数据块对应偏移量为16位
     */
    private static final String VIPARA = "j87y5ndh4sbg678h";

    /**
     * 密匙，必须16位
     */
    private static final String KEY = "dasg67gdhn9a867a";
    /**
     * 编码
     */
    private static final String ENCODING = "UTF-8";
    /**
     * 算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * AES：加密方式   CBC：工作模式   PKCS5Padding：填充模式
     */
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    /**
     * AES加密
     *
     * @param data
     * @return String
     */
    public static String AESencrypt(String data) {
        if (data == null || StringUtils.isEmpty(data)) {
            return data;
        }
        /*
         * 新建一个密码编译器的实例，由三部分构成，用"/"分隔，分别代表如下
         * 1. 加密的类型(如AES，DES，RC2等)
         * 2. 模式(AES中包含ECB，CBC，CFB，CTR，CTS等)
         * 3. 补码方式(包含nopadding/PKCS5Padding等等)
         * 依据这三个参数可以创建很多种加密方式
         */
        try {
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            //偏移量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(VIPARA.getBytes(ENCODING));
            //加密秘钥
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(ENCODING), ALGORITHM);
            //初始化密码编译器
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            //生成加密后的密文
            byte[] bytes = cipher.doFinal(data.getBytes(ENCODING));
            //将密文Base64转码返回
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            log.error("参数加密失败--{}", e);
            return null;
        }
    }

    public static String AesDecrypt(String data) {
        if (data == null || StringUtils.isEmpty(data)) {
            return data;
        }
        try {
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            //偏移量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(VIPARA.getBytes(ENCODING));
            //加密秘钥
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(ENCODING), ALGORITHM);
            //初始化密码编译器
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(data));
            return new String(bytes, ENCODING);
        } catch (Exception e) {
            log.error("参数解密失败--{}", e);
            return data;
        }
    }
}
