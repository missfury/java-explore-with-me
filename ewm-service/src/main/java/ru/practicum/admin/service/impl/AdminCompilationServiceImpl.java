package ru.practicum.admin.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.service.AdminCompilationService;
import ru.practicum.shared.dto.CompilationDto;
import ru.practicum.shared.dto.NewCompilationDto;
import ru.practicum.shared.dto.UpdateCompilationRequest;
import ru.practicum.shared.exceptions.NotFoundException;
import ru.practicum.shared.mapper.CompilationMapper;
import ru.practicum.shared.model.Compilation;
import ru.practicum.shared.model.Event;
import ru.practicum.shared.repository.CompilationRepository;
import ru.practicum.shared.repository.EventRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Long> eventIds = newCompilationDto.getEvents();
        Set<Event> events = new HashSet<>(eventRepository.findEventsByIds(eventIds));
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);

        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        } else {
            compilation.setPinned(false);
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Подборки с id %s не существует", compId)));

        if (updateCompilationRequest.getTitle() != null)
            compilation.setTitle(updateCompilationRequest.getTitle());

        if (updateCompilationRequest.getPinned() != null)
            compilation.setPinned(updateCompilationRequest.getPinned());

        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            List<Long> eventIds = updateCompilationRequest.getEvents();
            Collection<Event> events = eventRepository.findEventsByIds(eventIds);
            compilation.setEvents(new HashSet<>(events));
        }
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Подборки с id %s не существует", compId)));
        compilationRepository.delete(compilation);
    }

}
