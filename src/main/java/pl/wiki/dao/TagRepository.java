package pl.wiki.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiki.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
