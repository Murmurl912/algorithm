package util.recorder;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class StopWatch {

    protected long start;
    protected long end;
    private boolean isWatching;
    protected int counter;
    protected Record current;
    protected TreeMap<Record, Long> records;

    public StopWatch() {
        isWatching = false;
        counter = 0;
        records = new TreeMap<>();
        start = end = System.nanoTime();
    }

    synchronized public void watch(Object description) {
        if(isWatching) {
            return;
        }
        end = start = System.nanoTime();
        counter++;
        current = new Record(start, description);
    }

    synchronized public Record stop() {
        if(!isWatching) {
            return null;
        }
        end = System.nanoTime();
        isWatching = false;
        current.duration = start - end;
        return current;
    }

    synchronized public Record time() {
        if(isWatching) {
            return null;
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    synchronized public Map<Integer, Long> records() {
        return (Map<Integer, Long>) records.clone();
    }

    synchronized public void clear() {
        start = end = System.nanoTime();
        records.clear();
        counter = 0;
    }

    public static class Record implements Comparable<Record> {
        private long timestamp;
        private long duration;
        private Object description;

        public Record(
                long timestamp,
                Object description) {
            this.timestamp = timestamp;
            this.description = description;
        }

        public long getTimestamp() {
            return timestamp;
        }

        protected void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getDuration() {
            return duration;
        }

        protected void setDuration(long duration) {
            this.duration = duration;
        }

        public Object getDescription() {
            return description;
        }

        protected void setDescription(Object description) {
            this.description = description;
        }

        @Override
        public int compareTo(Record that) {
            return Long.compare(
                    this.timestamp,
                    that.timestamp);
        }
    }
}
