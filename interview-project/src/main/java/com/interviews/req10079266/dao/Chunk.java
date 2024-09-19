package com.interviews.req10079266.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chunk<T> {
    private List<T> rows;
    private int totalChunks;
    private int nextChunk;
}
