package pl.wiki.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wiki.model.Tag;
import pl.wiki.service.TagService;

import java.util.List;

@RestController
public class TagRestController {
    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public List<Tag> getTags(){
        return tagService.getAll();
    }

    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable("id") Long id){
        return tagService.get(id);
    }

    @PostMapping("/tags")
    public Tag createTag(@RequestBody Tag tag){
        return tagService.save(tag);
    }

    @PutMapping("/tags")
    public Tag updateTag(@RequestBody Tag tag){
        Tag tag1;
        tag1 = tagService.get(tag.getId());
        if(tag1 != null){
            return tagService.save(tag);
        } else {
            return null;
        }
    }

    @DeleteMapping("/tags/{id}")
    public void deleteTag(@PathVariable("id") Long id){
        tagService.deleteTag(id);
    }
}
