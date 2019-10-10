package yummyloop.example.util

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender
import org.apache.logging.log4j.core.layout.PatternLayout
import org.apache.logging.log4j.message.ParameterizedMessageFactory
import java.util.concurrent.LinkedBlockingQueue
import org.apache.logging.log4j.core.Logger as Log4jLogger


// see https://logging.apache.org/log4j/log4j-2.4/faq.html
class Logger(name: String) : Log4jLogger(LoggerContext.getContext(), name, ParameterizedMessageFactory()) {
    private val id = this.name
    private val layout: PatternLayout = PatternLayout.newBuilder()
        .withConfiguration(this.privateConfig.config)
        .withPattern("[%d{HH:mm:ss}] [$id/%level]: %msg%n")
        .build()

    init {
        replaceConsoleAppender()
        replaceFileAppender()
    }

    constructor(name: String, level : String) : this(name){
        setLevel(level)
    }

    private fun replaceConsoleAppender() {
        val default = this.appenders["SysOut"] as ConsoleAppender

        val builder = ConsoleAppender::class.java.getMethod("newBuilder").invoke(null) as ConsoleAppender.Builder<*>
        val appender = builder
                .setConfiguration(this.privateConfig.config)
                .withName(default.name)
                .withLayout(layout)
                .build()

        appender.start()

        this.removeAppender(default)
        this.privateConfig.loggerConfig.addAppender(appender, null, null)
    }

    private fun replaceFileAppender() {
        val default = this.appenders["File"] as RollingRandomAccessFileAppender

        val builder = RollingRandomAccessFileAppender::class.java.getMethod("newBuilder").invoke(null) as RollingRandomAccessFileAppender.Builder<*>
        val appender = builder
                .setConfiguration(this.privateConfig.config)
                .withBufferSize(default.bufferSize)
                .withFileName(default.fileName)
                .withFilePattern(default.filePattern)
                .withPolicy(default.manager.getTriggeringPolicy())
                .withFilter(default.filter)
                .withName(default.name)
                .withLayout(layout)
                .build()

        appender.start()

        this.removeAppender(default)
        this.privateConfig.loggerConfig.addAppender(appender, null, null)
    }

    // Server gui ? might not work
    private fun replaceServerConsoleAppender() {
        val default = this.appenders["ServerGuiConsole"] as com.mojang.util.QueueLogAppender

        val appender = com.mojang.util.QueueLogAppender(
                default.name,
                default.filter,
                this.layout,
                default.ignoreExceptions(),
                LinkedBlockingQueue<String>())
        //QueueLogAppender(name, filter, layout, ignoreExceptions, queue)

        appender.start()

        this.removeAppender(default)
        this.privateConfig.loggerConfig.addAppender(appender, null, null)
    }

    private fun setLevel(level : String){
        val logLevel : Level = when (level) {
            "ALL" -> Level.ALL
            "DEBUG" -> Level.DEBUG
            "ERROR" -> Level.ERROR
            "FATAL" -> Level.FATAL
            "INFO" -> Level.INFO
            "OFF" -> Level.OFF
            "TRACE" -> Level.TRACE
            "WARN" -> Level.WARN
            else -> Level.ALL
        }

        this.level=logLevel
        //Configurator.setLevel(this.name, logLevel)
        //Configurator.setRootLevel(logLevel)
    }
}

/* for reference
    //println(this.appenders.toString()) // {SysOut=SysOut, EXAMPLE=EXAMPLE, File=File, ServerGuiConsole=ServerGuiConsole}
    val config = this.privateConfig.config
    val loggerConfig = this.privateConfig.loggerConfig
    //config.addAppender(appender)
    //loggerConfig.addAppender(appender, null, null)
    //this.context.updateLoggers()
 */