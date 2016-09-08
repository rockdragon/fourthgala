
package akka_in_action.streams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream._
import scala.collection.immutable.Seq
import GraphDSL.Implicits._

object PriorityWorkerPoolShape_ extends App {

  case class PriorityWorkerPoolShape[In, Out](jobsIn: Inlet[In],
                                              priorityJobsIn: Inlet[In],
                                              resultsOut: Outlet[Out]) extends Shape {
    override val inlets: Seq[Inlet[_]] =
      jobsIn :: priorityJobsIn :: Nil
    override val outlets: Seq[Outlet[_]] =
      resultsOut :: Nil

    // scalastyle:off
    override def deepCopy() = PriorityWorkerPoolShape(
      jobsIn.carbonCopy(),
      priorityJobsIn.carbonCopy(),
      resultsOut.carbonCopy()
    )

    override def copyFromPorts(inlets: Seq[Inlet[_]],
                               outlets: Seq[Outlet[_]]) = {
      assert(inlets.size == this.inlets.size)
      assert(outlets.size == this.outlets.size)
      PriorityWorkerPoolShape[In, Out](inlets(0).as[In], inlets(1).as[In], outlets(0).as[Out])
    }

    // scalastyle:on
  }

  object PriorityWorkerPool {
    def apply[In, Out](
                        worker: Flow[In, Out, Any],
                        workerCount: Int
                      ): Graph[PriorityWorkerPoolShape[In, Out], NotUsed] = {
      GraphDSL.create() { implicit b =>

        val priorityMerge = b.add(MergePreferred[In](1))
        val balance = b.add(Balance[In](workerCount))
        val resultMerge = b.add(Merge[Out](workerCount))

        priorityMerge ~> balance

        for (i <- 0 until workerCount)
          balance.out(i) ~> worker ~> resultMerge.in(i)

        PriorityWorkerPoolShape(
          jobsIn = priorityMerge.in(0),
          priorityJobsIn = priorityMerge.preferred,
          resultsOut = resultMerge.out
        )
      }
    }
  }

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val worker1 = Flow[String].map("step 1 " + _)
  val worker2 = Flow[String].map("step 2 " + _)

  RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    val priorityPool1 = b.add(PriorityWorkerPool(worker1, 4))
    val priorityPool2 = b.add(PriorityWorkerPool(worker2, 2))

    Source(1 to 100).map("job: " + _) ~> priorityPool1.jobsIn
    Source(1 to 100).map("priority job: " + _) ~> priorityPool1.priorityJobsIn

    priorityPool1.resultsOut ~> priorityPool2.jobsIn
    Source(1 to 100).map("one-step, priority " + _) ~> priorityPool2.priorityJobsIn

    priorityPool2.resultsOut ~> Sink.foreach(println)
    ClosedShape
  }).run()

  Thread.sleep(200)

  system.terminate()
}
