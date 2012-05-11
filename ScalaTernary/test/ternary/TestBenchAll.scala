package ternary;

import TernaryWord._  // this is secret sauce to give you type abbreviation W and implicit conversions

object TestBenchAll {
  type f_Long = () => Long                    // type abbreviations think #define
  type f0 = () => W
  type f2 = (W,W) => W
  type f3 = (W,W,W) => W
  
  val now:f_Long = System.currentTimeMillis   // function value
  
  def main(args: Array[String]) {
    val n = 10000
    def test1(t:(String, f2)) = {             // nested function
      bench1(n)(t._1,t._2)                    // tuple access/application
    }
    val test2 = bench2(n)_                    // partially applied function value
    
    List(("a+b", (_ + _):f2),
         ("a-b", (_ - _):f2)) map test1       // apply function to tuples in list
         
    test1("a*b", _ * _)     // any redundant syntax in Scala can be left out
    
    test2("a+b+c", _ + _ + _)
    test2("a*b*c", _ * _ * _)
    test2("a+b*c", _ + _ * _)
    test2("a*b+c", _ * _ + _)
  }
  
  val a:W = "+-0+-0+-0"     // implicit conversions
  val b:W = 4542            // a = b
  val c:W = a               // a = b = c

  def bench1(n: Int)(s:String, u:f2): Long = bench(s, () => u(a,b), n, 1)
  def bench2(n: Int)(s:String, u:f3): Long = bench(s, () => u(a,b,c), n, 2)
  
  def bench(s:String, unitUnderTest: f0, n: Int, ops: Int): Long = {
    val start = now()
      for(i <- 1 to n) 
        unitUnderTest apply // not calling apply causes call to be lazily optimized out :) 
    val delta = (now()-start)+1
    println("performed "+n+" iterations of "+s+" in "+delta+"ms. => "+(1000*n*ops)/delta+" ops/s")
    delta                   // last line of func is return keyword optional
  }
}
