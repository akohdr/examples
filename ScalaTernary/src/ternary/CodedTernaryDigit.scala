# Copyright (c) 2010 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.

package ternary;

class CodedTernaryDigit {
  //val PLUS_CODE = (true,true)
  //val ZERO_UP_CODE = (false,true)
  //val ZERO_DOWN_CODE = (true,false)
  //val MINUS_CODE = (false,false)
  
  /*
    def cforEnc(t: Tuple2[Boolean, Boolean]): TernaryDigit = t match {
      case PLUS_CODE => PLUS_DIGIT
      case ZERO_UP_CODE => ZERO_DIGIT
      case ZERO_DOWN_CODE => ZERO_DIGIT
      case MINUS_CODE => MINUS_DIGIT
      //case _ => throw new IllegalArgumentException("no such digit: "+t2)
    } 
    */
      
      // experimental code using two bit coding scheme
      /*
      def csum(x:(Boolean, Boolean))(y:(Boolean, Boolean)):(Boolean, Boolean) = {
        val xIsZero = x._1 ^ x._2   // XOR
        if (xIsZero) return y
        
        val yIsZero = y._1 ^ y._2   // XOR
        if (yIsZero) return x
        
        val xIsPlus = x._1 // we can assume !xIsZero & x._1
        val yIsPlus = y._1 //!yIsZero & y._1
        
        if (xIsPlus)
          if (yIsPlus) MINUS_CODE else ZERO_UP_CODE
        else
          if (yIsPlus) ZERO_DOWN_CODE else MINUS_CODE
      }
      */
      

}
