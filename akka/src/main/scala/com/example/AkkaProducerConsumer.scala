package com.example

import java.util.concurrent.TimeUnit

import akka.actor.{ ActorSystem, Props }
import akka.routing.RoundRobinGroup
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.FiniteDuration

object AkkaProducerConsumer extends App {

  val maxCount       = 100
  val numProducers   = 5
  val numConsumers   = 3
  val minDelayTimeMs = 20
  val maxDelayTimeMs = 500

  implicit private val timeout: Timeout = new Timeout(
    new FiniteDuration(maxDelayTimeMs * maxCount, TimeUnit.MILLISECONDS))

  val routingAlgorithm = RoundRobinGroup

  val system: ActorSystem = ActorSystem("akkaPK")

  val consumerPaths = (1 to numConsumers).map(i => system.actorOf(Consumer.props, "consumer" + i).path.toString)
  val consumerPool  = system.actorOf(Props.empty.withRouter(routingAlgorithm(consumerPaths)), "pool")
  val producers = (1 to numProducers).map(i =>
    system.actorOf(Producer.props(consumerPool, maxCount, minDelayTimeMs, maxDelayTimeMs), "producer" + i))

  val results = producers.map(producer => ask(producer, "Start").mapTo[String])
  while (!results.forall(i => i.isCompleted)) Thread.sleep(maxDelayTimeMs / 10)
  system.terminate
}
