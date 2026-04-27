package cius.mai_onsyn.dobot

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val Any.log: Logger
    get() = LogManager.getLogger(this.javaClass)

val log: Logger
    get() = LogManager.getLogger("KotlinUnnamed")