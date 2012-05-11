# Copyright (c) 2010 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.

package ternary;

import TernaryDigit._

// Companion object of SAME name as class in SAME file
object TernaryDigit {

  type D = TernaryDigit  // type abbreviations
  type DS = List[TernaryDigit]
  
  // effectively static members of TernaryDigit
  val PLUS = '+'
  val ZERO = '0'   
  val MINUS = '-'
  
  val states = List(MINUS, ZERO, PLUS)
  
  // Singleton for each digit 'type' overrides are optimizations
  val MINUS_DIGIT = new TernaryDigit(MINUS) {
    override def *(d: D) = !d
    override def +(d: D) = d enc match {
      case PLUS => ZERO_DIGIT
      case ZERO => this
      case MINUS => forEnc(PLUS)  // breaks cyclic dependancy effectively PLUS_DIGIT
    }
  }
  val PLUS_DIGIT = new TernaryDigit(PLUS) {
    override def *(d: D) = d
    override def +(d: D) = d.enc() match {
      case MINUS => ZERO_DIGIT
      case ZERO => this
      case PLUS => MINUS_DIGIT
    }
  }
  val ZERO_DIGIT:TernaryDigit = new TernaryDigit(ZERO) {
    override def *(d: D) = this
    override def +(d: D) = d
  }
  
  // singleton partially applied curried functions (refer p198 Venners)
  // TODO: since all are commutative find 'cheapest' x*y vs y*x
  // TODO: investigate extension to Zellweger morphisms
  val timesPlus = multiply(PLUS)_             // input
  val timesMinus = multiply(MINUS)_           // !input
  val timesZero = multiply(ZERO)_             // ZERO
  val sumPlus = sum(PLUS)_                    // ++state
  val sumMinus = sum(MINUS)_                  // --state
  val sumZero = sum(ZERO)_                    // input
  val carryPlus = carry(PLUS)_                // case '+' => '+'; case _ => '0'
  val carryMinus = carry(MINUS)_              // case '-' => '-'; case _ => '0'
  val carryZero = carry(ZERO)_                // ZERO

  def negate(d: D):D = d match {
    case PLUS_DIGIT => MINUS_DIGIT
    case ZERO_DIGIT => ZERO_DIGIT
    case MINUS_DIGIT => PLUS_DIGIT
  }
    
  def negate(d: char):D = d match {
    case PLUS => MINUS_DIGIT
    case ZERO => ZERO_DIGIT
    case MINUS => PLUS_DIGIT
  }
  
  def multiply(x: char)(y: char):D = {
    multiply(forEnc(x))(forEnc(y))
  }
  def multiply(x: D)(y: D):D = {
    if(ZERO_DIGIT.equals(x) | ZERO_DIGIT.equals(y)) return ZERO_DIGIT
    if(x.equals(y)) PLUS_DIGIT else MINUS_DIGIT
  }

  def sum(x: D)(y: D):D = forEnc(sum(x enc)(y enc))
  //TODO: tail recursive ??  exploit cyclicality of states ?
  def sum(x: char)(y: char):char = x match {
    case ZERO => y
    case PLUS => y match {
      case ZERO => PLUS
      case PLUS => MINUS
      case MINUS => ZERO
    }
    case MINUS => y match{
      case ZERO => MINUS
      case PLUS => ZERO
      case MINUS => PLUS
    }
  }
  
  def carry(x: D)(y: D):D = forEnc(carry(x enc)(y enc))
  def carry(x: char)(y: char):char = { return if(x == y) x else ZERO }

  def halfAdder(x: D)(y: D):(D, D) = (carry(x)(y), sum(x)(y))
  def fullAdder(x: D)(y: D)(cin: D):(D, D) = {
    rules(x,y,cin)
  }
  
  // Ternary addition rules coded from Knuth vol 2 p207
  private def rules(x: char, y: char, c: char):(char,char) = (x,y,c) match {
    case ('-','-','-') => ('-','0')
    case ('-','-','0') => ('-','+')
    case ('-','-','+') => ('0','-')
    case ('-','0','-') => ('-','+')
    case ('-','0','0') => ('0','-')
    case ('-','0','+') => ('0','0')
    case ('-','+','-') => ('0','-')
    case ('-','+','0') => ('0','0')
    case ('-','+','+') => ('0','+')
    case ('0','-','-') => ('-','+')
    case ('0','-','0') => ('0','-')
    case ('0','-','+') => ('0','0')
    case ('0','0','-') => ('0','-')
    case ('0','0','0') => ('0','0')
    case ('0','0','+') => ('0','+')
    case ('0','+','-') => ('0','0')
    case ('0','+','0') => ('0','+')
    case ('0','+','+') => ('+','-')
    case ('+','-','-') => ('0','-')
    case ('+','-','0') => ('0','0')
    case ('+','-','+') => ('0','+')
    case ('+','0','-') => ('0','0')
    case ('+','0','0') => ('0','+')
    case ('+','0','+') => ('+','-')
    case ('+','+','-') => ('0','+')
    case ('+','+','0') => ('+','-')
    case ('+','+','+') => ('+','0')
  }

