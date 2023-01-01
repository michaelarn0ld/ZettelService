package io.michaelarnold.zettel.domain;

import io.michaelarnold.zettel.data.RepositoryUtils;
import io.michaelarnold.zettel.data.ZettelFileRepository;
import io.michaelarnold.zettel.exceptions.ZettelWhitelistException;
import io.michaelarnold.zettel.model.ZettelFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZettelFileService {

    @Autowired
    private ZettelFileRepository zettelFileRepository;

    @Autowired
    private RepositoryUtils repositoryUtils;

    public ZettelFile getZettel(String zettelId) {
        List<String> whitelist = repositoryUtils.getWhitelist();
        if(whitelist.stream().noneMatch(zettelId::equals)) {
            throw new ZettelWhitelistException("Cannot fetch zettel content which is not on the whitelist");
        }
        return zettelFileRepository.getZettel(zettelId);
    }
}
