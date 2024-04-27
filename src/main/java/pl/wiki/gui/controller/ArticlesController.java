package pl.wiki.gui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        model.addAttribute("articleTags", tags);
        List<Tag> allTags = tagService.getAll();
        model.addAttribute("tags", allTags);

        return "article2";
    }

    @GetMapping("/")
    public RedirectView redirectToArticleList(){
        return new RedirectView("articleList");
    }

    @GetMapping("/addArticle")
    public String addArticle2(Model model){
        model.addAttribute("article", new Article());
        List<Tag> tags = tagService.getAll();
        model.addAttribute("tags", tags);
        return "addArticle";
    }

    @PostMapping("/saveArticle")
    public String saveArticle(@ModelAttribute Article article){
        Article saved = articleService.save(article);
        DefaultOidcUser user = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = (String) user.getClaims().get("sub");
        article.setAuthorId(userId);
        return "redirect:/readArticle/" + saved.getId();
    }

    @GetMapping("deleteArticle/{id}")
    public String deleteArticle(@PathVariable("id") Long articleId){
        articleService.delete(articleId);
        return "redirect:/articleList";
    }
}
