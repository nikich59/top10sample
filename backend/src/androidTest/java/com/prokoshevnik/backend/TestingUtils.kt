package com.prokoshevnik.backend

import kotlin.random.Random

object TestingUtils {
    const val SPECIAL_SYMBOLS = " _-+*/=!@#$%^&*()\'\";:.,?№|\\<>"

    fun getRandomDbString(random: Random) =
        String(random.nextBytes(8)) + SPECIAL_SYMBOLS
}

