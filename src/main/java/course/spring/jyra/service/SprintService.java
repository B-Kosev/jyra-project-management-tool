package course.spring.jyra.service;


import course.spring.jyra.model.Sprint;

import java.util.List;

public interface SprintService {
    List<Sprint> findAll();

    Sprint findById(Integer id);

    Sprint findByTitle(String title);

    Sprint create(Sprint sprint);

    Sprint deleteById(Integer id);

    Sprint update(Sprint sprint, Integer oldId);

    Sprint update(Sprint sprint);

    List<Sprint> findBySearch(String keywords);

    long count();
}
