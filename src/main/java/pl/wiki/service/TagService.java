package pl.wiki.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wiki.dao.TagRepository;
import pl.wiki.model.Tag;

import java.util.Optional;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public Tag save(Tag tag){
        return tagRepository.save(tag);
    }

    public Tag get(Long id){
        Optional<Tag> optionalTag = tagRepository.findById(id);
        return optionalTag.orElseThrow();
    }

    public List<Tag> getAll(){
        return tagRepository.findAll();
    }

    public void deleteTag(Long id){
        tagRepository.deleteById(id);
    }
}
