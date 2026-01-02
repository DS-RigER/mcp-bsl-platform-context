/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.export

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.oshai.kotlinlogging.KotlinLogging
import ru.alkoleft.context.business.entities.MethodDefinition
import ru.alkoleft.context.business.entities.ParameterDefinition
import ru.alkoleft.context.business.entities.PlatformTypeDefinition
import ru.alkoleft.context.business.entities.PropertyDefinition
import ru.alkoleft.context.business.entities.Signature
import ru.alkoleft.context.infrastructure.export.models.ConstructorExport
import ru.alkoleft.context.infrastructure.export.models.MethodExport
import ru.alkoleft.context.infrastructure.export.models.ParameterExport
import ru.alkoleft.context.infrastructure.export.models.PlatformContextExport
import ru.alkoleft.context.infrastructure.export.models.PropertyExport
import ru.alkoleft.context.infrastructure.export.models.SignatureExport
import ru.alkoleft.context.infrastructure.export.models.TypeExport
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

/**
 * Загрузчик контекста платформы из предгенерированного JSON файла.
 *
 * Читает JSON файл, созданный PlatformContextExporter, и конвертирует
 * его в доменные сущности для использования в MCP сервере.
 */
class PlatformContextImporter {
    private val objectMapper =
        ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())

    /**
     * Результат загрузки контекста платформы из JSON.
     */
    data class ImportResult(
        val version: String,
        val globalMethods: List<MethodDefinition>,
        val globalProperties: List<PropertyDefinition>,
        val types: List<PlatformTypeDefinition>,
        val methodsByName: Map<String, List<String>>,
        val propertiesByName: Map<String, List<String>>,
    )

    /**
     * Загружает контекст платформы из JSON файла.
     *
     * @param jsonPath Путь к JSON файлу (platform-context.json)
     * @return Результат загрузки с entities и индексами
     */
    fun import(jsonPath: Path): ImportResult {
        logger.info { "Загрузка контекста платформы из JSON: $jsonPath" }

        val export = objectMapper.readValue(jsonPath.toFile(), PlatformContextExport::class.java)

        logger.info { "Версия платформы: ${export.version}" }
        logger.info { "Дата генерации: ${export.generatedAt}" }

        val globalMethods = export.globalMethods.map { it.toEntity() }
        val globalProperties = export.globalProperties.map { it.toEntity() }
        val types = export.types.map { it.toEntity() }

        logger.info {
            "Загружено: методы=${globalMethods.size}, " +
                "свойства=${globalProperties.size}, " +
                "типы=${types.size}"
        }
        logger.info {
            "Индексы: methodsByName=${export.methodsByName.size}, " +
                "propertiesByName=${export.propertiesByName.size}"
        }

        return ImportResult(
            version = export.version,
            globalMethods = globalMethods,
            globalProperties = globalProperties,
            types = types,
            methodsByName = export.methodsByName,
            propertiesByName = export.propertiesByName,
        )
    }

    // Extension functions для конвертации из export моделей в entities

    private fun MethodExport.toEntity() =
        MethodDefinition(
            name = nameEn,
            description = description,
            returnType = returnType ?: "",
            signature = signatures.map { it.toEntity() },
        )

    private fun SignatureExport.toEntity() =
        Signature(
            name = name,
            parameters = parameters.map { it.toEntity() },
            description = description,
        )

    private fun ParameterExport.toEntity() =
        ParameterDefinition(
            name = name,
            type = type,
            description = description,
            required = required,
            defaultValue = defaultValue,
        )

    private fun PropertyExport.toEntity() =
        PropertyDefinition(
            name = nameEn,
            description = description,
            propertyType = type,
            isReadOnly = readOnly,
        )

    private fun ConstructorExport.toEntity() =
        Signature(
            name = name,
            parameters = parameters.map { it.toEntity() },
            description = description,
        )

    private fun TypeExport.toEntity() =
        PlatformTypeDefinition(
            name = nameEn,
            description = description,
            methods = methods.map { it.toEntity() },
            properties = properties.map { it.toEntity() },
            constructors = constructors.map { it.toEntity() },
        )
}
