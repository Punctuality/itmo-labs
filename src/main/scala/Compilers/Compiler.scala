package Compilers

trait Compiler {
  def add(obj: String): Unit
  def compile(): Unit
  def getResult: Any
}
