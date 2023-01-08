package de.fantjastisch.cards_backend.link.aggregate;

import de.fantjastisch.cards_backend.link.Link;
import de.fantjastisch.cards_backend.link.validator.LinkValidator;
import de.fantjastisch.cards_backend.link.repository.LinkCommandRepository;
import de.fantjastisch.cards_backend.link.repository.LinkQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Component
public class LinkAggregate {

    private final LinkCommandRepository linkCommandRepository;
    private final LinkQueryRepository linkQueryRepository;
    private final LinkValidator linkValidator;

    @Autowired
    public LinkAggregate(LinkCommandRepository linkCommandRepository, LinkQueryRepository linkQueryRepository, LinkValidator linkValidator) {
        this.linkCommandRepository = linkCommandRepository;
        this.linkQueryRepository = linkQueryRepository;
        this.linkValidator = linkValidator;
    }

    public void handle(final CreateLink command) {
        List<Link> allCategories = linkQueryRepository.getPage(command.getSource());
        linkValidator.validate(command, allCategories);

        Link newLink = Link.builder()
                .name(command.getName())
                .source(command.getSource())
                .target(command.getTarget())
                .build();
        linkCommandRepository.save(newLink);
    }

    public Link handle(final String name, final UUID source) {
        return throwOrGet(name, source);
    }

    public List<Link> handle(final UUID source) {
        return linkQueryRepository.getPage(source);
    }

    public void handle(final DeleteLink command) {
        Link category = throwOrGet(command.getName(), command.getSource());
        linkCommandRepository.delete(category);
    }

    public void handle(final UpdateLink command) {
        List<Link> allCategories = linkQueryRepository.getPage(command.getSource());

        linkValidator.validate(command, allCategories);
        Link category = throwOrGet(command.getName(), command.getSource());

        linkCommandRepository.update(category);
    }

    private Link throwOrGet(String name, UUID source) {
        Link link = linkQueryRepository.get(name, source);
        if (link == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
        return link;
    }

}

