package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.time.LocalDateTime;


@Service
@Slf4j
public class StatsClient {
    private static final String STATS_SERVER_BASE_URL = "http://localhost:9090";
    private final WebClient client;

    public StatsClient() {
        this.client = WebClient.create(STATS_SERVER_BASE_URL);
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
