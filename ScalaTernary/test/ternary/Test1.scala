package ternary;

object Test1{
  def main(args: Array[String]) {
    println("Test1...")
    
    for(i <- -5 to 5) {
      var word = TernaryWord.forValue(i);
      println("word : "+word+"\tvalue: "+word.value)
      println("!word: "+(!word)+"\tvalue: "+(!word).value)
    }
    
    val RANGE = 10
    if(!false)
    for(i <- -RANGE to RANGE) {
      val s = TernaryWord.forValue(i)
      println(s+"\t"+s.value);
    } 
    
    
  }
  
  
}
