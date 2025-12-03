/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.hbk

import org.junit.jupiter.api.Test
import ru.alkoleft.context.infrastructure.hbk.reader.HbkContentReader
import kotlin.io.path.Path

class HbkReaderTest {
    @Test
    fun read() {
        val platformPath = System.getProperty("platform.context.path")
        val reader = HbkContentReader()
        reader.read(Path(platformPath, "shcntx_root.hbk")) {
            // Find Properties page and object with properties
            fun findPage(
                pages: List<ru.alkoleft.context.infrastructure.hbk.models.Page>,
                name: String,
            ): ru.alkoleft.context.infrastructure.hbk.models.Page? {
                for (page in pages) {
                    if (page.title.en == name) return page
                    val found = findPage(page.children, name)
                    if (found != null) return found
                }
                return null
            }

            // Print HTML content for a method with different blocks
            val globalContext = findPage(toc.pages, "Global context")
            if (globalContext != null) {
                val allMethods = globalContext.children.find { it.title.en.contains("Working with") }
                if (allMethods != null) {
                    allMethods.children.take(3).forEach { method ->
                        System.err.println("  Method: ${method.title.en}")
                        val content = getEntryStream(method).bufferedReader().readText()
                        System.err.println("  All V8SH_chapter blocks:")
                        "V8SH_chapter\">([^<]+)<".toRegex().findAll(content).forEach {
                            System.err.println("    Block: '${it.groupValues[1]}'")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun printEnumSectionNames() {
        val platformPath = System.getProperty("platform.context.path") ?: return
        val reader = HbkContentReader()
        reader.read(Path(platformPath, "shcntx_root.hbk")) {
            // Find enum catalog pages
            toc.pages.forEach { page ->
                println("Root page: '${page.title.en}' | ru: '${page.title.ru}'")
            }
        }
    }

    @Test
    fun extractTestHtmlFiles() {
        val platformPath = System.getProperty("platform.context.path") ?: return
        val reader = HbkContentReader()
        reader.read(Path(platformPath, "shcntx_root.hbk")) {
            fun findPage(
                pages: List<ru.alkoleft.context.infrastructure.hbk.models.Page>,
                condition: (ru.alkoleft.context.infrastructure.hbk.models.Page) -> Boolean,
            ): ru.alkoleft.context.infrastructure.hbk.models.Page? {
                for (page in pages) {
                    if (condition(page)) return page
                    val found = findPage(page.children, condition)
                    if (found != null) return found
                }
                return null
            }

            fun findAllPages(
                pages: List<ru.alkoleft.context.infrastructure.hbk.models.Page>,
                condition: (ru.alkoleft.context.infrastructure.hbk.models.Page) -> Boolean,
            ): List<ru.alkoleft.context.infrastructure.hbk.models.Page> {
                val result = mutableListOf<ru.alkoleft.context.infrastructure.hbk.models.Page>()
                for (page in pages) {
                    if (condition(page)) result.add(page)
                    result.addAll(findAllPages(page.children, condition))
                }
                return result
            }

            fun extractAndSave(
                page: ru.alkoleft.context.infrastructure.hbk.models.Page,
                outputDir: java.io.File,
            ) {
                try {
                    val content = getEntryStream(page).bufferedReader().readText()
                    outputDir.mkdirs()
                    val safeName = page.title.en.replace(Regex("[<>:\"/\\\\|?*]"), "_")
                    java.io.File(outputDir, "$safeName.html").writeText(content)
                    println("Extracted: ${page.title.en} -> ${outputDir.name}")
                } catch (e: Exception) {
                    println("Failed to extract ${page.title.en}: ${e.message}")
                }
            }

            // Extract method pages - find by names in current tests
            val methodNames =
                listOf(
                    "BeginGetFileFromServer",
                    "GetCommonTemplate",
                    "AttachAddIn",
                    "BeginTransaction",
                    "NumberInWords",
                    "StrTemplate",
                    "InputDate",
                )
            val methodsDir = java.io.File("src/test/resources/global-methods")
            methodNames.forEach { name ->
                val page = findPage(toc.pages) { it.htmlPath.contains("/methods/") && it.title.en == name }
                page?.let { extractAndSave(it, methodsDir) }
            }

            // Extract constructor pages
            val constructorsDir = java.io.File("src/test/resources/constructors")
            findAllPages(toc.pages) { it.htmlPath.contains("/ctors/") && !it.htmlPath.contains("catalog") }.take(5).forEach { page ->
                extractAndSave(page, constructorsDir)
            }

            // Extract enum pages (enums that have values as children)
            val enumsDir = java.io.File("src/test/resources/enums")
            findAllPages(toc.pages) {
                it.htmlPath.contains("/enums/") && !it.htmlPath.contains("/properties/")
            }.take(3).forEach { page ->
                extractAndSave(page, enumsDir)
            }

            // Extract enum value pages - look for property pages under enums
            val enumValuesDir = java.io.File("src/test/resources/enum-values")
            findAllPages(toc.pages) {
                it.htmlPath.contains("/enums/") && it.htmlPath.contains("/properties/")
            }.take(2).forEach { page ->
                extractAndSave(page, enumValuesDir)
            }

            // Extract object pages
            val objectsDir = java.io.File("src/test/resources/objects")
            findAllPages(toc.pages) {
                it.htmlPath.contains("/objects/") &&
                    !it.htmlPath.contains("/methods/") &&
                    !it.htmlPath.contains("/properties/") &&
                    !it.htmlPath.contains("/ctors/") &&
                    !it.htmlPath.contains("/events/") &&
                    !it.htmlPath.contains("catalog") &&
                    !it.htmlPath.contains("Global context") &&
                    it.htmlPath.endsWith(".html")
            }.take(3).forEach { page ->
                extractAndSave(page, objectsDir)
            }

            // Extract object property pages
            val objectPropertiesDir = java.io.File("src/test/resources/object-properties")
            findAllPages(toc.pages) {
                it.htmlPath.contains("/objects/") &&
                    it.htmlPath.contains("/properties/") &&
                    !it.htmlPath.contains("catalog") &&
                    !it.htmlPath.contains("/enums/")
            }.take(2).forEach { page ->
                extractAndSave(page, objectPropertiesDir)
            }
        }
    }

//    @Test
//    fun readPlatformContextGrabber(@TempDir path: Path){
//        val platformPath = System.getProperty("platform.context.path");
//        val parser = PlatformContextGrabber(Path(platformPath, "shcntx_root.hbk")), path)
//        parser.parse()
//    }
}
