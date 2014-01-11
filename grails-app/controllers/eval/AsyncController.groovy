package eval

import com.ning.http.client.AsyncCompletionHandler
import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.AsyncHttpClientConfig
import com.ning.http.client.Response
import com.ning.http.client.providers.grizzly.GrizzlyAsyncHttpProvider
import grails.async.Promise

import static grails.async.Promises.task
import static grails.async.Promises.waitAll

class AsyncController {

    static AsyncHttpClient.BoundRequestBuilder preparedGet
    static {
        AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().build();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(new GrizzlyAsyncHttpProvider(config), config);
        preparedGet = asyncHttpClient.prepareGet("http://www.example.com/")
    }

    def async_action() {
        def promise = task {
            def p1 = task {
                log.info("p1 done")
                2 * 2
            }
            def p2 = task {
                log.info("p2 done")
                4 * 4
            }
            def p3 = task {
                log.info("p3 done")
                8 * 8
            }
            def result = waitAll(p1, p2, p3)
            log.info("waitAll done")
            render "p1: ${result[0]}, p2: ${result[1]}, p3: ${result[2]}"
        }
        log.info("async_action done")

        return promise
    }

    def sync_action() {
        def p1 = 2 * 2
        log.info("p1 done")
        def p2 = 4 * 4
        log.info("p2 done")
        def p3 = 8 * 8
        log.info("p3 done")
        render "p1: $p1, p2: $p2, p3: $p3"
    }

    def netty_async_action() {
        Promise p1 = task {
            execp1()
            log.info("p1 done")
            2 * 2
        }
        Promise p2 = task {
            execp1()
            log.info("p2 done")
            4 * 4
        }
        Promise p3 = task {
            execp1()
            log.info("p3 done")
            8 * 8
        }
        def result = waitAll(p1, p2, p3)
        log.info("waitAll done")
        render "p1: ${result[0]}, p2: ${result[1]}, p3: ${result[2]}"
    }

    def execp1() {

        preparedGet.execute(new AsyncCompletionHandler<Response>() {

            @Override
            public Response onCompleted(Response response) throws Exception {
                // Do something with the Response
                // ...
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                // Something wrong happened.
            }
        });
    }
}
