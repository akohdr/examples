# Copyright (c) 2010 Andrew Wild (akohdr@gmail.com)
# Licensed under the MIT (MIT-LICENSE.txt) licence.

package ternary;

object Primes {
  implicit def bigInt2BigInteger(b: BigInt) = b.bigInteger
  implicit def bigInteger2BigInt(b: java.math.BigInteger) = new BigInt(b)
  
  def divisors(n: Int): List[Int] = for (val i <- List.range(1, n + 1); n % i == 0) yield i;
  def isPrime(n: Int) = divisors(n).length == 2;
  
  def naivePrimes(b: Int)(e: Int):List[Int] = List.range(b,e) filter isPrime

    // Stream impl     
  def from(n: Int): Stream[Int] = Stream.cons(n, from(n + 1))
  def sieveEratosthenes(s: Stream[Int]): Stream[Int] = Stream.cons(s.head, sieveEratosthenes(s.tail filter { _ % s.head != 0 }))
  def primes = sieveEratosthenes(from(2))
  
  // Borrow Java's impl makes use of implicit BitInt conversions
  lazy val primesJava: Stream[BigInt] = Stream.cons(2, primesJava.map(_.nextProbablePrime))
  
}
