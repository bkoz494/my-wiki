package pl.wiki.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.wiki.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a left join fetch a.tags where a.id = :id")
    Article findByIdJoinFetchTag(@Param("id") Long articleId);
}
