package com.pratyaksh.healthykingdom.utils

fun String.isValidPassword(): Boolean {

    val alphaPattern = Regex("[a-zA-Z]+")
    val numPattern = Regex("[0-9]+")
    val symbolPattern = Regex("""[&|â€œ|`|Â´|}|{|Â°|>|<|:|.|;|#|'|)|(|@|_|$|"|!|?|*|=|^|-]+""")

    return this.contains(alphaPattern) && this.contains(numPattern) && this.contains(symbolPattern)
}

fun String.isValidName(): Boolean{
    val alphaPattern = Regex("[a-zA-Z]+")
    return this.contains(alphaPattern)
}

fun String.isValidMail(): Boolean{
    val mailPattern = Regex("^[a-zA-Z0-9+_.-]+@[A-Za-z0=9.-]+\$")
    return this.contains(mailPattern)
}