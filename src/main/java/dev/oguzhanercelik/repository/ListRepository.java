package dev.oguzhanercelik.repository;

import dev.oguzhanercelik.entity.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListRepository extends JpaRepository<List, Integer> {

    java.util.List<List> findByUserId(Integer userId);
    Optional<List> findById(Integer id);
    boolean existsByIdAndUserId(Integer id, Integer userId);
    Integer countByUserId(Integer userId);

}
