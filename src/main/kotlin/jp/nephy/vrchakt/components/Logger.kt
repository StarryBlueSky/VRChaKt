package jp.nephy.vrchakt.components

import mu.KotlinLogging

internal fun logger(javaclass: Class<*>) = KotlinLogging.logger(javaclass.canonicalName)
