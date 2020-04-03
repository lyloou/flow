package com.lyloou.flow

fun main() {
    val cats = Herd(mutableListOf(Cat()))
    takeCareOfCats(cats)
}

open class Animal {
    fun feed() {
        println("fee 3from animal")
    }
}

class Herd<T : Animal>(val list: MutableList<T>) {
    val size: Int get() = list.size
    operator fun get(i: Int): T {
        return list[i]
    }
}

fun feedAll(animals: Herd<out Animal>) {
    for (i in 0 until animals.size) {
        println(animals[i])
        animals[i].feed()
    }
}

class Cat : Animal() {
    fun cleanLitter() {
        println("cleanLitter from Cat")
    }
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
//        cats[i].feed()
        feedAll(cats)
    }
}