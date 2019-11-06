package com.y.t

fun main(args:Array<String>) {
    println("Ch 02")
//    xA("lijunjie",::xB)
//    xBook2()
//    xA("lijunjie",{ "$it $it $it"})

//    xA2("lijunjie",fun(x1:String,x2:String):String{
//        return "$x1,$x2"
//    })

//    testPrintFoo()
//    testRevoke()
//    testCloseB()
//    testCloseC()
//    println("testCloseD")
//    testCloseD()

//    testCorresponds()

//    testWhen2()
//    testInfix()
//    testVararg()
    testInfix1()
}

//fun <T> sum(a:T,b:T) {
//   return a+b
//}
class Person2 {
    infix fun called(name:String) {
        println("My name is ${name}")
    }
}

fun testInfix1() {
    val p = Person2()
    p called "lijunjie"
}
fun printLetters(vararg letters:String,count:Int) {
    println("${count} letters are")

    for(letter in letters) {
        println("letter ${letter}")
    }
}

fun testVararg() {
    val letters = arrayOf("a","b","c")
    printLetters(*letters,count = 3)
}
infix fun String.plus1(that:String):String {
    return "${this} p ${that}"
}

fun testInfix() {
    val x = "Wind"
    val b = x plus1 "Image"
    println("$b")
}

fun testWhen3() {
    fun schedule(sunny:Boolean,day:DayOfWeek) = when {
        day == Day.SAT -> println("basketball")
        day == Day.SUN -> println("fishing")
        day == Day.FRI -> println("appointment")
        sunny -> println("library")
        else -> println("study")
    }

    val done = schedule(true,DayOfWeek.FRI)
    schedule(true,DayOfWeek.MON)
    println("Mon is ${DayOfWeek.MON.day}")
}

fun testWhen2() {
    fun schedule(sunny:Boolean,day:DayOfWeek) = when(day) {
        Day.SAT -> println("basketball")
        Day.SUN -> println("fishing")
        Day.FRI -> println("appointment")
        else -> when{
            sunny -> println("library")
            else -> println("study")
        }
    }

    val done = schedule(true,DayOfWeek.FRI)
    schedule(true,DayOfWeek.MON)
    println("Mon is ${DayOfWeek.MON.day}")
}
fun testWhen1() {
    fun schedule(sunny:Boolean,day:Day) = when(day) {
        Day.SAT -> println("basketball")
        Day.SUN -> println("fishing")
        Day.FRI -> println("appointment")
        else -> when{
            sunny -> println("library")
            else -> println("study")
        }
    }

    val done = schedule(true,Day.SAT)
    schedule(true,Day.MON)

}

enum class  Day {
    MON,
    TUE,
    WEN,
    THU,
    FRI,
    SAT,
    SUN
}

enum class DayOfWeek(val day:Int) {
    MON(1),
    TUE(2),
    WEN(3),
    THU(4),
    FRI(5),
    SAT(6),
    SUN(7)
    ;

    fun getDayNumber() = { day }
}
fun <A,B> Array<A>.corresponds(that:Array<B>,p:(A,B)->Boolean):Boolean {
    val i = this.iterator()
    val j = that.iterator()

    while(i.hasNext() && j.hasNext()) {
        if(!p(i.next(),j.next())) {
            return false
        }
    }

    return !i.hasNext() && !j.hasNext()
}

fun testCorresponds() {
    val a=arrayOf(1,2,3)
    val b = arrayOf(2,3,4)
    val c = a.corresponds(b) {
        x,y -> x+1 == y
    }

    println("c:$c")
}


fun printFoo(i:Int): () -> Unit = {
    println(i)
}

fun testPrintFoo() {
    listOf(1,2,3).forEach { printFoo(it).invoke() }
}

fun testRevoke() {
    val foo = {x:Int,y:Int -> x+y }
    println(foo(2,3))
    println(foo.invoke(2,3))

    val foo3 = {x:Int,y:Int,z:Int -> x+y+z}

    println(foo3.invoke(3,4,5))
}

fun xA2(name:String,done:(String,String)->String) {
    println(done(name,"it's $name"))
}
fun xB(x:String):String {
    return "$x $x"
}
fun xA(name:String,done:(String)->String) {
    println(done(name))
}

fun testCloseA() {
    var sum = 0
    listOf(1,2,3).filter { it>1 }.forEach {
        sum+=it
    }
    println("sum:$sum")

}

fun testCloseB() {
    { x:Int -> println("x:$x") }(3)
}

fun testCloseC() {
    val x = {x:Int,y:Int,z:Int -> x+y+z }
    println("sum:${x(1,2,3)}")
    println("sum2:${x.invoke(4,5,6)}")
}

fun testCloseD() {
    val x1 = {x:Int -> { y:Int -> { z:Int -> x+y+z }}}
    println("sum:${x1(1)(2)(3)}")
    println("sum:${x1.invoke(4).invoke(5).invoke(6)}")
}

class Book(val name:String)

fun xBook() {
    val getBook = ::Book
    println(getBook("Dive into kotlin").name)
}


fun xBook2() {
    val bookNames = listOf(
            Book("Thinking in Java"),
            Book("Dive into Kotlin")
    ).map{
        "the book name is ${it.name}"
    }

    println(bookNames)
}

fun xBook3() {
    val bookNames = listOf(
            Book("Thinking in Java"),
            Book("Dive into Kotlin")
    ).map(Book::name)

    println(bookNames)
}


