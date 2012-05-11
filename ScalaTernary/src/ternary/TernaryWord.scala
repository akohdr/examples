# Copyright (c) 2010 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.

package ternary;

import scala.collection.mutable.ListBuffer
import TernaryDigit._
import TernaryWord._

object TernaryWord{
  
  type W = TernaryWord  // type abbreviation

  def forValue(v:Double):W = if (v<0) !forAbsValue(-v) else forAbsValue(v)
  private def forAbsValue(v:Double):W = {
    var l = -1;  var a = 1.0  
    while ((v / a) > 0.5) { a *= 3;  l += 1 }
    forAbsValue(v, l)
  }
  
  def forValue(v:Long):W = if (v<0) !forAbsValue(Math.abs(v)) else forAbsValue(v)
  private def forAbsValue(v:Int):W = {
    //  l is number of positions (precision) could be set externally
    //  roughly based on optimized form of log3(v) + 1                         
    var l = -1;  var a = 1.0  
    while ((v / a) > 0.5) { a *= 3;  l += 1 }
    forAbsValue(v, l)
  }
  
  private def forAbsValue(v:Double, l:Int):W = {
    var word: DS = List()
    var w: Double = 1       // we must use double to prevent capping as geometric series grows large
    var aq: Double = v+1    // this is a+q combination where a=abs(v) and q=phase shift for column
    
    for(val p <- 0 to l){
      // Figure out next digit and prepend it to word
      word = {((((aq / w).intValue()) % 3) match {
        case 0 => MINUS_DIGIT
        case 2 => PLUS_DIGIT
        case _ => ZERO_DIGIT
      })}::word
      
      w *= 3   // period/wavelength of column geometric series of 3 more efficient than using Math.pow(3,p)
               // w geometric series 1*3*3*3 ... *3
      
      aq += w  // phase shift is arithmetic series of the wavelength
               // q arithmetic series w^0 + w^1 + w^2  ...  +w^n
    }
    return new TernaryWord(word);
  }
  
  implicit def int2TernaryWord(v: Int):W = forValue(v)
  implicit def long2TernaryWord(v: Long):W = forValue(v)
  implicit def bigint2TernaryWord(v: BigInt):W = forValue(v.longValue)
  implicit def str2TernaryWord(s: String):W = { new TernaryWord(s.toList map TernaryDigit.forEnc) }
}



// implementation of TernaryWord using has-a ds: List rather than is-a
class TernaryWord(val ds: DS) {
  // ds is in MSB->LSB order

    if(null == ds)
      throw new NullPointerException("null word")
    if(ds.contains(null))
      throw new NullPointerException("word contains null: "+ds)

  // public accessor
  def digits():DS = this.ds
  
  private var notMe:W = null  // lazy one-shot since value of this word is final so to is it's negative dual
  def unary_~() = unary_!
  def unary_!() = {if(null == notMe) notMe = new TernaryWord(ds map(!_)); notMe}
  
  //TODO: what's best way to make this lazy
  val value:Int = {
    var p = 1
    var v = 0
    for(d <- ds.reverse){
      d match {
        case PLUS_DIGIT => v += p
        case MINUS_DIGIT => v -= p
        case _ =>
      }
      p *= 3
    }
    v  
  }
  
  override def toString() = ds.mkString("")  // parameterless dumps list elements with their toString() using StringBuilder
  
  def pad(l: Int):String = pad(l,'0')
  def pad(l: Int, c:char):String = {
    val d:Int = l-(ds.length)
    val pre = new Array[char](Math.abs(d))
    java.util.Arrays.fill(pre,c)  // TODO what's the Scalaism
    //ds.mkString(pre.mkString,"","")
    pre.mkString("","",ds.mkString(""))
  }
  
  def *(that: W): W = {
    var suffix: DS = Nil
    var acc = forValue(0)
    var partial = forValue(0)
    for (d <- that.digits.reverse) {
      partial = new TernaryWord((ds map d.*):::suffix)
      //print("\n"+partial)
      acc += partial
      suffix = ZERO_DIGIT::suffix
    }
    acc
  }
 
  def -(that: W): W = { this+(!that) }
  def +(that: W): W = {
    var as:DS = Nil
    var cin:D = ZERO_DIGIT
    val zs = zipPad(ds.reverse, that.digits.reverse)       // digits are stored MSB -> LSB
    for(z <- zs) {
      val (cout: D, sum: D) = TernaryDigit.fullAdder(z._1)(z._2)(cin)
      //print("\n >"+z+"\t"+sum+"\t"+cin+"\t"+cout)
      as = sum::as
      cin = cout
    }
    
    while(as != Nil && ZERO_DIGIT == as.head){ as = as.tail }
    new TernaryWord(as)
  } 

  private def zipPad(a: DS, b: DS) = if(a.lengthCompare(b.length)<0) zipPadOrdered(a,b) else zipPadOrdered(b,a)
  private def zipPadOrdered(short: DS, long: DS): List[(D, D)] = {
    val b = new ListBuffer[(D, D)]
    var s = short
    var l = long
    while (!s.isEmpty) {
      b += (s.head, l.head)
      s = s.tail
      l = l.tail
    }
    while (!l.isEmpty) {
      b += (ZERO_DIGIT, l.head)
      l = l.tail
    }
    b += (ZERO_DIGIT, ZERO_DIGIT)  // ensures we account for carries
    b.toList
  }
  
}
