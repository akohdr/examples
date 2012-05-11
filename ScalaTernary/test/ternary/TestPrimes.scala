package ternary;

import TernaryWord._
import TestPrimes._
import Primes._

object TestPrimes {
  
  def main(args: Array[String]) {
    
    def color(a:Int, b:Int, c:Int):Int = (a,b,c) match {
      case (13, 3, 13) => 12
      case (6, _, 4) => 15
      case (10, _, 3) => 15
      case (10, _, 11) => 15
      case (13, 7, _) => 8
      case (13, 8, 7) => 13
      case (15, 8, _) => 1
      case (8, _, _) => 7 
      case (15, 1, _) => 2 
      case (_, 1, _) => 1
      case (1, _, _) => 8 
      case (2, _, _) => 13 
      case (4, _, _) => 13 
      case (5, _, _) => 13 
      case (15, 2, _) => 4
      case (_, 4, 8) => 4 
      case (_, 4, _) => 5 
      case (_, 5, _) => 3 
      case (15, 3, _) => 12 
      case (_, 2, _) => 2
      case (_, 3, _) => 3
      case (_, 8, _) => 8
      case (_, 11, _) => 10
      case (_, 12, _) => 11
      case (11, _, _) => 13 
      case (13, _, 1) => 15 
      case (13, _, 2) => 15 
      case (13, _, 3) => 15 
      case (13, _, 5) => 15 
      case (13, _, 6) => 15 
      case (13, _, 10) => 15 
      case (13, _, 11) => 15 
      case (13, 0, 8) => 15
      case (14, _, 6) => 15 
      case (14, _, 10) => 15 
      case (10, 0, 10) => 15 
      case (10, 9, 10) => 15 
      case (10, 13, 10) => 15 
      case (10, 0, 6) => 15 
      case (10, 9, 6) => 15 
      case (10, 13, 6) => 15 
      case (6, _, 6) => 0
      case (_, _, 10) => 9 
      case (6, 15, 9) => 14 
      case (10, 15, 9) => 14 
      case (_, 6, 9) => 10 
      case (_, 6, 14) => 10 
      case (_, 6, 15) => 10 
      case (_, 10, 9) => 10 
      case (_, 10, 14) => 10 
      case (_, 10, 15) => 10 
      case (_, 6, _) => 6 
      case (_, 10, _) => 6 
      case (6 , 15, _) => 13 
      case (10, 15, _) => 13 
      case (13, _, 9) => 14 
      case (13, _, 15) => 14 
      case (14, _, 9) => 14 
      case (14, _, 15) => 14 
      case (13, _, _) => 13 
      case (14, _, _) => 13 
      case (_, _, 15) => 15
      case (_, _, 9) => 9 
      case (_, _, 14) => 9 
      case (_, _, _) => 0
    }
    
    
    /*  Wolfram p1109 rules for primes CA
    {10, 0, 4, 8}  initial condition surrounded by zeros
    {{13, 3, 13} --> 12, 
      {6, _, 4} --> 15, 
      {10, _, 3 | 11} --> 15, 
      {13, 7, _} --> 8, 
      {13, 8, 7} --> 13, 
      {15, 8, _} 1, 
      {8, _, _} --> 7, 
      {15, 1, _} --> 2, 
      {_, 1 _} --> 1, 
      {1, _, _}--> 8, 
      {2 | 4 | 5, _, _} 13, 
      {15, 2, _} 4, 
      {_, 4, 8} --> 4, 
      {_, 4, _} 5, 
      {_, 5, _} 3, 
      {15, 3, _} --> 12, 
      {_, x: (2 | 3 | 8), _} --> x,
      {_, x: (11 | 12), _} --> x - 1,
      {11, _, _} --> 13, 
      {13, _, 1 | 2 | 3 | 5 | 6 | 10 | 11} --> 15, 
      {13, 0, 8} --> 15, 
      {14, _, 6 | 10} --> 15, 
      {10, 0 | 9 | 13, 6 | 10} --> 15, 
      {6, _, 6} --> 0, 
      {_, _, 10} --> 9, 
      {6 | 10, 15, 9} --> 14, 
      {_, 6 | 10, 9 | 14 | 15} --> 10, 
      {_, 6 | 10, _} --> 6, 
      {6 | 10, 15, _} --> 13, 
      {13 | 14, _, 9 | 15} --> 14, 
      {13 | 14, _, _} --> 13, 
      {_, _, 15} --> 15,
      {_, _, 9 | 14} --> 9, 
      {_, _, _} --> 0}
    }
    */
    
    
    //val pt: List[W] = primesBySieve(10) map int2TernaryWord
    //pt map ((t:W) => t.pad(10,' ')+t.toString.reverse) map println
    //pt map ((t:W) => t.pad(20,' ')) map println
    
    //val p2: Stream[BigInt] = Stream.range(0,100) map primes
    //val pt2: Stream[W] = p2 map bigint2TernaryWord
    //while(true)
    //pt2 map ((t:W) => t.toString.reverse) map println
    
    // Stream impl     
    var primesT:Stream[W] = primesJava map bigint2TernaryWord
    
    val ps = primesT take 50000 
  
    var c = ' '
    var l = 0
    val n = 2
    var x = new Array[Int](n)
    var w = 0
    var e = 0
    def sigma(xs: List[Int]): Int = (0 /: xs) (_ + _)
    def pi(xs: List[Int]): Int = (1 /: xs) (_ * _)
    
    def loop(s: Stream[W]):Stream[W] = {
      val cs = s.head.pad(10).toString.reverse
      def f(c:char):int = {if(c=='+') 1 else -1}
      val fv = (c: char) => if(c=='+') 1 else -1
      val ns:List[Int] = (cs.toList map fv)
      
      for(i <- n to 1) x(i) = x(i-1)
      x(0) = pi(ns)
      
      e=sigma(x.toList)
      c =if(e > 0) '+' else '-'
        
      c match {
        case '+' => print("O")
        case '-' => print("-")
        case _ => print(c)
      }
      
      l += 1
      if(l>w) { 
        println; l = 0; //w+=e
      }
      s.tail
    }

    val v = 200
    for (j <- 1 to 9){
      w = v+j; l=0
      println; println
      var ls = ps.tail
      for (i<-0 to 45000)
        ls = loop(ls)
    }
  }

}
