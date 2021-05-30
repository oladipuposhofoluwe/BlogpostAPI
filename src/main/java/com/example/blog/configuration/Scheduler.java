package com.example.blog.configuration;

import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Autowired
    UserService personService;

    //private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    //private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Scheduled(fixedRate = 3000)
    public void scheduleTaskWithFixedRate() {
        // logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
        personService.deactivatedPersonScheduler();
    }
}
