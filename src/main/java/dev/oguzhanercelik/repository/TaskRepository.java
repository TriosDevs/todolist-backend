package dev.oguzhanercelik.repository;

import dev.oguzhanercelik.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByListId(Integer listId);
    Optional<Task> findByIdAndUserId(Integer id, Integer userId);
    void deleteByIdAndUserId(Integer id, Integer userId);
    Integer countByListIdAndUserId(Integer listId, Integer userId);

}
