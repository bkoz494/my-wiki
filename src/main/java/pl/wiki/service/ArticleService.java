package pl.wiki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wiki.dao.ArticleRepository;
import pl.wiki.model.Article;
import pl.wiki.model.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepo;

    public Article save(Article article){
        return articleRepo.save(article);
    }

    public Article get(Long id){
        Optional<Article> optionalArticle = articleRepo.findById(id);
        return optionalArticle.orElseThrow();
    }

    public List<Article> getAll(){
        return articleRepo.findAll();
    }

    public void delete(Long id){
        articleRepo.deleteById(id);
    }

    public Article addTagsToArticle(Article article, Set<Tag> tags){
        article.addTags(tags);
        return articleRepo.save(article);
    }

    public Article getArticleWithTags(Long articleId) {
        return articleRepo.findByIdJoinFetchTag(articleId);
    }

    public Article updateArticle(Article article) {
        return articleRepo.save(article);
    }

    public List<Article> searchByTitle(String title){
        return articleRepo.findByTitleLike(title);
    }
}
