/** *
 * aes加密 解密
 * param { String } word 加密或解密的值
 * param { String } keyStr 秘钥key
 * return { String} 返回值
 *
 */
import CryptoES from 'crypto-es'

const keyStr = 'dasg67gdhn9a867a'


const finalKey = CryptoES.enc.Utf8.parse('dasg67gdhn9a867a')

const finalIv = CryptoES.enc.Utf8.parse('j87y5ndh4sbg678h')

export function encrypt(data: string) {
    const key = finalKey
    const iv = finalIv
    const srcs = CryptoES.enc.Utf8.parse(data);
    const encrypted = CryptoES.AES.encrypt(srcs, key, {
        iv: iv,
        mode: CryptoES.mode.CBC,
        padding: CryptoES.pad.Pkcs7
    });
    return CryptoES.enc.Base64.stringify(encrypted.ciphertext);
}


// 加密
// export function encrypt(word: string) {
//     const key = CryptoES.enc.Utf8.parse(keyStr)
//     const srcs = CryptoES.enc.Utf8.parse(word)
//     const encrypted = CryptoES.AES.encrypt(srcs, key, {mode: CryptoES.mode.ECB, padding: CryptoES.pad.Pkcs7})
//     return encrypted.toString()
// }

// 解密
export function decrypt(word: string) {
    const key = CryptoES.enc.Utf8.parse(keyStr)
    const decryptStr = CryptoES.AES.decrypt(word, key, {mode: CryptoES.mode.ECB, padding: CryptoES.pad.Pkcs7})
    return CryptoES.enc.Utf8.stringify(decryptStr).toString()
}

export default {encrypt, decrypt}
