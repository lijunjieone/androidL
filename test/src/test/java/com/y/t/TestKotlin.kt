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