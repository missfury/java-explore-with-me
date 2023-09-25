package ru.practicum.ewmservice.shared.dto;

import ru.practicum.ewmservice.shared.util.enums.RequestStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    private RequestStatus status;
}
