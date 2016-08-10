package akka_in_action.events

import akka.event.ActorEventBus
import akka.event.{ LookupClassification, EventBus }

class OrderMessageBus extends EventBus
with LookupClassification
with ActorEventBus{
  type Event = Order
  type Classifier = Boolean
  def mapSize = 2

  protected def classify(event: OrderMessageBus#Event) = {
    event.number > 1
  }

  protected def publish(event: OrderMessageBus#Event,
                       subscriber: OrderMessageBus#Subscriber) = {
    subscriber ! event
  }
}
