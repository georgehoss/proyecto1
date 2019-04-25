package com.tenneco.tennecoapp.Schedule;

import com.tenneco.tennecoapp.Model.Line;

import java.util.ArrayList;

public interface ScheduleContract {
    void getLines();
    void getPLines();
    void updateLines();
    void dialogCreate(ArrayList<Line> lines);
    void createLine(Line line);
}
