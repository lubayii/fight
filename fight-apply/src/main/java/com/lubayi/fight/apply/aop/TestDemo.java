package com.lubayi.fight.apply.aop;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

/**
 * @author lubayi
 * @date 2025/11/17
 */
public class TestDemo {

    private static final String SECRET = "0okmnji98uhbvgy76tfcxdr54eszaq109";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Test
    public void getToken() {
        String token = Jwts.builder()
                .signWith(SECRET_KEY)
                .claim("client_id", "must")
                .compact();
        System.out.println(token);  // eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJtdXN0In0.Y_QS72HnqhcpNBPTuZIbopDSPmd2o-8uicyin1Fyiwk
    }

    /**
     * 测试 JWT 无法被篡改
     * eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiJtdXN0In0.Y_QS72HnqhcpNBPTuZIbopDSPmd2o-8uicyin1Fyiwk
     * 的 Payload 内容为：{"client_id": "must"}
     * 现在将 Payload 的内容改为：{"client_id": "maybe"}，第二段替换为 eyJjbGllbnRfaWQiOiAibWF5YmUifQ
     */
    @Test
    public void testChangeJWT() {
        // 报错：io.jsonwebtoken.security.SignatureException:
        // JWT signature does not match locally computed signature.
        // JWT validity cannot be asserted and should not be trusted.
        System.out.println(
                Jwts.parser().verifyWith(SECRET_KEY).build()
                        .parseSignedClaims("eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnRfaWQiOiAibWF5YmUifQ.Y_QS72HnqhcpNBPTuZIbopDSPmd2o-8uicyin1Fyiwk")
                        .getPayload()
        );
        /*
        如何防止 JWT 被篡改？
        有了签名之后，即使 JWT 被泄露或者截获，黑客也没办法同时篡改 Signature、Header、Payload。
        这是为什么呢？
        因为服务端拿到 JWT 之后，会解析出其中包含的 Header、Payload 以及 Signature 。
        服务端会根据 Header、Payload、密钥再次生成一个 Signature。
        拿新生成的 Signature 和 JWT 中的 Signature 作对比，如果一样就说明 Header 和 Payload 没有被修改。

        不过，如果服务端的秘钥也被泄露的话，黑客就可以同时篡改 Signature、Header、Payload 了。
        黑客直接修改了 Header 和 Payload 之后，再重新生成一个 Signature 就可以了。

        密钥一定保管好，一定不要泄露出去。JWT 安全的核心在于签名，签名安全的核心在密钥。
         */
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