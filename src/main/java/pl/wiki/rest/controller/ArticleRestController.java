package pl.wiki.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pl.wiki.model.Article;
import pl.wiki.model.Tag;
import pl.wiki.service.ArticleService;
import pl.wiki.service.TagService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class ArticleRestController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;

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

    @PutMapping("/articles/{id}/tags/add")
    public Article addTagsToArticle(@PathVariable("id") Long id, @RequestBody List<Long> tagsIds){
        Set<Tag> tags = tagService.getAllByIds(tagsIds);
        Article article = articleService.get(id);
        return articleService.addTagsToArticle(article, tags);
    }
}
