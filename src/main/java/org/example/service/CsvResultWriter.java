package org.example.service;

import jakarta.annotation.PreDestroy;
import org.example.config.ResultWriterConfig;
import org.example.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CsvResultWriter implements ResultWriter {

    private final Writer writer;

    public CsvResultWriter(ResultWriterConfig config) throws IOException {
        writer = new BufferedWriter(new FileWriter(config.getFileName()));
    }

    @PreDestroy
    public void destroy() throws IOException {
        writer.close();
    }

    @Override
    public void write(UserInfo userInfo) {
        var str = buildCsvString(
                userInfo.getId(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getBirthDate(),
                userInfo.getCity(),
                userInfo.getContacts()
        );
        try {
            writer.write(str);
            writer.append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildCsvString(Object ...vals) {
        return Stream.of(vals)
                .map(Objects::toString)
                .map(this::escapeSpecialChars)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialChars(String s) {
        var res = s.replaceAll("\\R", " ");
        if (s.contains(",") || s.contains("\"") || s.contains("'")) {
            res = res.replaceAll("\"", "\"\"");
            res = "\"" + res + "\"";
        }
        return res;
    }
}
