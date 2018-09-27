package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Prototype
import io.micronaut.context.annotation.Replaces
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.*
import spock.mock.DetachedMockFactory

import javax.inject.Singleton

class MathServiceSpec extends Specification {

    @AutoCleanup
    @Shared
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    // 1. Mock from Mock() method of Specification works as expected
    MathService mathService = Mock(MathService)

    // 2. Mock from DetachedMockFactory doesn't work
//    MathService mathService = embeddedServer
//            .applicationContext
//            .getBean(MathService)

    @Unroll
    void "should compute #num to #square"() {
        given:
//        mathService.compute(_) >> { Math.pow(num, 2) }

        when:
        def result = mathService.compute(num)

        then:
        1 * mathService.compute(_) >> { Math.pow(num, 2) } // has been placed in then: block in order to see possible other invocations ; could be placed in given: block
        result == square

        where:
        num || square
        2   || 4
        3   || 9
    }

    @Factory
    static class MockFactory {
        def mockFactory = new DetachedMockFactory()

        @Singleton
        @Replaces(MathServiceImpl)
        MathService mockMathService() {
            return mockFactory.Mock(MathService)
        }
    }
}
