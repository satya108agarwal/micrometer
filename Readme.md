# Getting Started
Demonstrates how to expose endpoints with custom metrics in Spring boot so they can be reached by `metric-registrar` in Tanzu Platform for Cloud Foundry

### Micrometer:

Micrometer is a metrics collection library used in Java applications to capture and expose various metrics for monitoring purposes. It provides a way to integrate with different monitoring systems and visualize metrics.

### Prometheus Registry:

The micrometer-registry-prometheus module is a specific implementation of Micrometer's registry that exports metrics in a format that Prometheus can scrape. Prometheus is an open-source monitoring and alerting toolkit designed for reliability and scalability.

### What It Provides:
#### Metrics Export:
It enables your application to expose its metrics in a format that Prometheus can scrape. This allows Prometheus to collect and store these metrics for analysis and visualization.

#### Integration with Prometheus: 
By including this dependency, your application can seamlessly integrate with Prometheus for monitoring purposes. It helps in setting up Prometheus to scrape metrics from your application and use them for dashboards, alerting, and performance analysis.


### Usage Scenario:
#### Monitoring: 
When using this dependency, your application will expose an endpoint (usually /actuator/prometheus) that Prometheus can scrape to gather metrics.

#### Integration: 
This is useful in environments where you have Prometheus set up as part of your monitoring stack, and you want to collect metrics from your Java application to be visualized in a Prometheus-compatible dashboard (e.g., Grafana).

## Technologies used:
* Spring Boot 3.3.1
* Java 17
* Gradle
* cf cli

## Running the app locally
```bash
cd micrometer
./gradlew bootRun
```

### Push the app in TP CF Platform
```
cd micrometer
./gradlew build
cf push
```

## Endpoints
- `/simple` - Returns OK; exercises built-in Micrometer metrics
- `/high_latency` - a slow endpoint to simulate long-running requests
- `custom_metric` - increments the `custom_metric` counter
- `/actuator/prometheus` - Prometheus endpoint for metrics

## Testing in local
- launch http://localhost:8080/
- Click on any buttons on the home page
- launch http://localhost:8080/actuator/prometheus to view metrics

## Sample metrics data format to look for in local

```bash 
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/high_latency"} 1
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/high_latency"} 2.010210458
http_server_requests_seconds_count{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/simple"} 1
http_server_requests_seconds_sum{error="none",exception="none",method="GET",outcome="SUCCESS",status="200",uri="/simple"} 0.003914084
```

# Steps to Register a Custom App Metrics endpoint in TP CF Platform

## Step 1: Install the Metric Registrar CLI:
```bash
cf install-plugin -r CF-Community "metric-registrar"
```
## Step 2: Register the metrics endpoint of the app by running:
```bash
cf register-metrics-endpoint spring-metrics-registrar /actuator/prometheus --insecure
```

## Step 3: Install the Log Cache CLI by running:
```bash 
cf install-plugin -r CF-Community "log-cache"
```
Log Cache is a component of TAS for VMs that caches logs and metrics from across the platform.


## Step 4 :View the app metrics as they are emitted running this command. The --follow flag appends output as metrics are emitted.
```bash 
cf tail spring-metrics-registrar --envelope-class metrics --follow
```


## Testing in Tanzu
- launch app after you deployed in platform
- Click on any buttons on the home page
- launch http://localhost:8080/actuator/prometheus to view metrics


**Note:**
The output looks similar to the following example. The custom metric displays in the output.


```angular2html
cf tail spring-metrics-registrar --envelope-class metrics --follow | grep custom
   2024-07-22T17:13:39.59-0400 [spring-metrics-registrar/0] GAUGE custom:1.000000
```

Refer: https://docs.vmware.com/en/VMware-Tanzu-Application-Service/6.0/tas-for-vms/autoscaler-spring-tutorial.html#sample-app-ui

## Spring boot Actuator Metrics
- JVM Metrics
- Garbage Collector Metrics
- CPU Metrics
- UP time 
- Class Loading
- HTTP Metrics
- Datasource 
- Integration


### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.3.1/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.3.1/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.1/reference/htmlsingle/index.html#web)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.3.1/reference/htmlsingle/index.html#actuator)
* [Prometheus](https://docs.spring.io/spring-boot/docs/3.3.1/reference/htmlsingle/index.html#actuator.metrics.export.prometheus)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

