//Structure du message
//pour MOVE :
// x,y,z les positions des monstres pour move
//pour ATTACK :
// z les dégats des monstres
// x l'id du monstre qui attaque
// y l'id du monstre attaqué

class Message(val actionToPerform: String,
              val x: Double,
              val y: Double,
              val z: Double) extends Serializable {
  override def toString: String = s"actionToPerform : $actionToPerform value1 : $x value2 : $y value3 : $z"
}