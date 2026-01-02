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
 * Service for providing BSL check error descriptions.
 *
 * This service reads check description files from a configured directory.
 * Each check has its own markdown file with the check code as filename.
 *
 * Configuration:
 * - docs.checks.path: Path to directory containing check description files (*.md)
 *
 * Usage:
 * - getCheckInfo(code): Get full description for a specific check by its code
 * - searchChecks(query): Search checks by keyword
 * - listChecks(): Get list of all available checks
 */
@Service
class ChecksInfoService(
    @Value("\${docs.checks.path:}")
    private val checksPath: String,
) {
    private val logger = KotlinLogging.logger {}

    private lateinit var checksDirectory: File
    private var checksAvailable: Boolean = false
    private lateinit var availableChecks: List<String>

    @PostConstruct
    fun init() {
        logger.info { "Initializing ChecksInfoService..." }

        if (checksPath.isBlank()) {
            logger.warn { "docs.checks.path is not configured. Check descriptions will not be available." }
            checksAvailable = false
            availableChecks = emptyList()
            return
        }

        checksDirectory = File(checksPath)

        if (!checksDirectory.exists() || !checksDirectory.isDirectory) {
            logger.warn { "Checks directory not found: $checksPath. Check descriptions will not be available." }
            checksAvailable = false
            availableChecks = emptyList()
            return
        }

        availableChecks = checksDirectory
            .listFiles { file -> file.isFile && file.extension.lowercase() == "md" }
            ?.map { it.nameWithoutExtension }
            ?.sorted()
            ?: emptyList()

        checksAvailable = availableChecks.isNotEmpty()

        logger.info { "ChecksInfoService initialized. Found ${availableChecks.size} check descriptions." }
    }

    /**
     * Check if checks are available.
     *
     * @return true if checks directory is configured and contains files
     */
    fun isAvailable(): Boolean = checksAvailable

    /**
     * Get check description by check code.
     *
     * @param code Check code (e.g., "begin-transaction", "empty-except-statement")
     * @return Check description content or error message if not found
     */
    fun getCheckInfo(code: String): String {
        if (!checksAvailable) {
            return "❌ Check descriptions are not available. Configure 'docs.checks.path' to enable this feature."
        }

        val normalizedCode = code.lowercase().trim()
        val checkFile = File(checksDirectory, "$normalizedCode.md")

        if (!checkFile.exists() || !checkFile.isFile) {
            // Try to find similar checks
            val similar = findSimilarChecks(normalizedCode, 5)
            return if (similar.isNotEmpty()) {
                """❌ Check '$code' not found.

**Did you mean:**
${similar.joinToString("\n") { "- `$it`" }}

Use `listChecks()` to see all available checks."""
            } else {
                "❌ Check '$code' not found. Use `listChecks()` to see all available checks."
            }
        }

        return try {
            checkFile.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            logger.error(e) { "Failed to read check file: ${checkFile.absolutePath}" }
            "❌ Error reading check description: ${e.message}"
        }
    }

    /**
     * Search checks by keyword in their codes.
     *
     * @param query Search query
     * @return List of matching checks with their codes
     */
    fun searchChecks(query: String): String {
        if (!checksAvailable) {
            return "❌ Check descriptions are not available. Configure 'docs.checks.path' to enable this feature."
        }

        if (query.isBlank()) {
            return "❌ Search query cannot be empty"
        }

        val queryLower = query.lowercase()
        val matchingChecks = availableChecks.filter { it.contains(queryLower) }

        return if (matchingChecks.isEmpty()) {
            // Try fuzzy search
            val similar = findSimilarChecks(queryLower, 10)
            if (similar.isNotEmpty()) {
                """# Search Results for '$query'

No exact matches found. **Similar checks:**
${similar.joinToString("\n") { "- `$it`" }}"""
            } else {
                "❌ No checks found matching '$query'"
            }
        } else {
            """# Search Results for '$query'

Found ${matchingChecks.size} matching checks:
${matchingChecks.joinToString("\n") { "- `$it`" }}

Use `getCheckInfo(code)` to get detailed information about a specific check."""
        }
    }

    /**
     * Get list of all available checks.
     *
     * @return Formatted list of all check codes
     */
    fun listChecks(): String {
        if (!checksAvailable) {
            return "❌ Check descriptions are not available. Configure 'docs.checks.path' to enable this feature."
        }

        // Group checks by prefix for better organization
        val grouped = availableChecks.groupBy { check ->
            val parts = check.split("-")
            if (parts.size > 1) parts[0] else "other"
        }

        val result = StringBuilder()
        result.appendLine("# Available BSL Checks")
        result.appendLine()
        result.appendLine("Total: ${availableChecks.size} checks")
        result.appendLine()

        grouped.toSortedMap().forEach { (prefix, checks) ->
            result.appendLine("## ${prefix.replaceFirstChar { it.uppercase() }}")
            checks.forEach { check ->
                result.appendLine("- `$check`")
            }
            result.appendLine()
        }

        result.appendLine("Use `getCheckInfo(code)` to get detailed information about a specific check.")

        return result.toString()
    }

    /**
     * Find checks similar to the given query using simple fuzzy matching.
     */
    private fun findSimilarChecks(query: String, limit: Int): List<String> {
        return availableChecks
            .map { check -> check to calculateSimilarity(query, check) }
            .filter { it.second > 0.3 }
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }

    /**
     * Calculate similarity between two strings (simple algorithm).
     */
    private fun calculateSimilarity(s1: String, s2: String): Double {
        // Check if s1 is substring of s2
        if (s2.contains(s1)) return 1.0

        // Check word matches
        val words1 = s1.split("-", "_", " ")
        val words2 = s2.split("-", "_", " ")
        val commonWords = words1.count { word -> words2.any { it.contains(word) || word.contains(it) } }

        if (commonWords > 0) {
            return commonWords.toDouble() / maxOf(words1.size, words2.size)
        }

        // Calculate character overlap
        val chars1 = s1.toSet()
        val chars2 = s2.toSet()
        val intersection = chars1.intersect(chars2).size
        val union = chars1.union(chars2).size

        return if (union > 0) intersection.toDouble() / union else 0.0
    }
}
