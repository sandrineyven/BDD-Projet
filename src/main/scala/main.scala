import model._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.{Edge, EdgeContext, Graph, _}

import scala.collection.mutable


object main extends App {

  val conf = new SparkConf().setAppName("Fight").setMaster("local[*]")
  val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")

  class Node(val id: Int,
             val monster: Monster,
             val messagesToTreat: mutable.Queue[Message] = mutable.Queue()) extends Serializable {
    override def toString: String = s"id : $id Monster : $monster"
  }

  val monstersList = new java.util.ArrayList[Monster]()

  //Initialisation des monstres
  val solar = new Solar(1L,monstersList)
  monstersList.add(solar)
  val worgsRider1 = new WorgsRider(2L,monstersList)
  monstersList.add(worgsRider1)
  val worgsRider2 = new WorgsRider(3L,monstersList)
  monstersList.add(worgsRider2)
  val worgsRider3 = new WorgsRider(4L,monstersList)
  monstersList.add(worgsRider3)
  val worgsRider4 = new WorgsRider(5L,monstersList)
  monstersList.add(worgsRider4)
  val worgsRider5 = new WorgsRider(6L,monstersList)
  monstersList.add(worgsRider5)
  val worgsRider6 = new WorgsRider(7L,monstersList)
  monstersList.add(worgsRider6)
  val worgsRider7 = new WorgsRider(8L,monstersList)
  monstersList.add(worgsRider7)
  val worgsRider8 = new WorgsRider(9L,monstersList)
  monstersList.add(worgsRider8)
  val worgsRider9 = new WorgsRider(10L,monstersList)
  monstersList.add(worgsRider9)
  val warlord = new Warlord(11L,monstersList)
  monstersList.add(warlord)
  val barbaresOrc1 = new BarbareOrc(12L,monstersList)
  monstersList.add(barbaresOrc1)
  val barbaresOrc2 = new BarbareOrc(13L,monstersList)
  monstersList.add(barbaresOrc2)
  val barbaresOrc3 = new BarbareOrc(14L,monstersList)
  monstersList.add(barbaresOrc3)
  val barbaresOrc4 = new BarbareOrc(15L,monstersList)
  monstersList.add(barbaresOrc4)

  // Creation des sommets
  var myVertices = sc.makeRDD(Array(
    (1L, new Node(id = 1, monster = solar)),
    (2L, new Node(id = 2, monster = worgsRider1)),
    (3L, new Node(id = 3, monster = worgsRider2)),
    (4L, new Node(id = 4, monster = worgsRider3)),
    (5L, new Node(id = 5, monster = worgsRider4)),
    (6L, new Node(id = 6, monster = worgsRider5)),
    (7L, new Node(id = 7, monster = worgsRider6)),
    (8L, new Node(id = 8, monster = worgsRider7)),
    (9L, new Node(id = 9, monster = worgsRider8)),
    (10L, new Node(id = 10, monster = worgsRider9)),
    (11L, new Node(id = 11, monster = warlord)),
    (12L, new Node(id = 12, monster = barbaresOrc1)),
    (13L, new Node(id = 13, monster = barbaresOrc2)),
    (14L, new Node(id = 14, monster = barbaresOrc3)),
    (15L, new Node(id = 15, monster = barbaresOrc4))
  ))

  // Creation des arretes
  var myEdges = sc.makeRDD(Array(
    Edge(1L, 2L, "1"), Edge(1L, 3L, "2"), Edge(1L, 4L, "3"),
    Edge(1L, 5L, "4"), Edge(1L, 6L, "5"), Edge(1L, 7L, "6"),
    Edge(1L, 8L, "7"), Edge(1L, 9L, "8"), Edge(1L, 10L, "9"),
    Edge(1L, 11L, "10"), Edge(1L, 12L, "11"), Edge(1L, 13L, "12"),
    Edge(1L, 14L, "13"), Edge(1L, 15L, "14")
  ))

  var myGraph = Graph(myVertices, myEdges)
  val algoColoring = new ParcourGraph()
  val res = algoColoring.execute(myGraph, 50, sc)

}
