package com.tanknavy.oop_trait

/**
 * Author: Alex Cheng 6/15/2021 12:39 PM
 */

trait ValveApp {
  def id: Int
  def appName: String
  def appRenderOptions: Map[String, String]
}


case class ValveAppSpark(id:Int, appName:String) extends ValveApp{
  override def appRenderOptions: Map[String, String] = Map("streaming_queue" -> "yarn_up", "env" -> "prod")
}

case class ValveAppFlink(id:Int, appName:String) extends ValveApp{
  override val appRenderOptions: Map[String, String] = Map("streaming_queue" -> "yarn_up", "env" -> "prod")
}