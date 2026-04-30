package cius.mai_onsyn.dobot.gui.content.experience.log;

import kotlinx.coroutines.channels.Channel
import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.Property
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.config.plugins.PluginAttribute
import org.apache.logging.log4j.core.config.plugins.PluginElement
import org.apache.logging.log4j.core.config.plugins.PluginFactory
import java.io.Serializable

val LOG_QUEUE = Channel<LogEvent>(64)

@Plugin(name = "ComposeAppender", category = "Core", elementType = "appender")
class ComposeAppender(
    name: String,
    filter: Filter?,
    layout: Layout<out Serializable?>?
) : AbstractAppender(name, filter, layout, true, Property.EMPTY_ARRAY) {

    override fun append(event: LogEvent) {
        LOG_QUEUE.trySend(event)
    }

    companion object {
        @PluginFactory
        @JvmStatic
        fun createAppender(
            @PluginAttribute("name") name: String,
            @PluginElement("Filter") filter: Filter?,
            @PluginElement("Layout") layout: Layout<out Serializable?>?
        ): ComposeAppender {
            return ComposeAppender(name, filter, layout)
        }
    }
}