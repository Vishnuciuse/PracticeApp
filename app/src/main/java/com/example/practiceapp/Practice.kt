package com.example.practiceapp

class Practice {
}

fun main(){

    val map = HashMap<Int,Int>()
    val array = arrayListOf<String>()
    val checkList = listOf(5,2,2,6,9,7,5,9,9,2,1,4)




    for(num in checkList){
        if(map.containsKey(num)) {
            map[num] = map[num]!! +1
        }else{
            map[num]= 1
        }

    }

    println(map)
    val res1 = customHoF(3,5) { x,y -> x+y  }
    println(res1)
}

fun customHoF(a:Int,b:Int,operation:(Int,Int)->Int):Int{
   return operation(a,b)
}




