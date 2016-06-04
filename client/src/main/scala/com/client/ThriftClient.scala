package com.client

import com.example.ping.thriftscala.PingService
import com.twitter.finagle._
import com.twitter.finagle.param.{Tracer => PTracer}
import com.twitter.finagle.stats.NullStatsReceiver
import com.twitter.finagle.tracing._
import com.twitter.util.{Await, Future}

import scala.language.reflectiveCalls


object ThriftClient {

  val svc: PingService[Future] = ThriftMux.client
      .configured(param.Tracer(NullTracer))
      .configured(param.Stats(NullStatsReceiver))
      .newIface[PingService.FutureIface]("localhost:9999")

  def main(args: Array[String]) {

    Await.ready(svc.ping()).onSuccess( f => {
      println(s"ping response: ${f.toString}")
    }).onFailure( err => {
      println(s"error: ${err.getMessage}")
    })
  }
}
