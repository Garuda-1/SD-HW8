package ru.itmo.iandolzhanskii.sd.hw8.statistic;

public interface EventStatistic {

    void incEvent(String name);

    double getEventStatisticByName(String name);

    double getAllEventStatistic();

    void printStatistic();
}
