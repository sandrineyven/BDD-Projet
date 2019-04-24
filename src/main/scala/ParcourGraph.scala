import java.util

import main.Node
import model.Monster
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeContext, Graph, TripletFields, VertexId}

class ParcourGraph extends Serializable {

  //Interface console
  val ANSI_RESET = s"\u001B[0m"
  val ANSI_BLACK = "\u001B[30m"
  val ANSI_RED = "\u001B[31m"
  val ANSI_GREEN = "\u001B[32m"
  val ANSI_YELLOW = "\u001B[33m"
  val ANSI_BLUE = "\u001B[34m"
  val ANSI_PURPLE = "\u001B[35m"
  val ANSI_CYAN = "\u001B[36m"
  val ANSI_WHITE = "\u001B[37m"

  //Liste des monstres en jeu
  var monsterList: util.ArrayList[Monster] = new util.ArrayList[Monster]()

  //Fonction sendMessage
  def sendMonsterMessages(ctx: EdgeContext[Node, String, Array[Monster]]): Unit = {
    // Envoi seulement si les monstres sont en vie
    if (ctx.srcAttr.monster.isAlive && ctx.dstAttr.monster.isAlive) {
      val arraySrc = Array(ctx.srcAttr.monster)
      val arrayDst = Array(ctx.dstAttr.monster)
      ctx.sendToDst(arraySrc)
      ctx.sendToSrc(arrayDst)
    }
  }

  //Fonction mergeMessage
  def mergeMonsterMessages(m1: Array[Monster], m2: Array[Monster]): Array[Monster] = {
    // Merge des deux listes
    val array = Array(m1, m2).flatten
    array
  }

  //Creation du message
  def ChooseAction(vid: VertexId, vertex: Node, monsters: Array[Monster]): Node = {
    val m = new util.ArrayList[Monster]()

    monsters.foreach(monster => {
      m.add(monster)
    })

    val chosenAction = vertex.monster.decideAction(m)

    //S'il décide de bouger
    if (chosenAction.equals("move")) {
      val newPosition = vertex.monster.getNewPosition(monsterList)
      vertex.messagesToTreat += new Message("move", newPosition(0), newPosition(1), newPosition(2))
      println(" Monster " + vertex.monster + ANSI_PURPLE + " WILL MOVE TO (" + newPosition(0) + " ; " + newPosition(1) + " ; " + newPosition(2) + ")" + ANSI_RESET)
      vertex.monster.move(newPosition)
      new Node(vertex.id, vertex.monster, vertex.messagesToTreat)
    }
    //S'il décide d'attaquer
    else {
      val monsters = vertex.monster.detectEnemies(monsterList)
      for (i <- 0 until monsters.size()) {
        val monsterEnemy = monsters.get(i)
        val damage = vertex.monster.damage(monsterEnemy)
        vertex.messagesToTreat += new Message("attack", vertex.monster.id, monsterEnemy.id, damage)
        println("Monster " + vertex.monster + ANSI_RED + " WIIL ATTACK " + monsterEnemy + " : damage = " + damage + ANSI_RESET)
        if (damage == 0) {
          println(ANSI_RED + " BUT " + vertex.monster.name + "  MISSED " + monsterEnemy.name + "... damage = " + damage + ANSI_RESET)
        }
      }
      new Node(vertex.id, vertex.monster, vertex.messagesToTreat)
    }

  }

  //Applique les consequence de choix de l'action
  def performActions(vid: VertexId, vertex: Node, mergedMessages: List[Message]): Node = {

    mergedMessages.foreach(message => {
      if (message.actionToPerform.equals("move")) {
        val newPosition = Array[Double](message.x, message.y, message.z)
        vertex.monster.move(newPosition)
      }
      else if (message.actionToPerform.equals("isAttacked")) {
        vertex.monster.isAttacked(message.z.toInt)
        println("Monster " + vertex.monster + ANSI_RED + " HAS BEEN ATTACK - DAMAGES = " + message.z.toInt + ANSI_RESET)
      }

    })

    vertex.messagesToTreat.dequeueAll(message => true)

    new Node(vertex.id, vertex.monster, vertex.messagesToTreat)
  }

