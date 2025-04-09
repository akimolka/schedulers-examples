package quartz.example

import org.quartz.*
import org.quartz.JobBuilder.newJob
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory


class FirstJob: Job {
    override fun execute(context: JobExecutionContext) {
        println("Step one")
        val job2 = newJob(SecondJob::class.java).build()
        val instantOneTimeTrigger = newTrigger().build()
        context.getScheduler().scheduleJob(job2, instantOneTimeTrigger)
    }
}

class SecondJob: Job {
    override fun execute(c: JobExecutionContext) {
        println("Step two")
    }
}

fun chainQuartzExample() {
    val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()
    scheduler.start()

    val job1: JobDetail = newJob(FirstJob::class.java).build()
    val instantOneTimeTrigger = newTrigger().build()

    scheduler.scheduleJob(job1, instantOneTimeTrigger)
}