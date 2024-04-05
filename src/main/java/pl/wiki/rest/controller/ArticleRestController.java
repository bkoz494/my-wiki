package pl.wiki.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wiki.dao.ArticleRepository;
import pl.wiki.model.Article;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ArticleRestController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles")
    public List<Article> getArticles(){
        return articleRepository.findAll();
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable("id") Long id){
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isEmpty()){
            return null;
        }
        return optionalArticle.get();
    }

    /*@PostMapping
    public Article createArticle(@RequestBody Article article){
        return articleRepository.save(article);
    }*/
}
