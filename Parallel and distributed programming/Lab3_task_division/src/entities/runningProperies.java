package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class runningProperies {
    public String APPROACH;
    public Integer THREAD_NB;
    public String TYPE;
    public Integer FIRST_SIZE;
    public Integer SECOND_SIZE;
    public ArrayList<String> APPROACH_LIST;
    public ArrayList<Integer> THREAD_LIST;
    public ArrayList<String> TYPE_LIST;
    public ArrayList<Integer> FIRST_SIZE_LIST;
    public ArrayList<Integer> SECOND_SIZE_LIST;

    public runningProperies() {
        Random rand = new Random();
        this.APPROACH = "NORMAL";
        this.THREAD_NB = 10;
        this.TYPE = "task-row";
        this.FIRST_SIZE = ThreadLocalRandom.current().nextInt(2, 7);
        this.SECOND_SIZE = ThreadLocalRandom.current().nextInt(2, 7);
        this.APPROACH_LIST = new ArrayList<>(Arrays.asList("THREAD POOL", "NORMAL"));
        this.THREAD_LIST = new ArrayList<>(Arrays.asList(2, 10, 100));
        this.TYPE_LIST = new ArrayList<>(Arrays.asList("task-row", "task-col", "task-kth"));
        this.FIRST_SIZE_LIST = new ArrayList<>(Arrays.asList(2, 10, 100, 1000));
        this.SECOND_SIZE_LIST = new ArrayList<>(Arrays.asList(2, 10, 100, 1000));
    }
}
