package pl.wiki.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.wiki.model.Article;
import pl.wiki.service.ArticleService;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
public class ArticleRestController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    public List<Article> getArticles(){
        return articleService.getAll();
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable("id") Long id){
        return articleService.get(id);
    }

    @PostMapping("/articles")
    public Article createArticle(@RequestBody Article article){
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setAuthorId((String) user.getClaims().get("sub"));
        return articleService.save(article);
    }

    @PutMapping("/articles")
    public Article updateArticle(@RequestBody Article article){
        Article article1;
        article1 = articleService.get(article.getId());
        if(article1 != null){
            return articleService.save(article);
        } else {
            return null;
        }
    }

    @DeleteMapping("/articles/{id}")
    public void deleteArticle(@PathVariable("id") Long id){
        articleService.delete(id);
    }
}
