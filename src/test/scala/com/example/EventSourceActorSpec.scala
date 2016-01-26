package com.example

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ TestActors, TestKit, ImplicitSender, TestActorRef }
import org.scalatest.WordSpecLike
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll

class TestEventSource extends Actor with EventSource {
    def receive = eventSourceReceive
}

class EventSourceSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with MustMatchers with BeforeAndAfterAll {

      import EventSource._

      def this() = this(ActorSystem("EventSourceSpec"))

      override def afterAll {
          TestKit.shutdownActorSystem(system)
      }

      "Event Source" should {
          "allow us to register a listener" in {
              val a = TestActorRef[TestEventSource]
              a ! RegisterListener(testActor)
              a.underlyingActor.listeners must contain (testActor)
          }

           "allow us to unregister a listener" in {
              val a = TestActorRef[TestEventSource]
              a ! RegisterListener(testActor)
              a ! UnregisterListener(testActor)
              a.underlyingActor.listeners.size must be (0)
          }
          "send event to the test actor" in {
              val a = TestActorRef[TestEventSource]
              a ! RegisterListener(testActor)
              a.underlyingActor.notify("hello")
              expectMsg("hello")
          }
      }
  }
