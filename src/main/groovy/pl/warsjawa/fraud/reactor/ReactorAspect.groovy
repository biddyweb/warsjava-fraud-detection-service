package pl.warsjawa.fraud.reactor
import com.ofg.infrastructure.web.filter.correlationid.CorrelationIdHolder
import groovy.util.logging.Slf4j
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.MDC
import reactor.event.Event

import static com.ofg.infrastructure.web.filter.correlationid.CorrelationIdHolder.CORRELATION_ID_HEADER
import static org.springframework.util.StringUtils.hasText

//@CompileStatic
@Aspect
@Slf4j
class ReactorAspect {

    @Pointcut(value = "execution(* *(..))")
    private void anyMethod() {}

    @Pointcut(value = "@annotation(reactor.spring.annotation.Selector)")
    private void selectorAnnotated() {}

    @Pointcut("anyMethod() && selectorAnnotated()")
    private void anySelectorAnnotatedMethod() {}

    @Around('anySelectorAnnotatedMethod()')
    Object wrapWithCorrelationId(ProceedingJoinPoint pjp) throws Throwable {
        Object eventArgument = pjp.args.find { it instanceof Event }
        if (!eventArgument) {
            return pjp.proceed()
        }
        Event event = eventArgument as Event
        String correlationId = event.headers.get(CorrelationIdHolder.CORRELATION_ID_HEADER) as String
        CorrelationIdHolder.set(correlationId)
        setCorrelationIdOnMdc(correlationId)
        return pjp.proceed()
    }

    private void setCorrelationIdOnMdc(String correlationId) {
        if (hasText(correlationId)) {
            MDC.put(CORRELATION_ID_HEADER, correlationId)
            log.debug("Found correlationId in reactor event headers: $correlationId")
        }
    }
}
