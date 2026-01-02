/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.persistent.storage

import io.github.oshai.kotlinlogging.KotlinLogging
import ru.alkoleft.context.business.entities.MethodDefinition
import ru.alkoleft.context.business.entities.PlatformTypeDefinition
import ru.alkoleft.context.business.entities.PropertyDefinition
import ru.alkoleft.context.infrastructure.export.PlatformContextImporter
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

private val logger = KotlinLogging.logger {}

/**
 * Хранилище контекста платформы, загружаемое из JSON файла.
 *
 * Альтернатива PlatformContextStorage для работы с предгенерированным
 * JSON файлом вместо парсинга HBK на лету.
 *
 * Преимущества:
 * - Быстрая загрузка (JSON уже распарсен)
 * - Включает индексы связей (methodsByName, propertiesByName)
 * - Можно распространять готовые JSON файлы
 */
class JsonPlatformContextStorage(
    private val jsonPath: Path,
) {
    var methods: List<MethodDefinition> = emptyList()
        private set
    var properties: List<PropertyDefinition> = emptyList()
        private set
    var types: List<PlatformTypeDefinition> = emptyList()
        private set
    var methodsByName: Map<String, List<String>> = emptyMap()
        private set
    var propertiesByName: Map<String, List<String>> = emptyMap()
        private set
    var version: String = "unknown"
        private set

    private val indexInitialized = AtomicBoolean(false)
    private val lock = ReentrantReadWriteLock()

    /**
     * Загружает контекст из JSON файла.
     */
    fun load() {
        logger.debug { "Вызвана загрузка контекста из JSON" }

        lock.read {
            if (indexInitialized.get()) {
                logger.debug { "Контекст уже инициализирован (read lock)" }
                return
            }
        }

        lock.write {
            if (indexInitialized.get()) {
                logger.debug { "Контекст уже инициализирован (write lock)" }
                return
            }

            logger.info { "Инициализация контекста из JSON: $jsonPath" }
            loadFromJson()
            logger.info { "Контекст успешно инициализирован из JSON" }
        }
    }

    private fun loadFromJson() {
        if (!Files.exists(jsonPath)) {
            logger.error { "JSON файл не найден: $jsonPath" }
            throw IllegalArgumentException("JSON файл не найден: $jsonPath")
        }

        val importer = PlatformContextImporter()
        val result = importer.import(jsonPath)

        version = result.version
        methods = result.globalMethods
        properties = result.globalProperties
        types = result.types
        methodsByName = result.methodsByName
        propertiesByName = result.propertiesByName

        indexInitialized.set(true)

        logger.info {
            "Загружено из JSON v$version: " +
                "методы=${methods.size}, " +
                "свойства=${properties.size}, " +
                "типы=${types.size}"
        }
    }

    /**
     * Находит все типы, содержащие метод с указанным именем.
     *
     * @param methodName Имя метода
     * @return Список имён типов, содержащих этот метод
     */
    fun findTypesWithMethod(methodName: String): List<String> {
        load()
        return methodsByName[methodName] ?: emptyList()
    }

    /**
     * Находит все типы, содержащие свойство с указанным именем.
     *
     * @param propertyName Имя свойства
     * @return Список имён типов, содержащих это свойство
     */
    fun findTypesWithProperty(propertyName: String): List<String> {
        load()
        return propertiesByName[propertyName] ?: emptyList()
    }
}
