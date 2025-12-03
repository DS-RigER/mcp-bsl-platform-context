/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.hbk.parsers.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BlockHandlerTest {
    private lateinit var nameHandler: NameBlockHandler
    private lateinit var syntaxHandler: SyntaxBlockHandler
    private lateinit var parametersHandler: ParametersBlockHandler
    private lateinit var descriptionHandler: DescriptionBlockHandler

    @BeforeEach
    fun setUp() {
        nameHandler = NameBlockHandler()
        syntaxHandler = SyntaxBlockHandler()
        parametersHandler = ParametersBlockHandler()
        descriptionHandler = DescriptionBlockHandler()
    }

    @Test
    fun `test NameBlockHandler parses name with English translation`() {
        nameHandler.onOpenTag("p", mapOf("class" to "V8SH_heading"), false)
        nameHandler.onText("Массив (Array)")
        nameHandler.onCloseTag("p", false)

        val result = nameHandler.getResult()
        assertEquals("Массив", result.first)
        assertEquals("Array", result.second)
    }

    @Test
    fun `test NameBlockHandler parses name without English translation`() {
        nameHandler.onOpenTag("p", mapOf("class" to "V8SH_title"), false)
        nameHandler.onText("Array")
        nameHandler.onCloseTag("p", false)

        val result = nameHandler.getResult()
        // For English doc without parentheses, both names are same
        assertEquals("Array", result.first)
        assertEquals("Array", result.second)
    }

    @Test
    fun `test NameBlockHandler readName method with parentheses`() {
        val result = nameHandler.readName("Массив (Array)")
        assertEquals("Массив", result.first)
        assertEquals("Array", result.second)
    }

    @Test
    fun `test NameBlockHandler readName method without parentheses`() {
        val result = nameHandler.readName("Array")
        // For English doc without parentheses, both names are same
        assertEquals("Array", result.first)
        assertEquals("Array", result.second)
    }

    @Test
    fun `test SyntaxBlockHandler accumulates syntax text`() {
        syntaxHandler.onText("Новый Массив(")
        syntaxHandler.onText("<Количество>)")
        syntaxHandler.onText("")

        val result = syntaxHandler.getResult()
        assertEquals("Новый Массив(<Количество>)", result)
    }

    @Test
    fun `test SyntaxBlockHandler trims whitespace`() {
        syntaxHandler.onText("  Новый Массив()  ")

        val result = syntaxHandler.getResult()
        assertEquals("Новый Массив()", result)
    }

    @Test
    fun `test ParametersBlockHandler parses single parameter`() {
        // Simulate HTML structure of a parameter (English documentation)
        parametersHandler.onOpenTag("div", mapOf("class" to "V8SH_rubric"), false)
        parametersHandler.onText("<Count> (optional)")
        parametersHandler.onCloseTag("div", false)

        parametersHandler.onText("Type: ")

        parametersHandler.onOpenTag("a", mapOf("href" to "index.html"), false)
        parametersHandler.onText("Arbitrary")
        parametersHandler.onCloseTag("a", false)
        parametersHandler.onText(".")

        parametersHandler.onOpenTag("br", emptyMap(), true)
        parametersHandler.onText("Parameter description")

        val result = parametersHandler.getResult()
        assertEquals(1, result.size)

        val parameter = result[0]
        assertEquals("Count", parameter.name)
        assertEquals("Arbitrary", parameter.type)
        assertTrue(parameter.isOptional)
        assertEquals("Parameter description", parameter.description)
    }

    @Test
    fun `test ParametersBlockHandler parses parameter without linked type`() {
        // Simulate HTML structure of a parameter (English documentation)
        parametersHandler.onOpenTag("div", mapOf("class" to "V8SH_rubric"), false)
        parametersHandler.onText("<Count> (optional)")
        parametersHandler.onCloseTag("div", false)

        parametersHandler.onText("Type: Arbitrary.")

        parametersHandler.onOpenTag("br", emptyMap(), true)
        parametersHandler.onText("Parameter description")

        val result = parametersHandler.getResult()
        assertEquals(1, result.size)

        val parameter = result[0]
        assertEquals("Count", parameter.name)
        assertEquals("Arbitrary", parameter.type)
        assertTrue(parameter.isOptional)
        assertEquals("Parameter description", parameter.description)
    }

    @Test
    fun `test DescriptionBlockHandler accumulates description text`() {
        descriptionHandler.onText("Это описание ")
        descriptionHandler.onText("метода или свойства")
        descriptionHandler.onText(".")

        val result = descriptionHandler.getResult()
        assertEquals("Это описание метода или свойства.", result)
    }

    @Test
    fun `test DescriptionBlockHandler handles markdown formatting`() {
        descriptionHandler.onOpenTag("code", emptyMap(), false)
        descriptionHandler.onText("код")
        descriptionHandler.onCloseTag("code", false)
        descriptionHandler.onText(" в описании")

        val result = descriptionHandler.getResult()
        assertEquals("`код` в описании", result)
    }
}
