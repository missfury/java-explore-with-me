package ru.practicum.guest.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.guest.service.PublicCompilationService;
import ru.practicum.shared.dto.CompilationDto;
import ru.practicum.shared.exceptions.NotFoundException;
import ru.practicum.shared.mapper.CompilationMapper;
import ru.practicum.shared.model.Compilation;
import ru.practicum.shared.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        if (pinned == null) {
            return compilationRepository.findAll(pageable).stream()
                    .map(compilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAllByPinned(pinned, pageable).stream()
                    .map(compilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Подборка событий с id=" + compId + " не найдена")));

        return compilationMapper.toCompilationDto(compilation);
    }
}
