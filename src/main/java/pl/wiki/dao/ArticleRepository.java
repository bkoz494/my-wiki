package pl.wiki.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiki.model.Article;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
