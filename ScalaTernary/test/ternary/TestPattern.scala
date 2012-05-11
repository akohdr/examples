package ternary;

import TernaryWord._
import java.io._

object TestPattern {

  def main(args: Array[String]) {
    readSloaneDb()
    System.exit(0)
    
    var n: Int=100
    var m: Int=2
    if(args.length > 0){
      n = Integer.parseInt(args(0))
      m = Integer.parseInt(args(1))
    }
      
    var t:TernaryWord = TernaryWord.forValue(1)
    for(i <- 1 to n){
      println(t.pad(n,' ')+": "+t.value)
      t *= m
    }
  }
  
  def readSloaneDb() = {
    val in = new FileReader("C:/sloane/sloane_db")
    var c:char = ' '
    var i = 0
    var s = ""
    var name = ""
    do {
     i = in.read()
     c = i.toChar
     if(c==','){
       if(s.startsWith("A"))
         name=s
       else {
         try {
           val t:TernaryWord = java.lang.Long.parseLong(s)
           println(name+t.toString.reverse)
         } catch {
           case e: NumberFormatException => println(e.getMessage);
         }
       }
       s = ""
     }
     else if((c>0)&&(c!='\n')) s+= c
    } while(i>0)
  }
}
