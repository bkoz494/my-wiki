package pl.wiki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;
    @ManyToMany(mappedBy = "tags")
    Set<Article> articles = new HashSet<>();

    public void addArticle(Article article){
        this.articles.add(article);
    }

    public void removeArticle(Article article){
        this.articles.remove(article);
    }

    @PreRemove
    private void removeArticlesAssociations(){
        for(Article article : this.articles){
            article.removeTag(this);
        }
    }
}
