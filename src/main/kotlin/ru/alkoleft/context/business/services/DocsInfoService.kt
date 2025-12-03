/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.business.services

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

/**
 * Service for providing BSL coding guidelines and strict typing documentation.
 *
 * This service reads documentation files from external paths or falls back to
 * embedded classpath resources. Provides methods to search and retrieve information
 * about BSL strict typing and coding guidelines.
 *
 * Configuration:
 * - docs.guideline.path: External path to guideline.md file
 * - docs.strict-types.path: External path to strict-types.md file
 *
 * If external paths are not specified or files don't exist, embedded resources are used.
 */
@Service
class DocsInfoService(
    @Value("\${docs.strict-types.path:}")
    private val strictTypesPath: String,
    @Value("\${docs.guideline.path:}")
    private val guidelinePath: String,
) {
    private val logger = KotlinLogging.logger {}

    private lateinit var strictTypesContent: String
    private lateinit var guidelineContent: String
    private lateinit var topicSections: Map<String, String>

    @PostConstruct
    fun init() {
        logger.info { "Initializing DocsInfoService..." }

        strictTypesContent = loadDocumentationFile(
            externalPath = strictTypesPath,
            classpathResource = "/docinfo/strict-types.md",
            description = "strict-types",
        )

        guidelineContent = loadDocumentationFile(
            externalPath = guidelinePath,
            classpathResource = "/docinfo/guideline.md",
            description = "guideline",
        )

        topicSections = parseTopicSections(strictTypesContent)

        logger.info { "DocsInfoService initialized. Found ${topicSections.size} topics." }
    }

    /**
     * Load documentation file from external path or classpath.
     *
     * @param externalPath External file path (can be empty)
     * @param classpathResource Classpath resource path as fallback
     * @param description Description for logging
     * @return File content
     */
    private fun loadDocumentationFile(
        externalPath: String,
        classpathResource: String,
        description: String,
    ): String {
        // Try external path first
        if (externalPath.isNotBlank()) {
            val file = File(externalPath)
            if (file.exists() && file.isFile) {
                logger.info { "Loading $description from external path: $externalPath" }
                return file.readText(Charsets.UTF_8)
            } else {
                logger.warn { "External $description file not found: $externalPath, falling back to embedded resource" }
            }
        }

        // Fall back to classpath resource
        logger.info { "Loading $description from classpath: $classpathResource" }
        return javaClass.getResourceAsStream(classpathResource)
            ?.bufferedReader(Charsets.UTF_8)
            ?.use { it.readText() }
            ?: throw IllegalStateException("Embedded resource not found: $classpathResource")
    }

    /**
     * Get coding guideline content.
     *
     * @return Full content of coding guidelines
     */
    fun getGuideline(): String = guidelineContent

    /**
     * Get strict typing information by topic.
     *
     * Available topics:
     * - overview: Purpose and how strict typing works
     * - enabling: How to enable strict typing in modules
     * - calculated-typing: Calculated/dynamic typing
     * - declarative-typing: Declarative typing with comments
     * - type-links: References to types
     * - local-variables: Local variable initialization
     * - module-variables: Module variable initialization
     * - structure-keys: Structure key initialization
     * - arrays: Array description
     * - value-table: Value table description
     * - table-row: Table row description
     * - map: Map description
     * - value-list: Value list description
     * - constructor-functions: Constructor functions for complex data objects
     * - type-narrowing: Narrowing variable type
     * - forms: Form-related typing
     * - temp-storage: Temporary storage usage
     * - string-literals: String literals as names
     * - export-methods: Export procedures and functions
     * - query-results: Query result typing
     * - composite-types: Composite type limitations
     * - documentation-syntax: Documentation comment syntax
     * - diagnostics: How to diagnose untyped code
     *
     * @param topic Topic name to retrieve
     * @return Content for the specified topic or error message if not found
     */
    fun getStrictTypingInfo(topic: String): String {
        val normalizedTopic = topic.lowercase().trim()

        return topicSections[normalizedTopic]
            ?: "❌ Topic '$topic' not found. Available topics: ${topicSections.keys.sorted().joinToString(", ")}"
    }

    /**
     * Search strict typing documentation by keywords.
     *
     * @param query Search query
     * @return Matching sections from documentation
     */
    fun searchStrictTyping(query: String): String {
        if (query.isBlank()) {
            return "❌ Search query cannot be empty"
        }

        val queryLower = query.lowercase()
        val matchingTopics =
            topicSections.entries
                .filter { (_, content) -> content.lowercase().contains(queryLower) }
                .map { (topic, content) ->
                    val preview = extractPreview(content, queryLower)
                    "## Topic: $topic\n$preview"
                }

        return if (matchingTopics.isEmpty()) {
            "❌ No results found for '$query'"
        } else {
            "# Search Results for '$query'\n\nFound ${matchingTopics.size} matching topics:\n\n${matchingTopics.joinToString(
                "\n\n---\n\n",
            )}"
        }
    }

    /**
     * Get list of all available topics.
     *
     * @return List of topic names with brief descriptions
     */
    fun getAvailableTopics(): String {
        val topicDescriptions =
            mapOf(
                "overview" to "Purpose and how strict typing works",
                "enabling" to "How to enable strict typing in modules",
                "calculated-typing" to "Calculated/dynamic typing",
                "declarative-typing" to "Declarative typing with comments",
                "type-links" to "References to types",
                "local-variables" to "Local variable initialization",
                "module-variables" to "Module variable initialization",
                "structure-keys" to "Structure key initialization",
                "arrays" to "Array description",
                "value-table" to "Value table description",
                "table-row" to "Table row description",
                "map" to "Map description",
                "value-list" to "Value list description",
                "constructor-functions" to "Constructor functions for complex data objects",
                "type-narrowing" to "Narrowing variable type",
                "forms" to "Form-related typing",
                "temp-storage" to "Temporary storage usage",
                "string-literals" to "String literals as names",
                "export-methods" to "Export procedures and functions",
                "query-results" to "Query result typing",
                "composite-types" to "Composite type limitations",
                "documentation-syntax" to "Documentation comment syntax",
                "diagnostics" to "How to diagnose untyped code",
            )

        return """# Available Strict Typing Topics

${topicDescriptions.entries.joinToString("\n") { (topic, desc) -> "- **$topic**: $desc" }}

Use `getStrictTypingInfo(topic)` to get detailed information about a specific topic.
Use `searchStrictTyping(query)` to search across all topics.
"""
    }

    private fun parseTopicSections(content: String): Map<String, String> {
        val sections = mutableMapOf<String, String>()
        val topicPattern = Regex("""## TOPIC: (\S+)\s*\n""")

        val matches = topicPattern.findAll(content).toList()

        for (i in matches.indices) {
            val match = matches[i]
            val topicName = match.groupValues[1]
            val startIndex = match.range.last + 1
            val endIndex =
                if (i + 1 < matches.size) {
                    matches[i + 1].range.first
                } else {
                    content.length
                }

            sections[topicName] = content.substring(startIndex, endIndex).trim()
        }

        return sections
    }

    private fun extractPreview(
        content: String,
        query: String,
    ): String {
        val index = content.lowercase().indexOf(query)
        if (index == -1) return content.take(200) + "..."

        val start = maxOf(0, index - 100)
        val end = minOf(content.length, index + query.length + 200)

        val preview = content.substring(start, end)
        return (if (start > 0) "..." else "") + preview + (if (end < content.length) "..." else "")
    }
}
