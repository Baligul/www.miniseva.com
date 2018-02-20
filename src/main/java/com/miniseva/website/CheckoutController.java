package com.miniseva.website;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

import com.miniseva.app.schedule.Schedule;
import com.miniseva.app.schedule.ScheduleForm;
import com.miniseva.app.schedule.ScheduleRepository;

import java.util.*;

// import com.miniseva.app.slot.Slot;
// import com.miniseva.app.slot.SlotRepository;

// import com.miniseva.app.checkout.Checkout;
// import com.miniseva.app.checkout.CheckoutRepository;

@Controller
public class CheckoutController {
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    // private List<Schedule> schedules = new ArrayList<Schedule>();

    // private CheckoutRepository repoCheckout;
    // private ProductRepository repoProduct;
    // private SlotRepository repoSlot;
    private ScheduleRepository repoSchedule;

    public CheckoutController(ScheduleRepository repoSchedule) {
        this.repoSchedule = repoSchedule;
    }
        
    @PostMapping("/checkout")
    public String page(@ModelAttribute("scheduleForm") ScheduleForm scheduleForm, Model model) {
        model.addAttribute("page", "checkout");
        
        List<Schedule> schedules = scheduleForm.getSchedules();
        
        if(schedules != null && schedules.size() > 0) {
			for (Schedule schedule : schedules) {
                if (schedule.getIsScheduled() == 1) {
                    if(schedule != null && repoSchedule != null) {
                        repoSchedule.save(schedule);
                    } else {
                        log.info("repoSchedule is null 1");
                    }                   
                } else {
                    if(schedule != null && repoSchedule != null) {
                        repoSchedule.save(schedule);
                    } else {
                        log.info("repoSchedule is null 2");
                    }
                }
			}
		}

        return "website/checkout";
    }

    @GetMapping("/checkout")
    public String getCheckout(Model model) {
        model.addAttribute("page", "checkout");

        return "website/checkout";
    }

}