  //Construction du message d'action
  def sendActionMessages(ctx: EdgeContext[Node, String, List[Message]]): Unit = {
    //Source
    if (ctx.srcAttr.monster.isAlive) {
      ctx.srcAttr.messagesToTreat.foreach(message => {
        if (message.actionToPerform == "move") ctx.sendToSrc(List(message))
        else if (message.actionToPerform == "attack") {
          if (ctx.dstAttr.monster.isAlive && message.y == ctx.dstAttr.monster.id) {
            ctx.sendToDst(List(new Message("isAttacked", message.x, message.y, message.z)))
          }
        }
      })
    }

    //Destination
    if (ctx.dstAttr.monster.isAlive) {
      ctx.dstAttr.messagesToTreat.foreach(message => {
        if (message.actionToPerform.equals("move")) ctx.sendToDst(List(message))
        else if (message.actionToPerform.equals("attack")) {
          if (ctx.srcAttr.monster.isAlive && message.y == ctx.srcAttr.monster.id) {
            ctx.sendToSrc(List(new Message("isAttacked", message.x, message.y, message.z)))
          }
        }
      })
    }
  }

  def mergeActionMessages(messages1: List[Message], messages2: List[Message]): List[Message] = {

    val moveMessages1 = messages1.filter(msg => msg.actionToPerform.equals("move"))
    var moves = List[Message]()
    if (moveMessages1.nonEmpty) {
      moves = List(moveMessages1.head)
    }
    else {
      val moveMessages2 = messages2.filter(msg => msg.actionToPerform.equals("move"))
      if (moveMessages2.nonEmpty) {
        moves = List(moveMessages2.head)
      }
    }

    //Concatenation de tous les dégats subits
    val isAttackedMessages1 = messages1.filter(msg => msg.actionToPerform.equals("isAttacked"))
    val isAttackedMessages2 = messages2.filter(msg => msg.actionToPerform.equals("isAttacked"))
    var totalDamage = 0.0
    isAttackedMessages1.foreach(msg => totalDamage += msg.z.toDouble)
    isAttackedMessages2.foreach(msg => totalDamage += msg.z.toDouble)

    var isAttacked = List[Message]()

    if (isAttackedMessages1.nonEmpty || isAttackedMessages2.nonEmpty) {
      isAttacked = List(new Message("isAttacked", 0, 0, totalDamage))
    }

    if (moves.nonEmpty && isAttacked.nonEmpty) {
      List(moves, isAttacked).flatten
    }
    else if (moves.isEmpty && isAttacked.nonEmpty) {
      List(isAttacked).flatten
    }
    else if (moves.nonEmpty && isAttacked.isEmpty) {
      List(moves).flatten
    }
    else {
      List(moves).flatten
    }

  }


  def execute(g: Graph[Node, String], maxIterations: Int, sc: SparkContext): Graph[Node, String] = {
    //Choix de strategie: Pattern 1
    var myGraph = g
    var counter = 0
    val fields = new TripletFields(true, true, false)

    println()
    println("INITIAL GRAPH")
    myGraph.vertices.collect.foreach(vertex => {
      val monster = vertex._2.monster
      println(monster.toString)
    })
    println()


    def loop: Unit = {
      while (true) {


        monsterList.clear()
        //Recuperation des monstres pour l'intialisation
        val allVertices = myGraph.vertices.collect()
        allVertices.foreach(vertex => {
          monsterList.add(vertex._2.monster)
        })

        println("ITERATION N° " + (counter + 1))
        counter += 1
        if (counter == maxIterations) return

        val messages = myGraph.aggregateMessages[Array[Monster]](
          sendMonsterMessages,
          mergeMonsterMessages,
          fields
        )

        if (messages.isEmpty()) {
          println("message is empty");
          return
        }


        myGraph = myGraph.joinVertices(messages)(
          (vid, vertex, enemyMobs) => ChooseAction(vid, vertex, enemyMobs))

        val attackMessages = myGraph.aggregateMessages[List[Message]](
          sendActionMessages,
          mergeActionMessages,
          fields
        )

        myGraph = myGraph.joinVertices(attackMessages)(
          (vid, vertex, totalDamage) => performActions(vid, vertex, totalDamage)
        )

        var printedGraph = myGraph.vertices.collect()
        printedGraph = printedGraph.sortBy(_._1)
      }

    }

    loop //Execute la boucle de jeu
    myGraph
  }
}
