package uz.pdp.todo.enums;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPriorityConverter implements Converter<String, Priority> {

    @Override
    public Priority convert(@NotNull String source) {
        try {
            return Priority.valueOf(source.toUpperCase());
        } catch (Exception e) {
            return Priority.MEDIUM; // default
        }
    }
}
