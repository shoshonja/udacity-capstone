package com.bikes.greyp.udacitycapstoneproject.utils

import org.bouncycastle.util.encoders.Hex
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class Crypto {
    companion object {
        fun getSHA256hash(input: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(
                input.toByteArray(StandardCharsets.UTF_8)
            )
            return String(Hex.encode(hash))
        }
    }
}