package yummyloop.example.util

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.Configurator

// see https://logging.apache.org/log4j/log4j-2.4/faq.html
class Logger(private val name: String) {

    constructor(name: String, level : String) : this(name){
        setLevel(level)
    }

    private val log: Logger = LogManager.getFormatterLogger(name)

    fun setLevel(level : String){
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
        Configurator.setLevel(this.name, logLevel);
        //Configurator.setRootLevel(logLevel);
    }

    fun debug(message : String){
        this.log.debug(message)
    }

    fun error(message : String){
        this.log.error(message)
    }

    fun fatal(message : String){
        this.log.fatal(message)
    }

    fun info(message : String){
        this.log.info(message)
    }

    fun trace(message : String){
        this.log.trace(message)
    }

    fun warn(message : String){
        this.log.warn(message)
    }
}