  private def rules(x: D, y: D, c: D):(D,D) = (x,y,c) match {
    case (MINUS_DIGIT,MINUS_DIGIT,MINUS_DIGIT) => (MINUS_DIGIT,ZERO_DIGIT)
    case (MINUS_DIGIT,MINUS_DIGIT,ZERO_DIGIT) => (MINUS_DIGIT,PLUS_DIGIT)
    case (MINUS_DIGIT,MINUS_DIGIT,PLUS_DIGIT) => (ZERO_DIGIT,MINUS_DIGIT)
    case (MINUS_DIGIT,ZERO_DIGIT,MINUS_DIGIT) => (MINUS_DIGIT,PLUS_DIGIT)
    case (MINUS_DIGIT,ZERO_DIGIT,ZERO_DIGIT) => (ZERO_DIGIT,MINUS_DIGIT)
    case (MINUS_DIGIT,ZERO_DIGIT,PLUS_DIGIT) => (ZERO_DIGIT,ZERO_DIGIT)
    case (MINUS_DIGIT,PLUS_DIGIT,MINUS_DIGIT) => (ZERO_DIGIT,MINUS_DIGIT)
    case (MINUS_DIGIT,PLUS_DIGIT,ZERO_DIGIT) => (ZERO_DIGIT,ZERO_DIGIT)
    case (MINUS_DIGIT,PLUS_DIGIT,PLUS_DIGIT) => (ZERO_DIGIT,PLUS_DIGIT)
    case (ZERO_DIGIT,MINUS_DIGIT,MINUS_DIGIT) => (MINUS_DIGIT,PLUS_DIGIT)
    case (ZERO_DIGIT,MINUS_DIGIT,ZERO_DIGIT) => (ZERO_DIGIT,MINUS_DIGIT)
    case (ZERO_DIGIT,MINUS_DIGIT,PLUS_DIGIT) => (ZERO_DIGIT,ZERO_DIGIT)
    case (ZERO_DIGIT,ZERO_DIGIT,MINUS_DIGIT) => (ZERO_DIGIT,MINUS_DIGIT)
    case (ZERO_DIGIT,ZERO_DIGIT,ZERO_DIGIT) => (ZERO_DIGIT,ZERO_DIGIT)
    case (ZERO_DIGIT,ZERO_DIGIT,PLUS_DIGIT) => (ZERO_DIGIT,PLUS_DIGIT)
    case (ZERO_DIGIT,PLUS_DIGIT,MINUS_DIGIT) => (ZERO_DIGIT,ZERO_DIGIT)
    case (ZERO_DIGIT,PLUS_DIGIT,ZERO_DIGIT) => (ZERO_DIGIT,PLUS_DIGIT)
    case (ZERO_DIGIT,PLUS_DIGIT,PLUS_DIGIT) => (PLUS_DIGIT,MINUS_DIGIT)
    case (PLUS_DIGIT,MINUS_DIGIT,MINUS_DIGIT) => (ZERO_DIGIT,MINUS_DIGIT)
    case (PLUS_DIGIT,MINUS_DIGIT,ZERO_DIGIT) => (ZERO_DIGIT,ZERO_DIGIT)
    case (PLUS_DIGIT,MINUS_DIGIT,PLUS_DIGIT) => (ZERO_DIGIT,PLUS_DIGIT)
    case (PLUS_DIGIT,ZERO_DIGIT,MINUS_DIGIT) => (ZERO_DIGIT,ZERO_DIGIT)
    case (PLUS_DIGIT,ZERO_DIGIT,ZERO_DIGIT) => (ZERO_DIGIT,PLUS_DIGIT)
    case (PLUS_DIGIT,ZERO_DIGIT,PLUS_DIGIT) => (PLUS_DIGIT,MINUS_DIGIT)
    case (PLUS_DIGIT,PLUS_DIGIT,MINUS_DIGIT) => (ZERO_DIGIT,PLUS_DIGIT)
    case (PLUS_DIGIT,PLUS_DIGIT,ZERO_DIGIT) => (PLUS_DIGIT,MINUS_DIGIT)
    case (PLUS_DIGIT,PLUS_DIGIT,PLUS_DIGIT) => (PLUS_DIGIT,ZERO_DIGIT)
  }

  def forEnc(c: char): D = c match {
    case PLUS => PLUS_DIGIT
    case ZERO => ZERO_DIGIT
    case MINUS => MINUS_DIGIT
    case _ => throw new IllegalArgumentException("no such digit: "+c)
  }
}




//  Primary constructor creates single char field class and has NPE check
class TernaryDigit (val c: char){

  if(null == c) 
    throw new NullPointerException("null digit")
  
  // auxillary constructor takes first element of provided string as digit
  //def this(s: String) = this(s.toCharArray()(0))  
   
  // effectively a 'final' partially applied functions ... aka partial closures ?
  private val multiplyMe = multiply(this)_
  private val sumMe = sum(this)_
  private val carryMe = carry(this)_
  private var notMe = () => negate(c)  // hopefully this optimzes to one-shot
  
  // use partially applied curried functions in overloaded operators
  // gotta love the built in primitives (!)
  def unary_~() = unary_!
  def unary_!() = notMe()
  def *(that: D): D = multiplyMe(that)
  def +(that: D): D = sumMe(that)
  def -(that: D): D = sumMe(!that)
  //def /(that: D): D = ???
  
  def enc() = c
  def equals(d: D) = { d.enc() == c }

  override def toString() = c.toString()
}

