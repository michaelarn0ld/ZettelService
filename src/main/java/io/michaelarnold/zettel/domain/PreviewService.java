package io.michaelarnold.zettel.domain;

import io.michaelarnold.zettel.data.PreviewRepository;
import io.michaelarnold.zettel.model.Preview;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class PreviewService {

    @Autowired
    private PreviewRepository repository;

    public List<Preview> getPreviews() {
        return repository.getPreviews();
    }
}
