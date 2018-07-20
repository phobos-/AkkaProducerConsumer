package com.example

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorRef, Props}

import scala.util.Random

class Producer(pool: ActorRef, maxCount: Int, minDelayTimeMs: Int, maxDelayTimeMs: Int) extends Actor {

  val counter = new AtomicInteger(0)
  val r = new Random()

  override def receive: Receive = {
    case _ =>
      while(counter.get < maxCount) {
        Thread.sleep(minDelayTimeMs + r.nextInt(maxDelayTimeMs - minDelayTimeMs))
        pool ! counter.getAndIncrement + " from " + self.path.name
      }
      sender() ! "finished"
      context.stop(self)
  }
}

object Producer {
  def props(pool: ActorRef, maxCount: Int, minDelayTimeMs: Int, maxDelayTimeMs: Int): Props =
    Props(new Producer(pool, maxCount, minDelayTimeMs, maxDelayTimeMs))
}
