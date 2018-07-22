package com.example

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
    val testProbe         = TestProbe()
    val producerActorName = "testProducer"
    val producer          = system.actorOf(Props(new Producer(testProbe.ref, 1, 10, 20)), producerActorName)
    producer ! "start"
    testProbe.expectMsg("0 from " + producerActorName)
  }
}
