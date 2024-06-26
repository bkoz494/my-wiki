package pl.wiki.gui.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import pl.wiki.security.KeycloakApiService;
import pl.wiki.security.UserDTO;
import pl.wiki.service.ArticleService;
import pl.wiki.service.TagService;

import java.util.List;
import java.util.Set;

@Slf4j
@Controller
public class ArticlesController {
    @Autowired
    ArticleService articleService;
    @Autowired
    TagService tagService;
    @Autowired
    KeycloakApiService keycloakApiService;

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
        try {
            UserDTO userDTO = keycloakApiService.getUserById(article.getAuthorId());
            model.addAttribute("user", userDTO);
        } catch (Exception e) {
            log.error("Could not obtain an author from Keycloak");
        }
        return "article";
    }

    @GetMapping("/")
    public RedirectView redirectToArticleList(){
        return new RedirectView("articleList");
    }

    @GetMapping("/addArticle")
    public String addArticle(Model model){
        model.addAttribute("article", new Article());
        List<Tag> tags = tagService.getAll();
        model.addAttribute("tags", tags);
        return "addArticle";
    }

    @GetMapping("editArticle/{id}")
    public String editArticle(@PathVariable("id") Long id, Model model){
        Article article = articleService.get(id);
        model.addAttribute("article", article);
        List<Tag> tags = tagService.getAll();
        model.addAttribute("tags", tags);
        model.addAttribute("editMode", true);
        return "addArticle";
    }

    @PostMapping("/saveArticle")
    public String saveArticle(@ModelAttribute Article article){
        DefaultOidcUser user = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = (String) user.getClaims().get("sub");
        article.setAuthorId(userId);
        Article saved = articleService.save(article);
        return "redirect:/readArticle/" + saved.getId();
    }

    @GetMapping("deleteArticle/{id}")
    public String deleteArticle(@PathVariable("id") Long articleId){
        articleService.delete(articleId);
        return "redirect:/articleList";
    }

    @PostMapping("/updateArticle")
    public String updateArticle(@ModelAttribute("article") Article updatedArticle){
        DefaultOidcUser user = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updatedArticle.setAuthorId((String) user.getClaims().get("sub"));
        Article article = articleService.updateArticle(updatedArticle);
        return "redirect:/readArticle/" + article.getId();
    }
    @Getter
    @Setter
    @NoArgsConstructor
    class SearchDto {
        String title;
    }

    @ModelAttribute
    public void addTitleDTOToModel(Model model){

        model.addAttribute("searchDto", new SearchDto());
    }

    @PostMapping("/search")
    public String search(@ModelAttribute SearchDto searchDto, Model model){
        List<Article> articles = articleService.searchByTitle(searchDto.getTitle());
        model.addAttribute("articles", articles);
        return "articleList";
    }
}
