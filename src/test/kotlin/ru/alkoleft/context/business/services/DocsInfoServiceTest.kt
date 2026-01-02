/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.business.services

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("DocsInfoService Tests")
class DocsInfoServiceTest {
    private lateinit var service: DocsInfoService

    @BeforeEach
    fun setUp() {
        // Use empty paths to fall back to classpath resources
        service =
            DocsInfoService(
                strictTypesPath = "",
                guidelinePath = "",
            )
        service.init()
    }

    @Test
    @DisplayName("getGuideline should return guideline content")
    fun testGetGuideline() {
        val result = service.getGuideline()

        assertTrue(result.isNotEmpty())
        assertContains(result, "constructor")
    }

    @Test
    @DisplayName("getStrictTypingInfo should return topic content for valid topic")
    fun testGetStrictTypingInfoValidTopic() {
        val result = service.getStrictTypingInfo("overview")

        assertTrue(result.isNotEmpty())
        assertFalse(result.startsWith("❌"))
        assertContains(result, "Strict Typing")
    }

    @Test
    @DisplayName("getStrictTypingInfo should return error for invalid topic")
    fun testGetStrictTypingInfoInvalidTopic() {
        val result = service.getStrictTypingInfo("nonexistent-topic")

        assertTrue(result.startsWith("❌"))
        assertContains(result, "not found")
    }

    @Test
    @DisplayName("getStrictTypingInfo should be case-insensitive")
    fun testGetStrictTypingInfoCaseInsensitive() {
        val result1 = service.getStrictTypingInfo("OVERVIEW")
        val result2 = service.getStrictTypingInfo("Overview")
        val result3 = service.getStrictTypingInfo("overview")

        assertEquals(result1, result2)
        assertEquals(result2, result3)
    }

    @Test
    @DisplayName("getStrictTypingInfo for arrays should contain array examples")
    fun testGetStrictTypingInfoArrays() {
        val result = service.getStrictTypingInfo("arrays")

        assertFalse(result.startsWith("❌"))
        assertContains(result, "Array")
    }

    @Test
    @DisplayName("getStrictTypingInfo for value-table should contain ValueTable examples")
    fun testGetStrictTypingInfoValueTable() {
        val result = service.getStrictTypingInfo("value-table")

        assertFalse(result.startsWith("❌"))
        assertContains(result, "ValueTable")
    }

    @Test
    @DisplayName("getStrictTypingInfo for structure-keys should contain Structure examples")
    fun testGetStrictTypingInfoStructureKeys() {
        val result = service.getStrictTypingInfo("structure-keys")

        assertFalse(result.startsWith("❌"))
        assertContains(result, "Structure")
    }

    @Test
    @DisplayName("searchStrictTyping should find matching topics")
    fun testSearchStrictTyping() {
        val result = service.searchStrictTyping("ValueTable")

        assertFalse(result.startsWith("❌"))
        assertContains(result, "Search Results")
        assertContains(result, "ValueTable")
    }

    @Test
    @DisplayName("searchStrictTyping should return error for empty query")
    fun testSearchStrictTypingEmptyQuery() {
        val result = service.searchStrictTyping("")

        assertTrue(result.startsWith("❌"))
        assertContains(result, "empty")
    }

    @Test
    @DisplayName("searchStrictTyping should return no results message for non-matching query")
    fun testSearchStrictTypingNoResults() {
        val result = service.searchStrictTyping("xyznonexistentterm123")

        assertTrue(result.startsWith("❌"))
        assertContains(result, "No results")
    }

    @Test
    @DisplayName("getAvailableTopics should list all topics")
    fun testGetAvailableTopics() {
        val result = service.getAvailableTopics()

        assertContains(result, "overview")
        assertContains(result, "arrays")
        assertContains(result, "value-table")
        assertContains(result, "structure-keys")
        assertContains(result, "constructor-functions")
    }

    @Test
    @DisplayName("All documented topics should be available")
    fun testAllDocumentedTopicsAvailable() {
        val expectedTopics =
            listOf(
                "overview",
                "enabling",
                "calculated-typing",
                "declarative-typing",
                "type-links",
                "local-variables",
                "module-variables",
                "structure-keys",
                "arrays",
                "value-table",
                "table-row",
                "map",
                "value-list",
                "constructor-functions",
                "type-narrowing",
                "forms",
                "temp-storage",
                "string-literals",
                "export-methods",
                "query-results",
                "composite-types",
                "documentation-syntax",
                "diagnostics",
            )

        for (topic in expectedTopics) {
            val result = service.getStrictTypingInfo(topic)
            assertFalse(result.startsWith("❌"), "Topic '$topic' should be available but returned: $result")
        }
    }
}
