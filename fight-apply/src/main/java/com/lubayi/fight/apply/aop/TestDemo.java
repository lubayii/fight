package com.lubayi.fight.apply.aop;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;

/**
 * @author lubayi
 * @date 2025/11/17
 */
public class TestDemo {

    @Test
    public void getToken() {
        String token = JWT.create()
                .withClaim("client_id", "must")
                .sign(Algorithm.HMAC256("0okmnji98uhbvgy7"));
        System.out.println(token);  // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnRfaWQiOiJtdXN0In0.lA1ZE3TPOh4xh4Oqx1jrlJVTo2yrcypu3cbQ7Z0Wpfk
    }

}
/*
jwt.io
JWT（JSON Web Token）

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnRfaWQiOiJtdXN0In0.lA1ZE3TPOh4xh4Oqx1jrlJVTo2yrcypu3cbQ7Z0Wpfk
是经过加密之后的密文字符串，中间通过 . 来分割。分别表示JWT的三个组成部分：Header、Payload、Signature

Header 和 Payload 都是 JSON 格式的数据，Signature 由 Payload、Header 和 Secret(密钥)通过特定的计算公式和加密算法得到。

Header（头部） : JSON 形式的 Header 被转换成 Base64 编码，成为 JWT 的第一部分
通常由两部分组成：
    typ：Type，令牌类型
    alg：Algorithm，签名算法
{
  "alg": "HS256",
  "typ": "JWT"
}


Payload（载荷） : 也是 JSON 格式数据，包含了 Claims(声明，包含 JWT 的相关信息)。
用来存放实际需要传递的数据，被转换成 Base64 编码，成为 JWT 的第二部分。


Signature（签名）：为了防止 JWT（主要是 payload） 被篡改，
服务器通过 Payload、Header 和一个密钥(Secret)使用 Header 里面指定的签名算法（默认是 HMAC SHA256）生成，
生成的签名会成为 JWT 的第三部分。

签名的生成需要用到：1、Header + Payload；2、存放在服务端的密钥；3、签名算法
签名的计算公式如下：HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)


算出签名以后，把 Header、Payload、Signature 三个部分拼成一个字符串，每个部分之间用"点"（.）分隔，这个字符串就是 JWT 。

 */