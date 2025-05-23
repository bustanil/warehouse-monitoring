package com.bustanil.warehouse.service;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.netty.udp.UdpServer;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class UdpListener {

    public static final Logger logger = LoggerFactory.getLogger(UdpListener.class);

    public Disposable listen(int port, Function<String, Publisher<?>> handler){
        return UdpServer.create()
                .port(port)
                .handle((in, out) ->
                        in.receive()
                                .asString(StandardCharsets.UTF_8)
                                .flatMap(handler)
                                .onErrorContinue((t, o) -> {
                                    logger.error("Error in udp listener", t);
                                })
                                .then()
                )
                .bind()
                .doOnSuccess(server ->
                        logger.info("UDP server started on port {}", port)
                )
                .doOnError(throwable -> logger.error("Error in udp listener", throwable))
                .subscribe();
    }

}
