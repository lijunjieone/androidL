package com.y.t

import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    println("Ch 03")
//    testSmart1()
//    testCut()
//    testPlate2()
//    testType2()
//    testSort4()
    testCopy()
}

fun testCopy() {
    val destDouble = arrayOf<Double>(3.0, 4.0, 5.0, 6.0)
    val srcDouble = arrayOf<Double>(1.0, 2.0, 3.0)
    println(Arrays.toString(destDouble))
    copy(destDouble, srcDouble)
    println(Arrays.toString(destDouble))

    val destDouble2 = arrayOfNulls<Number>(3)
//    val srcInt = arrayOf<Double>(12.0,3.0,4.0)
    val srcInt = arrayOf<Int>(12, 3, 4)
    println(Arrays.toString(destDouble2))
    copy2(destDouble2, srcInt)
    println(Arrays.toString(destDouble2))
}

fun <T> copy3(dest: Array<T>, src: Array<out T>) {
    if (dest.size < src.size) {
        throw IndexOutOfBoundsException()
    } else {
        src.forEachIndexed { index, t -> dest[index] = src[index] }
    }
}

fun <T> copy2(dest: Array<in T>, src: Array<T>) {
    if (dest.size < src.size) {
        throw IndexOutOfBoundsException()
    } else {
        src.forEachIndexed { index, t -> dest[index] = src[index] }
    }
}

fun <T> copy(dest: Array<T>, src: Array<T>) {
    if (dest.size < src.size) {
        throw IndexOutOfBoundsException()
    } else {
        src.forEachIndexed { index, t -> dest[index] = src[index] }
    }
}

fun testSort4() {
    val numberComparator = Comparator<Number> { n1, n2 ->
        n1.toDouble().compareTo(n2.toDouble())
    }

    val doubleList = mutableListOf(4.0, 3.0)
    println(doubleList)
    doubleList.sortWith(numberComparator)
    println(doubleList)

    val doubleList2 = mutableListOf(100, 50)
    println(doubleList2)
    doubleList2.sortWith(numberComparator)
    println(doubleList2)

}

fun testSort1() {
    val doubleComparator = Comparator<Double> { d1, d2 ->
        d1.compareTo(d2)
    }

    val doubleList = mutableListOf(4.0, 3.0)
    println(doubleList)
    doubleList.sortWith(doubleComparator)
    println(doubleList)

}

open class Plate2<T>(val t: T, val clazz: Class<T>) {
    fun getType(): String {
        return clazz.toString()
    }

}

fun testType2() {
    val list1 = ArrayList<String>()
    val list2 = object : ArrayList<String>() {}

    println(list1.javaClass.genericSuperclass)
    println(list2.javaClass.genericSuperclass)
}

fun testPlate2() {
    val apple = Plate2(Apple(1.0), Apple::class.java)
    println("type: ${apple.getType()}")
}

class Plate<T>(val t: T)
open class Fruit(val weight: Double)
class Apple(weight: Double) : Fruit(weight)
class Banana(weight: Double) : Fruit(weight)

class FruitPlate<T : Fruit>(val t: T)

class Noodles(weight: Double)

//泛型上届
val applePlate = FruitPlate<Apple>(Apple(100.0))

//泛型限制,所以不能给noodle
//val noodlesPlate = FruitPlate<Noodles>(Noodles(200))

interface Ground {}

class Watermelon(weight: Double) : Fruit(weight), Ground

fun <T> cut(t: T) where T : Fruit, T : Ground {
    println("You can cut me.")
}

fun testCut() {
    cut(Watermelon(100.0))
//    cut(Apple(100.0))
}

class SmartList<T> : ArrayList<T>() {
    fun find(t: T): T? {
        val index = super.indexOf(t)
        return if (index >= 0) super.get(index) else null
    }
}


fun testSmart1() {
    val smartList = SmartList<String>()
    smartList.add("one")

    println(smartList.find("one"))
    println(smartList.find("two"))
}
