package edu.ucdenver.park.microgrid.dummy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DummyMain {
    public static void main(String[] args) {
        DummyMicrogrid grid = new DummyMicrogrid();
        ObjectMapper mapper = new ObjectMapper();
        String gridAsString = null;
        try {
            gridAsString = mapper.writeValueAsString(grid);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(gridAsString);
    }
}
