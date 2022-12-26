package course.spring.jyra.service;

import course.spring.jyra.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Integer id);

    User findByUsername(String username);

    User create(User user);

    User deleteById(Integer id);

    User update(User user, Integer oldId);

    User update(User user);

    String printProjects(Integer id);

    String printAssignedTasks(Integer id);

//    List<User> findBySearch(String keywords);

    long count();
}
