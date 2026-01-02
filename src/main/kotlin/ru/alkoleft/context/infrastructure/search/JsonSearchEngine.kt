/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.search

import io.github.oshai.kotlinlogging.KotlinLogging
import ru.alkoleft.context.business.entities.Definition
import ru.alkoleft.context.business.entities.MethodDefinition
import ru.alkoleft.context.business.entities.PlatformTypeDefinition
import ru.alkoleft.context.business.entities.PropertyDefinition
import ru.alkoleft.context.business.valueobjects.ApiType
import ru.alkoleft.context.business.valueobjects.SearchQuery
import ru.alkoleft.context.infrastructure.persistent.storage.JsonPlatformContextStorage
import ru.alkoleft.context.infrastructure.persistent.storage.PlatformContextStorage
import ru.alkoleft.context.infrastructure.search.indexes.HashIndex
import ru.alkoleft.context.infrastructure.search.indexes.Indexes
import ru.alkoleft.context.infrastructure.search.indexes.StartWithIndex

private val logger = KotlinLogging.logger {}

/**
 * Поисковый движок для JSON-based хранилища.
 *
 * Использует предзагруженный JSON с индексами связей methodsByName и propertiesByName
 * для быстрого поиска методов и свойств по типам.
 */
class JsonSearchEngine(
    private val jsonStorage: JsonPlatformContextStorage,
) : SearchEngine {
    private val hashIndexes =
        Indexes(
            methods = HashIndex(),
            properties = HashIndex(),
            types = HashIndex(),
        )
    private val startWithIndexes =
        Indexes(
            methods = StartWithIndex(),
            properties = StartWithIndex(),
            types = StartWithIndex(),
        )
    private var initialized = false

    override fun initialize(context: PlatformContextStorage) {
        // Игнорируем PlatformContextStorage, используем jsonStorage
        initializeFromJson()
    }

    private fun initializeFromJson() {
        if (initialized) return

        logger.info { "Инициализация JSON поискового движка..." }
        try {
            jsonStorage.load()
        } catch (e: Exception) {
            logger.error(e) { "Ошибка при загрузке JSON контекста: ${e.message}" }
            throw e
        }

        // Загружаем индексы
        hashIndexes.properties.load(jsonStorage.properties, PropertyDefinition::name)
        hashIndexes.methods.load(jsonStorage.methods, MethodDefinition::name)
        hashIndexes.types.load(jsonStorage.types, PlatformTypeDefinition::name)

        startWithIndexes.properties.load(jsonStorage.properties, PropertyDefinition::name)
        startWithIndexes.methods.load(jsonStorage.methods, MethodDefinition::name)
        startWithIndexes.types.load(jsonStorage.types, PlatformTypeDefinition::name)

        initialized = true
        logger.info {
            "JSON поисковый движок инициализирован: " +
                "типы=${hashIndexes.types.size}, " +
                "методы=${hashIndexes.methods.size}, " +
                "свойства=${hashIndexes.properties.size}"
        }
    }

    private fun <T> initializeIfNeeded(block: () -> T): T {
        if (!initialized) {
            initializeFromJson()
        }
        return block()
    }

    override fun search(searchQuery: SearchQuery): List<Definition> =
        initializeIfNeeded {
            search(searchQuery.query, searchQuery.maxResults, searchQuery.apiType)
        }

    override fun findType(name: String): PlatformTypeDefinition? =
        initializeIfNeeded {
            hashIndexes.types.get(name).firstOrNull()
        }

    override fun findProperty(name: String): PropertyDefinition? =
        initializeIfNeeded {
            hashIndexes.properties.get(name).firstOrNull()
        }

    override fun findMethod(name: String): MethodDefinition? =
        initializeIfNeeded {
            hashIndexes.methods.get(name).firstOrNull()
        }

    override fun findTypeMember(
        type: PlatformTypeDefinition,
        memberName: String,
    ): Definition? {
        val memberNameLower = memberName.lowercase()
        return initializeIfNeeded {
            type.methods.find { it.name.lowercase() == memberNameLower }
                ?: type.properties.find { it.name.lowercase() == memberNameLower }
        }
    }

    /**
     * Находит все типы, содержащие метод с указанным именем.
     * Использует предгенерированный индекс methodsByName.
     */
    fun findTypesWithMethod(methodName: String): List<String> =
        initializeIfNeeded {
            jsonStorage.findTypesWithMethod(methodName)
        }

    /**
     * Находит все типы, содержащие свойство с указанным именем.
     * Использует предгенерированный индекс propertiesByName.
     */
    fun findTypesWithProperty(propertyName: String): List<String> =
        initializeIfNeeded {
            jsonStorage.findTypesWithProperty(propertyName)
        }

    private fun search(
        query: String,
        limit: Int?,
        type: ApiType? = null,
    ): List<Definition> {
        val effectiveLimit = limit ?: 10
        val normalizedQuery = query.trim().lowercase()

        val results = mutableListOf<Definition>()

        when (type) {
            ApiType.TYPE -> {
                results.addAll(startWithIndexes.types.get(normalizedQuery))
            }
            ApiType.METHOD -> {
                results.addAll(startWithIndexes.methods.get(normalizedQuery))
            }
            ApiType.PROPERTY -> {
                results.addAll(startWithIndexes.properties.get(normalizedQuery))
            }
            ApiType.CONSTRUCTOR -> {
                // Конструкторы ищем через типы
                results.addAll(startWithIndexes.types.get(normalizedQuery))
            }
            null -> {
                results.addAll(startWithIndexes.types.get(normalizedQuery))
                results.addAll(startWithIndexes.methods.get(normalizedQuery))
                results.addAll(startWithIndexes.properties.get(normalizedQuery))
            }
        }

        return results.take(minOf(effectiveLimit, 50))
    }
}
