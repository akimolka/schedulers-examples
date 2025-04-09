package quartz.example

import org.quartz.Job
import org.quartz.JobBuilder.newJob
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder.simpleSchedule
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory


class HelloJob: Job {
    override fun execute(c: JobExecutionContext) {
        println("Hello World!")
    }
}

fun basicQuartzExample() {
    val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()
    scheduler.start()

    val job: JobDetail = newJob(HelloJob::class.java)
        .build()

    val trigger = newTrigger()
        .withSchedule(
            simpleSchedule()
                .withIntervalInSeconds(30)
                .repeatForever()
        )
        .build()

    scheduler.scheduleJob(job, trigger)
}