import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ TestKit, TestProbe }
import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers }

class AkkaProducerConsumerTest(_system: ActorSystem)
    extends TestKit(_system)
    with Matchers
    with FlatSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("AkkaQuickstartSpec"))

  override def afterAll: Unit =
    shutdown(system)

  "A Producer Actor" should "produce message" in {
    val maxCount          = 1
    val minDelay          = 10
    val maxDelay          = minDelay * 2
    val testProbe         = TestProbe()
    val producerActorName = "testProducer"
    val producer          = system.actorOf(Props(new Producer(testProbe.ref, maxCount, minDelay, maxDelay)), producerActorName)
    producer ! "start"
    testProbe.expectMsg("0 from " + producerActorName)
  }
}
