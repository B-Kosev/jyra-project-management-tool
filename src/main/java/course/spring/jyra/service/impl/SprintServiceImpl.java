package course.spring.jyra.service.impl;

import course.spring.jyra.dao.*;
import course.spring.jyra.exception.EntityNotFoundException;
import course.spring.jyra.model.*;
import course.spring.jyra.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SprintServiceImpl implements SprintService {
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;

    @Autowired
    public SprintServiceImpl(SprintRepository sprintRepository, UserRepository userRepository) {
        this.sprintRepository = sprintRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Sprint> findAll() {
        return sprintRepository.findAll();
    }

    @Override
    public Sprint findById(Integer id) {
        return sprintRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with ID=%s not found.", id)));
    }

    @Override
    public Sprint findByTitle(String title) {
        return sprintRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException(String.format("Sprint with title=%s not found.", title)));
    }

    @Override
    public Sprint create(Sprint sprint) {
        sprint.setId(null);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new EntityNotFoundException(String.format("User with username=%s could not be found", auth.getName())));
            sprint.setOwner(user);
        }

        sprint.setCreated(LocalDateTime.now());
        sprint.setModified(LocalDateTime.now());
        sprint.calculateDuration();
        return sprintRepository.save(sprint);
    }

    @Override
    public Sprint update(Sprint sprint) {
        Sprint oldSprint = findById(sprint.getId());
        sprint.setCreated(oldSprint.getCreated());
        sprint.setModified(LocalDateTime.now());
        return sprintRepository.save(sprint);
    }

    @Override
    public Sprint deleteById(Integer id) {
        Sprint oldSprint = findById(id);
        sprintRepository.deleteById(id);
        return oldSprint;
    }

    @Override
    public long count() {
        return sprintRepository.count();
    }
}
