package siri.xlite.common;

public interface Color {
    String BLACK = (char) 27 + "[1;30m";
    String RED = (char) 27 + "[1;31m";
    String GREEN = (char) 27 + "[1;32m";
    String YELLOW = (char) 27 + "[1;33m";
    String BLUE = (char) 27 + "[1;34m";
    String MAGENTA = (char) 27 + "[1;35m";
    String CYAN = (char) 27 + "[1;36m";
    String WHITE = (char) 27 + "[1;37m";
    String NORMAL = (char) 27 + "[0;39m";

    String SETCOLOR_SUCCESS = GREEN;
    String SETCOLOR_FAILURE = RED;
    String SETCOLOR_WARNING = YELLOW;
    String SETCOLOR_NORMAL = NORMAL;
}
