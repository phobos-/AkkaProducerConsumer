import org.scalatest.{ FlatSpec, Matchers }

import scala.annotation.tailrec

//noinspection ScalaStyle
class ScalaProblemsTest extends FlatSpec with Matchers {

  /*
   * puzzles from this blog:
   * http://blog.thedigitalcatonline.com/blog/2015/04/07/99-scala-problems-01-find-last-element/
   */

  private def appendLast[A](elem: A, list: List[A]): List[A] = list ::: List(elem)

  "A function " should "find last element of a list" in {

    val list = List(1, 2, 3, 4, 5)

    def lastElement[A](list: List[A]): A = list.last

    @tailrec
    def lastElementRec[A](list: List[A]): A = list match {
      case head :: Nil => head
      case _ :: tail   => lastElementRec(tail)
      case _           => throw new NoSuchElementException
    }

    lastElement(list) should equal(5)
    lastElementRec(list) should equal(5)
  }

  it should "find last but one element of a list" in {
    val list = List(5, 4, 3, 2, 1)

    def lastButOneElement[A](list: List[A]): A = list.take(list.size - 1).last

    @tailrec
    def lastButOneRec[A](list: List[A]): A = list match {
      case beforeLast :: _ :: Nil => beforeLast
      case _ :: tail              => lastButOneRec(tail)
      case _                      => throw new NoSuchElementException
    }

    lastButOneElement(list) should equal(2)
    lastButOneRec(list) should equal(2)
  }

  it should "find kth element of a list" in {

    val list = List(1, 2, 9, 4, 5)

    def kthElement[A](k: Int, list: List[A]): A = if (k > list.size) throw new IndexOutOfBoundsException else list(k)

    @tailrec
    def kthElementRec[A](k: Int, list: List[A]): A = list match {
      case head :: Nil  => head
      case head :: tail => if (k > 0) kthElementRec(k - 1, tail) else head
      case _            => throw new NoSuchElementException
    }

    kthElement(2, list) should equal(9)
    kthElementRec(2, list) should equal(9)
  }

  it should "find the number of elements of a list" in {

    val elems = List(5, 5, 5, 5, 5)

    def numElems[A](list: List[A]): Int = list.size

    def numElemsRec[A](list: List[A]): Int = {
      @tailrec
      def inner(l: List[A], acc: Int): Int = l match {
        case _ :: Nil  => acc + 1
        case _ :: tail => inner(tail, acc + 1)
        case _         => throw new NoSuchElementException
      }

      inner(list, 0)
    }

    def numElemsFold[A](list: List[A]): Int = list.foldLeft(0)((acc: Int, _) => acc + 1)

    numElems(elems) should equal(5)
    numElemsRec(elems) should equal(5)
    numElemsFold(elems) should equal(5)
  }

  it should "reverse a list" in {

    val reversedList = List(5, 4, 3, 2, 1)
    val normalList   = List(1, 2, 3, 4, 5)

    def reverse[A](list: List[A]): List[A] = list.reverse

    def reverseRec[A](list: List[A]): List[A] = {
      @tailrec
      def inner(l: List[A], reversedList: List[A]): List[A] = l match {
        case Nil          => reversedList
        case head :: tail => inner(tail, head :: reversedList)
        case _            => throw new NoSuchElementException
      }
      inner(list, List())
    }

    def reverseFold[A](list: List[A]): List[A] = list.foldLeft(List[A]())((reversed, head) => head :: reversed)

    reverse(reversedList) should equal(normalList)
    reverseRec(reversedList) should equal(normalList)
    reverseFold(reversedList) should equal(normalList)
  }

  it should "find out whether a list is a palindrome" in {

    val palindrome = List(1, 2, 3, 2, 1)

    def isPalindrome[A](list: List[A]): Boolean = list == list.reverse

    def isPalindromeRec[A](list: List[A]): Boolean = {
      @tailrec
      def inner(l: List[A], result: Boolean): Boolean = l match {
        case Nil     => result
        case List(_) => result
        case _       => inner(l.tail.init, result && l.head == l.last)
      }
      inner(list, true)
    }

    isPalindrome(palindrome) should be(true)
    isPalindromeRec(palindrome) should be(true)
  }

  it should "flatten a nested list structure" in {
    val bulgingList = List(List(1, 1), 2, List(3, List(5, 8)))
    val flatList    = List(1, 1, 2, 3, 5, 8)

    def flattenRec(list: List[Any]): List[Any] = list match {
      case Nil                      => Nil
      case (left: List[_]) :: right => flattenRec(left) ::: flattenRec(right)
      case head :: tail             => head :: flattenRec(tail)
    }

    def flattenTailRec(list: List[Any]): List[Any] = {
      @tailrec
      def inner(l: List[Any], res: List[Any]): List[Any] = l match {
        case Nil                      => res //koniec
        case (left: List[_]) :: Nil   => inner(left, res) //List(List(asd)) -> List(asd)
        case (left: List[_]) :: right => inner(right, left ::: res) //List(asd), def -> def
        case head :: tail             => inner(tail, appendLast(head, res)) //a, List(asd) -> List(asd)
      }
      inner(list, List())
    }

    flattenRec(bulgingList) should equal(flatList)
    flattenTailRec(bulgingList) should equal(flatList)
  }

  it should "eliminate duplicates" in {
    val duplicatedList = List(1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5)
    val distinctList   = List(1, 2, 3, 1, 4, 5)

    def compress[A](list: List[A]): List[A] = {
      @tailrec
      def inner(l: List[A], res: List[A]): List[A] = l match {
        case head :: Nil                       => appendLast(head, res)
        case head :: tail if head != tail.head => inner(tail, appendLast(head, res))
        case _ :: tail                         => inner(tail, res)
      }
      inner(list, List())
    }

    def compressFold[A](list: List[A]): List[A] = list.foldLeft(List[A]()) {
      case (acc, e) if acc.isEmpty || acc.last != e => appendLast(e, acc)
      case (acc, _)                                 => acc
    }

    compress(duplicatedList) should equal(distinctList)
    compressFold(duplicatedList) should equal(distinctList)
  }

  it should "pack consecutive duplicates of list elements into sublists" in {
    val unpacked = List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
    val packed   = List(List('a, 'a, 'a, 'a), List('b), List('c, 'c), List('a, 'a), List('d), List('e, 'e, 'e, 'e))

    def pack[A](list: List[A]): List[List[A]] = {
      @tailrec
      def inner(l: List[A], res: List[List[A]]): List[List[A]] = l match {
        case Nil => res
        case _ => {
          val (s: List[A], r: List[A]) = l span { _ == l.head }
          inner(r, res ::: List(s))
        }
      }
      inner(list, List())
    }

    pack(unpacked) should equal(packed)
  }

}
