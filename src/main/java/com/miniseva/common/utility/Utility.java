package com.miniseva.common.utility;

import com.miniseva.security.account.Account;
import com.miniseva.security.account.AccountService;
import com.miniseva.app.block.Block;
import com.miniseva.app.block.BlockRepository;
import com.miniseva.app.lead.Lead;
import com.miniseva.app.lead.LeadRepository;
import com.miniseva.app.customer.Customer;
import com.miniseva.app.customer.CustomerRepository;
import com.miniseva.app.schedule.Schedule;
import com.miniseva.app.schedule.ScheduleRepository;
import com.miniseva.app.orders.Orders;

public class Utility {

    private BlockRepository repoBlock;
    private LeadRepository repoLead;
    private AccountService srvAccount;
    private CustomerRepository repoCustomer;
    private ScheduleRepository repoSchedule;


    public Utility(BlockRepository repoBlock,
                   LeadRepository repoLead,
                   CustomerRepository repoCustomer,
                   AccountService srvAccount,
                   ScheduleRepository repoSchedule) {
        this.repoBlock = repoBlock;
        this.repoLead = repoLead;
        this.repoCustomer = repoCustomer;
        this.srvAccount = srvAccount;
        this.repoSchedule = repoSchedule;
    }

    public String makeOrderBy(Orders order) {
        String orderBy = "";        
        Long createdById = order.getCreatedBy();
        Account account = srvAccount.findById(createdById);

        orderBy = account.getName() + "\n" + account.getUsername();

        return orderBy;
    }

    public String makeAddress(Orders order) {
        String address = "";
        Long createdById = order.getCreatedBy();
        Account account = srvAccount.findById(createdById);
        Long customerId = account.getCustomerId();
        Customer customer = repoCustomer.findById(customerId);
        Long leadId = customer.getLeadId();
        Lead lead = repoLead.findById(leadId);
        Long blockId = customer.getBlockId();
        if(blockId != null) {
            Block block = repoBlock.findById(blockId);
            address = "Block " + block.getName() + ", ";
        }

        address = address + lead.getName() + ", " +
                    lead.getName() + ", " + "\n" +
                    lead.getAddress1() + ", " + "\n" +
                    lead.getCity() + " - " +
                    lead.getPostalCode() + ", " + "\n" +
                    lead.getState();
                    
        return address;
    }
}