package com.y.t



fun main(args:Array<String>) {
    println("my name is $name")
//    println("this is ${nullStr?:0}")
//    printLength(nullStr!!)
//    echo("test")
//    Test2.sayMessage2("Test.test")
//    function()
//    Dog().printName(Dog())
//    Dog().printName(Animal())
//    testThread()
//    val a = testThread()


//    testInvoke()

//    testOnlyIf()

    //函数的变量的执行
//    functionVar()

    // zoo 由dog2来完成
//    Zoo(Dog2()).bark()
//    testEnum(Command.B)

//    testSplit()

//    testFor()


//    testList()

//    testList2()


    testSort3()
}

data class Person(var name:String ,var age:Int)

fun initPersonList():MutableList<Person> {
    var personList:MutableList<Person> = mutableListOf()
    personList.add(Person("Jim",12))
    personList.add(Person("A-Lin",12))
    personList.add(Person("Tom",11))
    personList.add(Person("Mary",14))
    return personList
}

fun testSort() {
    val personList = initPersonList()
    println("before")
    personList.forEach(::println)

    println("after")
    personList.sortByDescending { it.age }

    personList.forEach(::println)

}

fun testSort2() {
    val personList = initPersonList()
    println("before")
    personList.forEach(::println)

    println("after")
    personList.sortWith(compareBy({it.age},{it.name}))
    personList.forEach(::println)

}

fun testSort3() {
    val c1:Comparator<Person> = Comparator{o1,o2 ->
        if(o2.age == o1.age) {
            o1.name.compareTo(o2.name)
        }else {
            o2.age - o1.age
        }
    }

    val personList = initPersonList()
    println("before")
    personList.forEach(::println)

    println("after")
    personList.sortWith(c1)
    personList.forEach(::println)
}

fun testList2() {
    val list = arrayListOf<Char>('a','b','c','d')

    val a = list.map { it - 'a' }.filter { it > 0 }.find { it>1 }

    println("a $a")
}

fun testList() {
    val list = arrayListOf<String>("a","b","c","d")
    for(str in list) {
        println("str: $str")
    }

    for((index,value) in list.withIndex()) {
        println("index: $index, value: $value")
    }
}
//fun testFor1
fun testFor() {
    for(i in 1..10) {
        println("$i")
    }

    for(i in 10 downTo 1) {
        println("down $i")
    }

    for(i in 1..10 step 2) {
        println("step $i")
    }


    repeat(10) {
        println("repeat $it")
    }

    for(i in 1 until 10) {
        println("$i")
    }
}

fun testSplit() {
    val user = User(12,name)
    val (age,name) = user
    println(age)
    println(name)
}

class User(var age:Int,var name:String) {
    operator fun component1() = age
    operator fun component2() = name
}
sealed class SuperCommand {
    object A:SuperCommand()
    object B:SuperCommand()
    object C:SuperCommand()
    object D:SuperCommand()
}

fun testSealed(sealedValue:SuperCommand) {
    when(sealedValue) {
        SuperCommand.A -> {

        }
        SuperCommand.B -> {

        }
        SuperCommand.C -> {

        }
        SuperCommand.D -> {

        }

    }

}

enum class Command(val count:Int) {
    A(1),B(2),C(3),D(4)
}

fun testEnum(command: Command) {
    when(command) {
        Command.A -> {
            println(Command.A.count)
        }
        Command.B -> {
            println(Command.B.count)
        }
        Command.C -> {
            println(Command.C.count)
        }
        Command.D -> {
            println(Command.D.count)
        }
    }

}

interface Animal2 {
    fun bark()
}

class Dog2 :Animal2 {
    override fun bark() {
        println("wang")
    }
}

class Zoo(animal:Animal2) :Animal2 by animal


fun functionVar() {
    val runnable = Runnable {
        println("run")
    }

    val function2:()->Unit
    function2 = runnable::run
//    function2()
    function2.invoke()

}

fun testOnlyIf() {
    onlyIf(true) {
        println("function onIf run2")
    }

    onlyIf(true,"this is test text") {
        println("msg:$it")
    }
}

fun onlyIf(isDebug:Boolean,block:()->Unit) {
    if(isDebug) {
        block()
    }
}

fun onlyIf(isDebug:Boolean,msg:String,block:(String)->Unit) {
    if(isDebug) {
        block(msg)
    }
}


fun testInvoke() {
    val a = { name:String->
        println("cat's name is $name")
    }

    a.invoke("bluecat")
}

fun testThread() {
    val thread = Thread{
        var count = 10
        while(count>0) {
            println(Dog().printName(Dog()))
            Thread.sleep(100)
            count-=1
        }
    }

    thread.start()
}

open class Animal
class Dog : Animal()

fun Animal.name() = "animal"
fun Dog.name() = "dog"


fun Animal.printName(anim:Animal) {
    println(anim.name())
}

fun Dog.printName(anim:Dog) {
    println(anim.name())
}

fun function() {
    val str = "hello world"
    fun say(count:Int = 10) {
        println(str+count)
        if(count>0) {
            say(count-1)
        }
    }

    say()
}

fun printLength(name:String) {
    println(name.length)
}

object Test2 {
    fun sayMessage(msg:String) {
        println(msg)
    }

    fun sayMessage2(msg:String) {
        echo("message is ${msg}")
    }
}

var nullStr:String? = null
var age:Int = 18
val name:String = "Lijunjie"