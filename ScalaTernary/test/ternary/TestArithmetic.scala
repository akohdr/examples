package ternary;

object TestArithmetic {
  def main(args: Array[String]) {

    for(i <- 1 to 100; 
      val a = TernaryWord.forValue((Math.random*1000).intValue);
      val b = TernaryWord.forValue((Math.random*1000).intValue);
      val av = a.value; val bv = b.value
    ) {
      print(av+":"+a+"\t")
      print(bv+":"+b+"\t")
      print(av+"+"+bv+"="+(av+bv)+"\t")
      print(av+"-"+bv+"="+(av-bv)+"\t")
      print(av+"*"+bv+"="+(av*bv))
      val s = a+b
      print("\tADD "+s.value+":"+s)
      val d = a-b
      print("\tSUB "+d.value+":"+d)
      val m = a*b
      print("\tMULT "+m.value+":"+m)
      println
      if ((av+bv)!=s.value)throw new Exception("addition mis-match")
      if ((av-bv)!=d.value)throw new Exception("subtraction mis-match")
      if ((av*bv)!=m.value)throw new Exception("multiply mis-match")
    }
  }
}
