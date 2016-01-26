package com.example

/**
 * @author yy
 */
import akka.actor.{Actor, ActorRef, ActorLogging}

object ControlSurfaces {
    case class StickBack(amount: Float)
    case class StickForward(amount: Float)
    case class AltitudeUpdate(altitude: Double)
}

class ControlSurfaces(altimeter :ActorRef) extends Actor with ActorLogging{
   import Altimeter._
   import ControlSurfaces._
   import EventSource._

   override def preStart() {
       altimeter ! RegisterListener(self)
   }

   def receive = {
       case StickBack(amount) => {
           altimeter ! RateChange(1 * amount)
       }
       case StickForward(amount) => {
           altimeter ! RateChange(-1 * amount)
       }

       case AltitudeUpdate(altitude) => {
           log.info(s"altitude now is $altitude")
       }
   } 
}
