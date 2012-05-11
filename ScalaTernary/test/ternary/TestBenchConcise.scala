package ternary;

import TernaryWord._

object TestBenchConcise {
  val now:() => Long = System.currentTimeMillis
  
  def main(args: Array[String]) {
    val n = 10000
    val test1 = bench1(n)_
    val test2 = bench2(n)_
    test1("a+b", _ + _)
    test1("a-b", _ - _)
    test1("a*b", _ * _)
    test2("a+b+c", _ + _ + _)
    test2("a*b*c", _ * _ * _)
    test2("a+b*c", _ + _ * _)
    test2("a*b+c", _ * _ + _)
  }
  
  val a:W = "+-0+-0+-0"     // implicit conversions
  val b:W = 4542            // a = b
  val c:W = a               // a = b = c

  def bench1(n: Int)(s:String, u:(W,W) => W): Long = bench(s, () => u(a,b), n, 1)
  def bench2(n: Int)(s:String, u:(W,W,W) => W): Long = bench(s, () => u(a,b,c), n, 2)
  
  def bench(s:String, unitUnderTest: () => Unit, n: Int, ops: Int): Long = {
    val start = now()
      for(i <- 1 to n) 
        unitUnderTest apply // not calling apply causes call to be lazily optimized out :) 
    val delta = (now()-start)+1
    println("performed "+n+" iterations of "+s+" in "+delta+"ms. => "+(1000*n*ops)/delta+" ops/s")
    delta                   // last line of func is return keyword optional
  }
}
