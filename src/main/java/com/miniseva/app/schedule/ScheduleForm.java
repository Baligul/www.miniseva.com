package com.miniseva.app.schedule;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import org.springframework.format.annotation.DateTimeFormat;;

import javax.validation.constraints.*;

import java.util.List;

public class ScheduleForm implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(ScheduleForm.class);

    private List<Schedule> schedules;

    protected ScheduleForm() {
    }

    public ScheduleForm(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}
}