package me.moye.practise.genericList

abstract class List[+T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}

class Node[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty =false
}

class Empty[T] extends List[T] {
  def isEmpty = true
  def head = throw new UnsupportedOperationException
  def tail = throw new UnsupportedOperationException
}