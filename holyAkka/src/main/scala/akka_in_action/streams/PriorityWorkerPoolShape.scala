
package akka_in_action.streams

import akka.stream.{Outlet, Inlet, Shape}
import scala.collection.immutable.Seq

case class PriorityWorkerPoolShape[In, Out](jobsIn: Inlet[In],
                                            priorityJobsIn: Inlet[In],
                                            resultsOut: Outlet[Out]) extends Shape {

  override val inlets: Seq[Inlet[_]] =
    jobsIn :: priorityJobsIn :: Nil
  override val outlets: Seq[Outlet[_]] =
    resultsOut :: Nil

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
}
