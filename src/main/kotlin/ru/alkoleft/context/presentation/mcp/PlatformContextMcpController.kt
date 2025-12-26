/*
 * Copyright (c) 2024-2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.presentation.mcp

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.annotation.ToolParam
import org.springframework.stereotype.Service
import ru.alkoleft.context.business.services.ContextSearchService
import ru.alkoleft.context.business.services.DocsInfoService
import ru.alkoleft.context.business.services.ResponseFormatterService

private val logger = KotlinLogging.logger {}

/**
 * MCP Service для поиска API платформы 1С
 *
 * Presentation Layer компонент, предоставляющий MCP интерфейс для поиска API
 */
@Service
class PlatformContextMcpController(
    private val searchService: ContextSearchService,
    private val formatter: ResponseFormatterService,
    private val docsInfoService: DocsInfoService,
) {
    /**
     * Поиск по API платформы 1С Предприятие
     */
    @Tool(
        name = "search",
        description = "Поиск по API платформы 1С Предприятие. Используйте конкретные термины 1С для получения точных результатов.",
    )
    fun search(
        @ToolParam(
            description =
                "Поисковый запрос. Используйте конкретные термины из 1С: " +
                    "методы ('СтрНайти', 'ВыполнитьПроверкуПравДоступа'), " +
                    "типы ('Справочник', 'Документ'), " +
                    "свойства ('БезопасноеХранилище', 'ПланыСчетов', 'СредстваМультимедиа')",
        )
        query: String,
        @ToolParam(
            description =
                "Тип искомого элемента API: " +
                    "'method' - глобальные методы, " +
                    "'property' - глобальные свойства, " +
                    "'type' - типы данных, " +
                    "null - все типы",
        )
        type: String? = null,
        @ToolParam(description = "Максимальное количество результатов (по умолчанию 10, максимум 50)")
        limit: Int? = null,
    ): String {
        logger.debug { "search called with query='$query', type='$type', limit='$limit'" }
        try {
            val result = searchService.searchAll(query, type, limit)
            logger.debug { "search result size: ${result.size}" }
            return formatter.formatQuery(query) +
                formatter.formatSearchResults(result)
        } catch (e: Exception) {
            logger.error(e) { "Ошибка при выполнении поиска" }
            return formatter.formatError(e)
        }
    }

    /**
     * Получение детальной информации об API элементе
     */
    @Tool(
        name = "info",
        description = "Получение детальной информации об элементе API платформы 1С. Требует точное имя элемента.",
    )
    fun getInfo(
        @ToolParam(description = "Точное имя элемента API в 1С. Примеры: 'НайтиПоСсылке', 'СправочникСсылка', 'Ссылка', 'Код'")
        name: String,
        @ToolParam(
            description =
                "Уточнение типа элемента: " +
                    "'method' - метод/функция, " +
                    "'property' - свойство/реквизит, " +
                    "'type' - тип данных",
        )
        type: String,
    ): String {
        logger.debug { "getInfo called with name='$name', type='$type'" }
        try {
            val definition = searchService.getInfo(name, type)
            logger.debug { "getInfo result: $definition" }
            return formatter.formatMember(definition)
        } catch (e: Exception) {
            logger.error(e) { "Ошибка при получении информации об элементе" }
            return "❌ **Ошибка:** ${e.message}"
        }
    }

    /**
     * Получение информации об элементе типа (методе или свойстве)
     */
    @Tool(
        name = "getMember",
        description = "Получение информации о методе или свойстве конкретного типа 1С. Используйте точные имена типов и элементов.",
    )
    fun getMember(
        @ToolParam(description = "Имя типа 1С. Примеры: 'СправочникСсылка', 'ДокументОбъект', 'Строка', 'Число', 'Дата'")
        typeName: String,
        @ToolParam(description = "Имя метода или свойства типа. Примеры: 'НайтиПоКоду', 'Записать', 'Код', 'Наименование', 'Длина'")
        memberName: String,
    ): String {
        logger.debug { "getMember called with typeName='$typeName', memberName='$memberName'" }
        try {
            val definition = searchService.findMemberByTypeAndName(typeName, memberName)
            logger.debug { "getMember result: $definition" }
            return formatter.formatMember(definition)
        } catch (e: Exception) {
            logger.error(e) { "Ошибка при получении информации об элементе типа" }
            return formatter.formatError(e)
        }
    }

    /**
     * Получение полного списка всех методов и свойств для указанного типа 1С
     */
    @Tool(
        name = "getMembers",
        description = "Получение полного списка всех методов и свойств для указанного типа 1С. Полный справочник API типа.",
    )
    fun getMembers(
        @ToolParam(
            description =
                "Имя типа 1С для получения полного списка методов и свойств. " +
                    "Примеры: 'СправочникСсылка', 'ДокументОбъект', 'Строка', 'ТаблицаЗначений', 'Запрос'",
        )
        typeName: String,
    ): String {
        logger.debug { "getMembers called with typeName='$typeName'" }
        try {
            val definition = searchService.findTypeMembers(typeName)
            logger.debug { "getMembers result size: ${definition.size}" }
            return formatter.formatTypeMembers(definition)
        } catch (e: Exception) {
            logger.error(e) { "Ошибка при получении информации об элементе типа" }
            return formatter.formatError(e)
        }
    }

    /**
     * Получение списка конструкторов для указанного типа 1С
     */
    @Tool(
        name = "getConstructors",
        description = "Получение списка конструкторов для указанного типа 1С. Показывает способы создания объектов данного типа.",
    )
    fun getConstructors(
        @ToolParam(
            description =
                "Имя типа 1С для получения конструкторов. " +
                    "Примеры: 'СправочникМенеджер', 'ДокументМенеджер', 'Запрос', 'ТаблицаЗначений'",
        )
        typeName: String,
    ): String {
        logger.debug { "getConstructors called with typeName='$typeName'" }
        try {
            val result = searchService.findConstructors(typeName)
            logger.debug { "getConstructors result size: ${result.size}" }
            return formatter.formatConstructors(result, typeName)
        } catch (e: Exception) {
            logger.error(e) { "Ошибка при получении информации об элементе типа" }
            return formatter.formatError(e)
        }
    }

    /**
     * Get BSL coding guidelines
     */
    @Tool(
        name = "getCodingGuideline",
        description = "Get BSL coding guidelines for 1C:Enterprise development. Returns best practices and coding standards.",
    )
    fun getCodingGuideline(): String {
        logger.debug { "getCodingGuideline called" }
        return docsInfoService.getGuideline()
    }

    /**
     * Get strict typing information by topic
     */
    @Tool(
        name = "getStrictTypingInfo",
        description =
            "Get strict typing information for BSL code in 1C:Enterprise. " +
                "Use this to learn how to properly type variables, parameters, structures, arrays, value tables, etc.",
    )
    fun getStrictTypingInfo(
        @ToolParam(
            description =
                "Topic name. Available topics: " +
                    "'overview' - purpose and how strict typing works, " +
                    "'enabling' - how to enable strict typing, " +
                    "'local-variables' - local variable initialization, " +
                    "'module-variables' - module variable initialization, " +
                    "'structure-keys' - structure key initialization, " +
                    "'arrays' - array description, " +
                    "'value-table' - value table description, " +
                    "'table-row' - table row description, " +
                    "'map' - map description, " +
                    "'constructor-functions' - constructor functions, " +
                    "'type-links' - references to types, " +
                    "'forms' - form-related typing, " +
                    "'query-results' - query result typing, " +
                    "'documentation-syntax' - documentation comment syntax, " +
                    "'diagnostics' - how to diagnose untyped code. " +
                    "Use 'topics' to get full list of available topics.",
        )
        topic: String,
    ): String {
        logger.debug { "getStrictTypingInfo called with topic='$topic'" }
        return if (topic.lowercase() == "topics") {
            docsInfoService.getAvailableTopics()
        } else {
            docsInfoService.getStrictTypingInfo(topic)
        }
    }

    /**
     * Search strict typing documentation
     */
    @Tool(
        name = "searchStrictTyping",
        description =
            "Search strict typing documentation for BSL code. " +
                "Use keywords to find relevant information about typing structures, arrays, value tables, parameters, etc.",
    )
    fun searchStrictTyping(
        @ToolParam(
            description = "Search query. Examples: 'ValueTable', 'constructor', 'Structure', 'Array', 'parameter type'",
        )
        query: String,
    ): String {
        logger.debug { "searchStrictTyping called with query='$query'" }
        return docsInfoService.searchStrictTyping(query)
    }
}
