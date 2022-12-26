package io.michaelarnold.zettel.domain;

import io.michaelarnold.zettel.data.PreviewRepository;
import io.michaelarnold.zettel.model.Preview;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PreviewService {

    @Autowired
    private PreviewRepository repository;

    public List<Preview> getPreviews() {
        List<String> whitelist = repository.getWhitelist();
        int[] idArr = {0};
        List<Preview> previews = repository.getPreviews().stream()
               .filter(p -> whitelist.contains(p.getZetId()))
               .collect(Collectors.toList());
        previews.forEach(p -> p.setId(++idArr[0]));
        return previews;
    }
}
