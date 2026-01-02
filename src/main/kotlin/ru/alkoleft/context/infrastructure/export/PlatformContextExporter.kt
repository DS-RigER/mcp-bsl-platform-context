/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.export

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.oshai.kotlinlogging.KotlinLogging
import ru.alkoleft.context.infrastructure.export.models.ConstructorExport
import ru.alkoleft.context.infrastructure.export.models.EnumExport
import ru.alkoleft.context.infrastructure.export.models.EnumValueExport
import ru.alkoleft.context.infrastructure.export.models.MethodExport
import ru.alkoleft.context.infrastructure.export.models.ParameterExport
import ru.alkoleft.context.infrastructure.export.models.PlatformContextExport
import ru.alkoleft.context.infrastructure.export.models.PropertyExport
import ru.alkoleft.context.infrastructure.export.models.SignatureExport
import ru.alkoleft.context.infrastructure.export.models.TypeExport
import ru.alkoleft.context.infrastructure.hbk.models.ConstructorInfo
import ru.alkoleft.context.infrastructure.hbk.models.EnumInfo
import ru.alkoleft.context.infrastructure.hbk.models.MethodInfo
import ru.alkoleft.context.infrastructure.hbk.models.MethodParameterInfo
import ru.alkoleft.context.infrastructure.hbk.models.MethodSignatureInfo
import ru.alkoleft.context.infrastructure.hbk.models.ObjectInfo
import ru.alkoleft.context.infrastructure.hbk.models.PropertyInfo
import ru.alkoleft.context.infrastructure.persistent.storage.PlatformContextLoader
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Сервис для конвертации HBK документации платформы 1С в JSON формат.
 *
 * Парсит HBK файлы и создаёт единый JSON файл с полным контекстом платформы,
 * включая индексы связей для быстрого поиска.
 */
class PlatformContextExporter {
    private val objectMapper =
        ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    /**
     * Конвертирует HBK документацию в JSON файл.
     *
     * @param platformPath Путь к каталогу платформы 1С (содержит shcntx_root.hbk)
     * @param outputPath Путь для сохранения результата
     * @param version Версия платформы (опционально, для метаданных)
     */
    fun export(
        platformPath: Path,
        outputPath: Path,
        version: String = "unknown",
    ) {
        logger.info { "Начало конвертации HBK → JSON" }
        logger.info { "Источник: $platformPath" }
        logger.info { "Результат: $outputPath" }

        val loader = PlatformContextLoader()
        var exportData: PlatformContextExport? = null

        loader.loadPlatformContext(platformPath) {
            logger.info { "Парсинг глобальных методов..." }
            val globalMethods = globalMethods().map { it.toExport() }.toList()
            logger.info { "Найдено глобальных методов: ${globalMethods.size}" }

            logger.info { "Парсинг глобальных свойств..." }
            val globalProperties = globalProperties().map { it.toExport() }.toList()
            logger.info { "Найдено глобальных свойств: ${globalProperties.size}" }

            logger.info { "Парсинг типов..." }
            val types = types().map { it.toExport() }.toList()
            logger.info { "Найдено типов: ${types.size}" }

            logger.info { "Парсинг перечислений..." }
            val enums = enums().map { it.toExport() }.toList()
            logger.info { "Найдено перечислений: ${enums.size}" }

            // Построение индексов связей
            logger.info { "Построение индексов связей..." }
            val methodsByName = buildMethodsIndex(types)
            val propertiesByName = buildPropertiesIndex(types)
            logger.info { "Уникальных методов в индексе: ${methodsByName.size}" }
            logger.info { "Уникальных свойств в индексе: ${propertiesByName.size}" }

            exportData =
                PlatformContextExport(
                    version = version,
                    generatedAt = Instant.now(),
                    globalMethods = globalMethods,
                    globalProperties = globalProperties,
                    types = types,
                    enums = enums,
                    methodsByName = methodsByName,
                    propertiesByName = propertiesByName,
                )
        }

        // Создаём директорию если не существует
        Files.createDirectories(outputPath)

        // Записываем JSON
        val jsonFile = outputPath.resolve("platform-context.json")
        objectMapper.writeValue(jsonFile.toFile(), exportData)
        logger.info { "JSON сохранён: $jsonFile" }

        val fileSize = Files.size(jsonFile)
        logger.info { "Размер файла: ${fileSize / 1024} KB" }

        logger.info { "Конвертация завершена успешно!" }
    }

    /**
     * Строит индекс методов: имя метода → список типов, где он доступен.
     */
    private fun buildMethodsIndex(types: List<TypeExport>): Map<String, List<String>> {
        val index = mutableMapOf<String, MutableList<String>>()

        for (type in types) {
            for (method in type.methods) {
                val methodName = method.nameEn
                index.getOrPut(methodName) { mutableListOf() }.add(type.nameEn)
            }
        }

        return index.mapValues { it.value.sorted() }
    }

    /**
     * Строит индекс свойств: имя свойства → список типов, где оно доступно.
     */
    private fun buildPropertiesIndex(types: List<TypeExport>): Map<String, List<String>> {
        val index = mutableMapOf<String, MutableList<String>>()

        for (type in types) {
            for (property in type.properties) {
                val propertyName = property.nameEn
                index.getOrPut(propertyName) { mutableListOf() }.add(type.nameEn)
            }
        }

        return index.mapValues { it.value.sorted() }
    }

    // Extension functions для конвертации моделей

    private fun ObjectInfo.toExport() =
        TypeExport(
            nameRu = nameRu,
            nameEn = nameEn,
            description = description,
            methods = methods?.map { it.toExport() } ?: emptyList(),
            properties = properties?.map { it.toExport() } ?: emptyList(),
            constructors = constructors?.map { it.toExport() } ?: emptyList(),
        )

    private fun MethodInfo.toExport() =
        MethodExport(
            nameRu = nameRu,
            nameEn = nameEn,
            description = description,
            returnType = returnValue?.type,
            signatures = signatures.map { it.toExport() },
        )

    private fun MethodSignatureInfo.toExport() =
        SignatureExport(
            name = name,
            syntax = syntax,
            description = description,
            parameters = parameters.map { it.toExport() },
        )

    private fun MethodParameterInfo.toExport() =
        ParameterExport(
            name = name,
            type = type,
            description = description,
            required = !isOptional,
            defaultValue = null,
        )

    private fun PropertyInfo.toExport() =
        PropertyExport(
            nameRu = nameRu,
            nameEn = nameEn,
            description = description,
            type = typeName,
            readOnly = readonly,
        )

    private fun ConstructorInfo.toExport() =
        ConstructorExport(
            name = name,
            syntax = syntax,
            description = description,
            parameters = parameters.map { it.toExport() },
        )

    private fun EnumInfo.toExport() =
        EnumExport(
            nameRu = nameRu,
            nameEn = nameEn,
            description = description,
            values = values.map { EnumValueExport(it.nameRu, it.nameEn, it.description) },
        )
}
