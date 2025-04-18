package com.retry.replay.RetryReplayApplication.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelConfig extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:startJob")
                .log("Executing job via Camel")
                .to("bean:retryableJob?method=execute");
    }
}

