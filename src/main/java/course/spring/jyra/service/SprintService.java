package course.spring.jyra.service;


import course.spring.jyra.model.Sprint;

import java.util.List;

public interface SprintService {
    List<Sprint> findAll();

    Sprint findById(Integer id);

    Sprint findByTitle(String title);

    Sprint create(Sprint sprint, Integer ownerId, Integer projectId, Integer boardId, Integer resultId);

    Sprint deleteById(Integer id);

    Sprint update(Sprint sprint, Integer boardId, Integer resultId);

    long count();
}
