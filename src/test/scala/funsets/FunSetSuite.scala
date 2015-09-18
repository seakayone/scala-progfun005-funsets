package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)

    val u12 = union(s1, s2)
    val u23 = union(s2, s3)
    val u123 = union(u12, u23)
    val u24 = union(s2, s4)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(contains(s2, 2), "Singleton")
      assert(contains(s3, 3), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains intersections of elements"){
    new TestSets {
      val s = intersect(u12, u23)
      assert(!contains(s, 1), "Intersect 1")
      assert(contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect3")
    }
  }

  test("diff contains difference of elements"){
    new TestSets {
      val s = diff(u12, u23)
      assert(contains(s, 1), "Intersect 1")
      assert(!contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect3")
    }
  }

  test("filter contains only filtered (x < 3) elements of a set"){
    new TestSets {
      val s = filter(u123, x=> x < 3 )
      assert(contains(s, 1), "Filter 1")
      assert(contains(s, 2), "Filter 2")
      assert(!contains(s, 3), "Filter 3")
    }
  }

  test("filter contains only filtered  (x => 2) elements of a set"){
    new TestSets {
      val s = filter(u123, x => x >= 2 )
      assert(!contains(s, 1), "Filter 1")
      assert(contains(s, 2), "Filter 2")
      assert(contains(s, 3), "Filter 3")
    }
  }

  test("forall iterates a set and checks (x >= 1 && x <=3) condition"){
    new TestSets {
      val s = forall(u123, x=> (x >= 1 && x <=3))
      assert(s, "Union Set 1,2,3 contains integers 1 <= x <= 3")
    }
  }

  test("forall iterates a set and checks (x > 4) condition"){
    new TestSets {
      val s = forall(u123, x=> (x > 4))
      assert(!s)
    }
  }

  def isEven(x:Int):Boolean = (x % 2 == 0)
  def isOdd(x:Int):Boolean = !isEven(x)

  test("forall iterates a set and checks u1234 only contains even numbers"){
    new TestSets {
      val s = forall(u24, x=> isEven(x))
      assert(s, "Union set 2,4 only contains even numbers")
    }
  }

  test("exists checks if at least one element fulfills the even condition"){
    new TestSets {
      val s = exists(u123, x=> isEven(x))
      assert(s, "Union set 1,2,3 contains at least one even number")
    }
  }

  test("exists checks if at least one element fulfills the odd condition"){
    new TestSets {
      val s = exists(u123, x=> isOdd(x))
      assert(s, "Union set 1,2,3 contains at least one odd number")
    }
  }

  test("exists checks if at least one element doesn't fulfill the odd condition"){
    new TestSets {
      val s = exists(u24, x=> isOdd(x))
      assert(!s, "Union set 2,4 does not contain any odd number")
    }
  }

  test("map transforms one set into the other by multiplying with 2"){
    new TestSets {
      val s = map(u24, x=> x*2)
      assert(!contains(s, 2))
      assert(contains(s, 4))
      assert(contains(s, 8))
    }
  }

}
