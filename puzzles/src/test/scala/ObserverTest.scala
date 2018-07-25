import org.scalatest.{ FlatSpec, Matchers }

import scala.collection.mutable.ArrayBuffer

class ObserverTest extends FlatSpec with Matchers {

  trait Observer {
    def update(state: Boolean): Unit
  }

  object Obs1 extends Observer {
    var state: Boolean                        = false
    override def update(state: Boolean): Unit = this.state = state
  }

  object Obs2 extends Observer {
    var state: Boolean                        = false
    override def update(state: Boolean): Unit = this.state = state
  }

  object Subject {
    val observers: ArrayBuffer[Observer] = new ArrayBuffer[Observer]()

    def subscribe(observer: Observer): Unit   = observers.append(observer)
    def unsubscribe(observer: Observer): Unit = observers - observer
    def action(state: Boolean): Unit          = observers.foreach(_.update(state))
  }

  "Subject" should "notify observers of its changed state" in {
    val change = true
    Subject.subscribe(Obs1)
    Subject.subscribe(Obs2)
    Subject.action(change)
    Obs1.state should be(change)
    Obs2.state should be(change)
  }
}
