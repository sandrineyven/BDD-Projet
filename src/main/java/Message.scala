class Message(val actionToPerform:String,
              val x:Double,
              val y:Double,
              val z:Double) extends Serializable
{
  override def toString: String = s"actionToPerform : $actionToPerform value1 : $x value2 : $y value3 : $z"
}