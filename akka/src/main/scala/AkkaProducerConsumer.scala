import java.util.concurrent.TimeUnit

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.pattern.ask
import akka.routing.RoundRobinGroup
import akka.util.Timeout

import scala.collection.immutable
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

object AkkaProducerConsumer extends App {

  val maxCount: Int       = 100
  val numProducers: Int   = 5
  val numConsumers: Int   = 3
  val minDelayTimeMs: Int = 20
  val maxDelayTimeMs: Int = 500

  implicit private val timeout: Timeout = new Timeout(
    new FiniteDuration(maxDelayTimeMs * maxCount, TimeUnit.MILLISECONDS))

  val routingAlgorithm: RoundRobinGroup.type = RoundRobinGroup

  val system: ActorSystem = ActorSystem("akkaPK")

  val consumerPaths: immutable.Seq[String] =
    (1 to numConsumers).map(i => system.actorOf(Consumer.props, "consumer" + i).path.toString)
  val consumerPool: ActorRef = system.actorOf(Props.empty.withRouter(routingAlgorithm(consumerPaths)), "pool")
  val producers: immutable.Seq[ActorRef] = (1 to numProducers).map(i =>
    system.actorOf(Producer.props(consumerPool, maxCount, minDelayTimeMs, maxDelayTimeMs), "producer" + i))

  val results: immutable.Seq[Future[String]] = producers.map(producer => ask(producer, "Start").mapTo[String])
  while (!results.forall(i => i.isCompleted)) Thread.sleep(maxDelayTimeMs / 10)
  system.terminate
}
