package com.metrics.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * REST controller that demonstrates various metrics functionalities using Micrometer.
 * Provides endpoints for high latency simulation, custom metrics, and simple counters.
 */
@RestController
public class MetricsController {

    private MeterRegistry registry;
    private AtomicLong custom;

    /**
     * Constructor for RestController.
     *
     * @param registry MeterRegistry instance used for registering metrics.
     */
    public MetricsController(MeterRegistry registry) {
        this.registry = registry;
        this.custom = new AtomicLong(0L);
    }

    /**
     * Endpoint that simulates high latency.
     *
     * @return A ResponseEntity with an HTTP status of OK after a 2-second delay.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */

    @GetMapping("/high_latency")
    public ResponseEntity<String> highLatency() throws InterruptedException {
        Thread.sleep(2000);
        return new ResponseEntity<>("{}", null, HttpStatus.OK);
    }

    /**
     * Endpoint for manipulating a custom gauge metric.
     *
     * @param increment Optional query parameter to increment or decrement the custom gauge.
     *                  If the parameter is present and not empty, the gauge is incremented.
     *                  Otherwise, the gauge is decremented.
     * @return A ResponseEntity with an HTTP status of OK.
     */
    @GetMapping("/custom_metric")
    public ResponseEntity<String> customMetric(@RequestParam(value="inc", defaultValue="") String increment) {
        AtomicLong customGauge = registry.gauge("custom", this.custom);
        if (!"".equals(increment)) {
            customGauge.incrementAndGet();
        } else {
            customGauge.decrementAndGet();
        }
        
        return new ResponseEntity<>("{}", null, HttpStatus.OK);
    }

    /**
     * Endpoint that increments a simple counter metric.
     *
     * @return A ResponseEntity with an HTTP status of OK.
     */
    @GetMapping("/simple")
    public ResponseEntity<String> simple() {
        registry.counter("simple").increment();
        return new ResponseEntity<>("{}", null, HttpStatus.OK);
    }
}