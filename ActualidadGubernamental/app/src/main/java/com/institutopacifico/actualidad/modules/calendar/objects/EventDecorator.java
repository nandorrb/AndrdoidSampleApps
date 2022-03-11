package com.institutopacifico.actualidad.modules.calendar.objects;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by mobile on 9/22/17 at 16:06.
 * Fernando Rubio Burga
 */

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;
    private int integer_span_radius=10;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }
    public EventDecorator(int color, Collection<CalendarDay> dates,int integer_span_radius) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this.integer_span_radius=integer_span_radius;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(integer_span_radius, color));
    }
}