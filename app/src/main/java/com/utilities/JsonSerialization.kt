package com.utilities

/**
 * Some of network request migrated to kotlinx.Serialization.
 * To work with migrated data classes - mark network request with such annotation
 * to use Serialization Converter Factory.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonSerialization