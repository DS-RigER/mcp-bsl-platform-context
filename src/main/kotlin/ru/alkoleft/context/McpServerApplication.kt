/*
 * Copyright (c) 2024-2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.alkoleft.context.infrastructure.export.PlatformContextExporter
import java.nio.file.Paths

@SpringBootApplication
class McpServerApplication

fun main(args: Array<String>) {
    System.err.println("MCP BSL Context starting...")
    try {
        runMainLogic(args)
    } catch (e: Exception) {
        System.err.println("FATAL ERROR: ${e.message}")
        e.printStackTrace(System.err)
        System.exit(1)
    }
}

private fun runMainLogic(args: Array<String>) {
    val parser = ArgParser("mcp-bsl-context")

    val platformPath by parser.option(
        ArgType.String,
        shortName = "p",
        fullName = "platform-path",
        description = "Путь к каталогу платформы 1С",
    )
    val verbose by parser.option(
        ArgType.Boolean,
        shortName = "v",
        fullName = "verbose",
        description = "Включить отладочное логирование",
    )
    val mode by parser
        .option(
            ArgType.Choice(listOf("sse", "stdio", "convert"), { it }),
            shortName = "m",
            fullName = "mode",
            description = "Режим работы: sse, stdio или convert (конвертация HBK → JSON)",
        ).default("stdio")
    val ssePort by parser.option(
        ArgType.Int,
        fullName = "port",
        description = "Порт для SSE сервера (по умолчанию 8080)",
    )
    val outputPath by parser.option(
        ArgType.String,
        shortName = "o",
        fullName = "output",
        description = "Путь для сохранения результата конвертации (для режима convert)",
    )
    val platformVersion by parser.option(
        ArgType.String,
        fullName = "version",
        description = "Версия платформы для метаданных (для режима convert)",
    )
    val jsonPath by parser.option(
        ArgType.String,
        shortName = "j",
        fullName = "json-path",
        description = "Путь к JSON файлу контекста (вместо HBK)",
    )

    parser.parse(args)

    // Настройка логирования
    if (verbose == true) {
        System.setProperty("logging.level.root", "DEBUG")
    }

    // Режим конвертации - не запускаем Spring, просто конвертируем
    if (mode == "convert") {
        runConvertMode(platformPath, outputPath, platformVersion)
        return
    }

    // Настройка пути к платформе (HBK) или JSON
    if (!jsonPath.isNullOrBlank()) {
        System.setProperty("platform.context.json-path", jsonPath as String)
    } else if (!platformPath.isNullOrBlank()) {
        System.setProperty("platform.context.path", platformPath as String)
    }

    // Настройка режима работы
    val activeProfiles = mutableListOf<String>()

    when (mode) {
        "sse" -> {
            activeProfiles.add("sse")
            if (ssePort != null) {
                System.setProperty("server.port", ssePort.toString())
            }
        }

        "stdio" -> {
            activeProfiles.add("stdio")
        }
    }

    if (System.getProperty("os.name").startsWith("Windows")) {
        System.setProperty("file.encoding", "UTF-8")
    }

    // Не передаём CLI аргументы Spring - они уже обработаны
    runApplication<McpServerApplication> {
        setDefaultProperties(mapOf("spring.profiles.active" to activeProfiles.joinToString(",")))
    }
}

/**
 * Запускает режим конвертации HBK → JSON.
 */
private fun runConvertMode(
    platformPath: String?,
    outputPath: String?,
    platformVersion: String?,
) {
    if (platformPath.isNullOrBlank()) {
        System.err.println("Ошибка: для режима convert необходим параметр --platform-path")
        System.exit(1)
    }
    if (outputPath.isNullOrBlank()) {
        System.err.println("Ошибка: для режима convert необходим параметр --output")
        System.exit(1)
    }

    try {
        val exporter = PlatformContextExporter()
        exporter.export(
            platformPath = Paths.get(platformPath),
            outputPath = Paths.get(outputPath),
            version = platformVersion ?: "unknown",
        )
        println("Конвертация завершена успешно!")
        println("Результат: $outputPath/platform-context.json")
    } catch (e: Exception) {
        System.err.println("Ошибка при конвертации: ${e.message}")
        e.printStackTrace()
        System.exit(1)
    }
}
