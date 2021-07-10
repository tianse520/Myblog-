package com.liu.dao;

import com.liu.pojo.Tag;
import com.liu.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tianse
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag getTagById(Long id);

    Tag findByName(String name);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
