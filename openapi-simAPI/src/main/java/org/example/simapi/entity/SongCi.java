package org.example.simapi.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SongCi implements Serializable {
    String author;
    List<String> paragraphs;
    String rhythmic;
    List<String> tags;
}
