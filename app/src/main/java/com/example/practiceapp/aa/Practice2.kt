package com.example.practiceapp.aa

class Practice2 {
}

fun main(){

    val students = arrayListOf<Student>()
    students.add(Student("vis",27))
    students.add(Student("alv",24))
    students.add(Student("sur",25))

    val res = students.map { it.name }
    val r = students.filter { it.age > 25 }
    println()

    

}



data class Student(val name:String,val age:Int)