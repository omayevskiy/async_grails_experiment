package async

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

class AsyncSimulation extends Simulation {

    val scn = scenario("Async grails experiment")
                .exec(
                    http("async_action_on_tomcat")
                    .get("http://localhost:8080/async_grails_experiment-0.1/async/netty_async_action")
                )
                /*
                .exec(
                    http("netty_async_action_on_netty")
                    .get("http://localhost:8090/async_grails_experiment/async/netty_async_action")
                )
                .exec(
                    http("sync_action")
                    .get("http://localhost:8090/async_grails_experiment/async/sync_action")
                )
                */

    setUp(scn.inject(ramp(100000 users) over (10 seconds)))
}
