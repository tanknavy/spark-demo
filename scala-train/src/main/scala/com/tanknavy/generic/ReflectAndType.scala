package com.tanknavy.generic

/**
 * Author: Alex Cheng 4/25/2021 6:35 PM
 */

/*
https://developer.aliyun.com/article/641350
Scala反射： 获取runtime时类型，通过类型实例化新对象，访问或者调用对象的方法或属性等
> typeTag[List[Int]]
> reso.tpe
> typeOf[List[Int]]
> classTag[List[String]]

TypeTag[T封装了compile-time时类型到runtime时TypeTag, 运行时类型信息保存在TypeTag对象中
ClassTag,对于List[String]类型，TypeTag[List[Sting]]返回类型是scal.List[String],
而ClassTag[List[String]]返回immutable.List
TypeTag包含全部type信息，ClassTag包含部分类型信息
 */
object ReflectAndType {
  def main(args: Array[String]): Unit = {
    paramInfo(List(1,2,3))
    paramInfo2(Seq("aa","bb")) //[T: TypeTag]，上下文绑定，产生TypeTag隐式参数
    paramInfo3(Seq(1,"bb")) //[T: TypeTag]，上下文绑定，产生TypeTag隐式参数
  }

  import scala.reflect.runtime.universe._

  //泛型(类型参数化)方法中使用了隐式参数，请求编译器产生一个TypeTag
  def paramInfo[T](x: T)(implicit tag: TypeTag[T]) = { //隐式参数
    //tpe反射表示出T的类型
    val argType = tag.tpe match {case TypeRef(_, _, args) => args}
    println(s"type of $x has type arguments $argType")
  }

  //上述方法简化，使用context bound方式[T:TypeTag]
  //compiler将产生一个TypeTag[T]类型的implicit parameter
  def paramInfo2[T: TypeTag](x: T) = {
    //tpe反射表示出T的类型
    val argType = typeOf[T] match {case TypeRef(_, _, args) => args}
    println(s"type of $x has type arguments $argType")
  }

  def paramInfo3[T: TypeTag](x: T) = {
    //tpe反射表示出T的类型
    val argType = typeOf[T]
    println(s"type of $x has type arguments $argType")
  }

}
