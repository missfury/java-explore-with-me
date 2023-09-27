package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.time.LocalDateTime;


@Service
@Slf4j
@Configuration
@ComponentScan
public class StatsClient {
    private final WebClient client;

    public StatsClient(@Value("${stats.server.url}") String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public void callEndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        final StatsHitDto endpointHit = new StatsHitDto(app, uri, ip, timestamp);
        this.client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(endpointHit)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public ResponseEntity<List<StatsViewDto>> callEndpointStats(String start, String end, String[] uris,
                                                                Boolean isUnique) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", isUnique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(StatsViewDto.class)
                .block();
    }
}
