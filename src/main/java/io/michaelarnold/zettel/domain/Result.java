package io.michaelarnold.zettel.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Data
public class Result<T> {

    private T payload;

    @Setter(AccessLevel.NONE)
    private final List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public void addErrorMessage(String message) {
        messages.add(message);
    }
}
