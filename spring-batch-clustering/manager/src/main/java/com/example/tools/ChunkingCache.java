package com.example.tools;

import com.example.tools.Pair;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ChunkingCache implements ItemReader<Pair<Integer, String>> {

    private static List<Pair<Integer, String>> list = new LinkedList<>();

    @Override
    public Pair<Integer, String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return !list.isEmpty() ? list.remove(0) : null;
    }

    public void add(Pair<Integer, String> integerStringPair) {
        list.add(integerStringPair);
    }
}
