package pl.wiki.gui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import pl.wiki.model.Article;
import pl.wiki.model.Tag;
import pl.wiki.service.ArticleService;
import pl.wiki.service.TagService;

import java.util.List;
import java.util.Set;

@Controller
public class ArticlesController {
    @Autowired
    ArticleService articleService;
    @Autowired
    TagService tagService;

    @GetMapping("articleList")
    public String showAllArticles(Model model){
        List<Article> articles = articleService.getAll();
        model.addAttribute("articles", articles);
        List<Tag> tags = tagService.getAll();
        model.addAttribute("tags", tags);
        return "articleList";
    }

    @GetMapping("readArticle/{id}")
    public String readArticle(@PathVariable("id") Long articleId, Model model){
        Article article = articleService.getArticleWithTags(articleId);
        model.addAttribute("article", article);
        Set<Tag> tags = article.getTags();
        model.addAttribute("tags", tags);
        List<Tag> allTags = tagService.getAll();
        model.addAttribute("allTags", allTags);
        return "article";
    }

    @GetMapping("/")
    public RedirectView redirectToArticleList(){
        return new RedirectView("articleList");
    }
}
