import java.util
import java.util.Properties

import com.typesafe.config.ConfigFactory

/**
 * Author: Alex Cheng 3/26/2021 4:41 PM
 */

object TestScala2 {

  private val aa = "aa"
  private var vaultUtil = None

  def main(args: Array[String]): Unit = {
//    val video_type = null
//    val partner_name = "podS"
//    val checkList = Seq[String]("pod", "news")
//    var hedge = 0.0
//    if (video_type == "LIVE" && checkList.contains(partner_name)) {
//      hedge = 0.4}
//    else if (video_type == "LIVE"){
//      hedge = 0.2
//    }else{
//      hedge = 0.3
//    }
//
//    println(s"hedge: = $hedge")
//
//    val env = getEnv("stage")
//    println(env._2)
//
//    println(getArgs("alex"))
//    println(getArgs("bob",20))
//
//    println((VaultEnv.valueOf("PROD").isDefined))
//    println((VaultEnv.valueOf("PRODs").isDefined))

    val config = ConfigFactory.load()
    //println(config)
    println("env---------------------------------------------------------------")
    val env: util.Map[String, String] = System.getenv()
    println(env)
    println("properties---------------------------------------------------------------")
    val properties: Properties = System.getProperties
    println(properties)

    println("reflect--------------------------")
    classOf[Person].getDeclaredFields.foreach{ field => field.setAccessible(true); println(field.getName)}

    println(List.apply(classOf[People].getDeclaredFields.map(f => f.getName) : _*))
    classOf[People].getDeclaredFields.foreach(f => println(f.getName))
}

  def null2BlackString(str: String) :String = {
    if(str == null) "" else str
  }

  def getEnv(env: String) : (String, String, String) = {
    env match {
      case "prod" => ("pa","pb","pc")
      case "stage" => ("sa","sb","sc")
      case _ => ("a","b","c")
    }
  }

  def getArgs(name:String, age:Int = 10): String = {
    name + age.toString
  }

  object VaultEnv extends Enumeration{
    type VaultEnv = Value
    val PROD, STAGINIG = Value

    def valueOf(name:String):Option[this.Value]={
      this.values.find(_.toString == name)
    }
  }


  //env vs properties


}

class Person(name:String,years:Int){}

case class People(name:String,years:Int)