package pl.wiki.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wiki.dao.ArticleRepository;
import pl.wiki.model.Article;

import java.util.List;
import java.util.Optional;

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
}
