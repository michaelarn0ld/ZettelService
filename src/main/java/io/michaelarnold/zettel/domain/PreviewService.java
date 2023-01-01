package io.michaelarnold.zettel.domain;

import io.michaelarnold.zettel.data.PreviewRepository;
import io.michaelarnold.zettel.data.RepositoryUtils;
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

    @Autowired
    private RepositoryUtils repositoryUtils;

    public List<Preview> getPreviews() {
        List<String> whitelist = repositoryUtils.getWhitelist();
        log.info("Zettel whitelist: " + whitelist);
        int[] idArr = {0};
        List<Preview> previews = repository.getPreviews().stream()
               .filter(p -> whitelist.contains(p.getZetId()))
               .collect(Collectors.toList());
        log.info("Zettel previews size after filtering with whitelist: " +  previews.size());
        log.info("Example preview: " + previews.get(0));
        previews.forEach(p -> p.setId(++idArr[0]));
        return previews;
    }
}
