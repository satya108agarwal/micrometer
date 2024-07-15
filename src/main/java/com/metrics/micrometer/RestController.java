package com.metrics.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.atomic.AtomicLong;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private MeterRegistry registry;
    private AtomicLong custom;

    public RestController(MeterRegistry registry) {
        this.registry = registry;
        this.custom = new AtomicLong(0L);
    }

    @GetMapping("/high_latency")
    public ResponseEntity<String> highLatency() throws InterruptedException {
        Thread.sleep(2000);
        return new ResponseEntity<>("{}", null, HttpStatus.OK);
    }

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

    @GetMapping("/simple")
    public ResponseEntity<String> simple() {
        registry.counter("simple").increment();
        return new ResponseEntity<>("{}", null, HttpStatus.OK);
    }
}