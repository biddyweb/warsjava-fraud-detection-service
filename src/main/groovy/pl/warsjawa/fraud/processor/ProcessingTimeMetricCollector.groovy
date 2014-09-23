package pl.warsjawa.fraud.processor

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Timer as MetricTimer
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j

import java.util.concurrent.Callable

@Slf4j
@PackageScope
class ProcessingTimeMetricCollector {

    private final MetricTimer processingTimeMetric

    ProcessingTimeMetricCollector(MetricRegistry metricRegistry, String metricName) {
        this.processingTimeMetric = metricRegistry.timer(metricName)
    }

    void executeAndMeasureTime(Closure closure) {
        processingTimeMetric.time({
            closure.call()
        } as Callable<Void>)
    }
}
