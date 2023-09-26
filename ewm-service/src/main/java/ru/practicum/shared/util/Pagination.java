package ru.practicum.ewmservice.shared.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Pagination extends PageRequest {
    int from;

    public Pagination(int from, int size, Sort sort) {
        super(from / size, size, sort);
        this.from = from;
    }

    @Override
    public long getOffset() {
        return from;
    }
}
