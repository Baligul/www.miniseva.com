package com.miniseva.website;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import com.miniseva.app.schedule.Schedule;
import com.miniseva.app.schedule.ScheduleForm;
import com.miniseva.app.schedule.ScheduleRepository;
import com.miniseva.app.orders.Orders;
import com.miniseva.app.orders.OrdersRepository;

import java.util.*;

import org.joda.time.DateTime;

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
    private OrdersRepository repoOrders;

    public CheckoutController(ScheduleRepository repoSchedule,
                              OrdersRepository repoOrders) {
        this.repoSchedule = repoSchedule;
        this.repoOrders = repoOrders;
    }
        
    @PostMapping("/checkout")
    public String page(@ModelAttribute("scheduleForm") ScheduleForm scheduleForm, 
                       Model model,
                       @RequestParam(value="amount") int amount,
                       @RequestParam(value="currencyCode") String currencyCode) {
        model.addAttribute("page", "checkout");
        
        List<Schedule> schedules = scheduleForm.getSchedules();

        try {
            if(schedules != null && schedules.size() > 0) {
                // First create order then add schedule for each order
                Orders order = new Orders(amount, currencyCode, DateTime.now());
                order = repoOrders.save(order);
                for (Schedule schedule : schedules) {
                    schedule.setStatus("new");
                    schedule.setOrderId(order.getId());
                    if (schedule.getIsScheduled() == 1) {                    
                        repoSchedule.save(schedule);
                    } else {
                        schedule.setDates(getCurrentFormattedDate());
                        repoSchedule.save(schedule);
                    }
                }
                model.addAttribute("success", true);
                model.addAttribute("order", order);
                model.addAttribute("schedules", schedules);
                model.addAttribute("purchasedDate", getCurrentFormattedDate());
            }            
        } catch (Exception e) {
            model.addAttribute("success", false);
            log.info("Error: " + e.getMessage());
        }

        return "website/checkout";
    }

    @GetMapping("/checkout")
    public String getCheckout(Model model) {
        model.addAttribute("page", "checkout");

        return "website/checkout";
    }

    public String getCurrentFormattedDate() {
        DateTime currentDate = DateTime.now();

        int currentDateYear = currentDate.getYear();
        String currentDateMonth = currentDate.toString("MM");
        String currentDateDay = currentDate.toString("dd");

        return currentDateDay + "-" + currentDateMonth + "-" + currentDateYear;
    }

}