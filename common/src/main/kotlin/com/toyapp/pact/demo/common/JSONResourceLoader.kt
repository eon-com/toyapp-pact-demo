package com.toyapp.pact.demo.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object JSONResourceLoader {

    fun <T> loadCollectionFromResource(resourceName: String, clazz: Class<T>): List<T> {
        val mapper = jacksonObjectMapper().withCustomConfiguration()
        val objectsAsJson = (this.javaClass.getResource(resourceName).readText())
        return mapper.readValue(objectsAsJson, mapper.typeFactory.constructCollectionType(List::class.java, clazz))
    }

    fun <T> loadFromResource(resourceName: String, clazz: Class<T>): T {
        val mapper = jacksonObjectMapper().withCustomConfiguration()
        val objectAsJson = (this.javaClass.getResource(resourceName).readText())
        return mapper.readValue(objectAsJson, clazz)
    }

}