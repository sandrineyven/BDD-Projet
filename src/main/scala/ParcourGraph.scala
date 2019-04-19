import main.Node
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Graph, TripletFields}

class ParcourGraph extends Serializable {

  def execute(g: Graph[Node, String], maxIterations: Int, sc: SparkContext): Graph[Node, String] = {
    //Choix de strategie: Pattern 1
    var myGraph = g
    var counter = 0
    val fields = new TripletFields(true, true, false)

    println()
    println("INITIAL GRAPH")
    myGraph.vertices.collect.foreach(vertex =>
    {
      val monster = vertex._2.monster
      println(monster.toString)
    })
    println()


    def loop: Unit = {
      while (true) {

        println("ITERATION NUMERO : " + (counter + 1))
        counter += 1
        if (counter == maxIterations) return
        /*
                val messages = myGraph.aggregateMessages[Long](
                  sendTieBreakValues,
                  selectBest,
                  fields //use an optimized join strategy (we don't need the edge attribute)
                )

                if (messages.isEmpty()) return

                myGraph = myGraph.joinVertices(messages)(
                  (vid, sommet, bestId) => increaseColor(vid, sommet, bestId))
*/
                //Ignorez : Code de debug
                var printedGraph = myGraph.vertices.collect()
                printedGraph = printedGraph.sortBy(_._1)
                printedGraph.foreach(
                  elem => println(elem._2.toString())
                )
      }

    }

    loop //execute loop
    myGraph //return the result graph
  }
}
