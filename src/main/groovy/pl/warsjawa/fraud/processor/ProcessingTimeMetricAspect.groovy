package pl.warsjawa.fraud.processor
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Timer as TimerMetric
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import reactor.spring.annotation.Selector

import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap

@CompileStatic
@Aspect
@Slf4j
class ProcessingTimeMetricAspect {

    private final MetricRegistry metricRegistry
    private final Map<String, TimerMetric> timers = new ConcurrentHashMap<>()

    ProcessingTimeMetricAspect(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry
    }

    @Pointcut("execution(public void pl.warsjawa.fraud.processor.ClientProcessing.subscribeForFraudEvent(..))")
    private void anyClientProcessingMethod() {}

    @Pointcut("@annotation(selector)")
    private void selectorAnnotated(Selector selector) {}

    @Pointcut("anyClientProcessingMethod() && selectorAnnotated(selector)")
    private void anyClientProcessingSelectorAnnotatedMethod(Selector selector) {}

    @Around('anyClientProcessingSelectorAnnotatedMethod(selector)')
    Object calculateProcessingTime(ProceedingJoinPoint pjp, Selector selector) throws Throwable {
        String selectorKey = selector.value()
        TimerMetric timer = timers.putIfAbsent(selectorKey, metricRegistry.timer(selectorKey)) ?: timers[selectorKey]
        return timer.time {
            pjp.proceed()
        } as Callable<Void>
    }
}
