/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.hbk.parsers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import ru.alkoleft.context.infrastructure.hbk.parsers.specialized.MethodPageParser
import java.io.File
import kotlin.test.assertFalse

class MethodPageParserTest {
    @Test
    fun `test parse BeginGetFileFromServer - English documentation`() {
        val parser = MethodPageParser()
        val file = File("src/test/resources/global-methods/BeginGetFileFromServer.html")
        val result = parser.parse(file.inputStream())

        assertEquals("BeginGetFileFromServer", result.nameRu) // In English doc both names are same
        assertEquals("BeginGetFileFromServer", result.nameEn)
        assertEquals(2, result.signatures.size)

        // Check for two signatures with correct names
        assertTrue(result.signatures.any { it.name == "With dialog" })
        assertTrue(result.signatures.any { it.name == "Without a dialog" })

        // Find signatures by name
        val signature1 = result.signatures.first { it.name == "With dialog" }
        val signature2 = result.signatures.first { it.name == "Without a dialog" }

        // Check syntax
        assertEquals(
            "BeginGetFileFromServer(<Address>, <FileName>, <GettingFilesDialogParameters>)",
            signature1.syntax,
        )
        assertEquals(
            "BeginGetFileFromServer(<NotifyDescriptionOnCompletion>, <Address>, <PathToFile>)",
            signature2.syntax,
        )

        // Check descriptions exist
        assertNotNull(signature1.description)
        assertNotNull(signature2.description)

        // Check first signature parameters
        assertEquals(3, signature1.parameters.size)
        assertEquals("Address", signature1.parameters[0].name)
        assertEquals("String", signature1.parameters[0].type)
        assertFalse { signature1.parameters[0].isOptional }

        assertEquals("FileName", signature1.parameters[1].name)
        assertEquals("String", signature1.parameters[1].type)
        assertTrue { signature1.parameters[1].isOptional }

        assertEquals("GettingFilesDialogParameters", signature1.parameters[2].name)
        assertEquals("GetFilesDialogParameters", signature1.parameters[2].type)
        assertTrue { signature1.parameters[2].isOptional }

        // Check second signature parameters
        assertEquals(3, signature2.parameters.size)
        assertEquals("NotifyDescriptionOnCompletion", signature2.parameters[0].name)
        assertEquals("CallbackDescription", signature2.parameters[0].type)
        assertTrue { signature2.parameters[0].isOptional }
        assertTrue { signature2.parameters[0].description.contains("Contains the description of the procedure") }

        assertEquals("Address", signature2.parameters[1].name)
        assertEquals("String", signature2.parameters[1].type)
        assertFalse { signature2.parameters[1].isOptional }

        assertEquals("PathToFile", signature2.parameters[2].name)
        assertEquals("String", signature2.parameters[2].type)
        assertFalse { signature2.parameters[2].isOptional }

        assertNull(result.example)
    }

    @Test
    fun `test parse GetCommonTemplate - English documentation`() {
        val parser = MethodPageParser()
        val file = File("src/test/resources/global-methods/GetCommonTemplate.html")
        val result = parser.parse(file.inputStream())

        assertEquals("GetCommonTemplate", result.nameRu)
        assertEquals("GetCommonTemplate", result.nameEn)

        // Check that there is at least one signature
        assertTrue(result.signatures.isNotEmpty())

        val signature = result.signatures[0]
        assertEquals("GetCommonTemplate(<CommonTemplate>)", signature.syntax)

        // Check parameters
        assertEquals(1, signature.parameters.size)
        assertEquals("CommonTemplate", signature.parameters[0].name)
        assertFalse { signature.parameters[0].isOptional }

        // Check return value
        assertNotNull(result.returnValue)
        assertTrue { result.returnValue!!.type.isNotEmpty() }
    }

    @Test
    fun `test parse AttachAddIn - English documentation`() {
        val parser = MethodPageParser()
        val file = File("src/test/resources/global-methods/AttachAddIn.html")
        val result = parser.parse(file.inputStream())

        assertEquals("AttachAddIn", result.nameRu)
        assertEquals("AttachAddIn", result.nameEn)
        assertTrue(result.signatures.isNotEmpty())
    }

    @Test
    fun `test parse BeginTransaction - English documentation`() {
        val parser = MethodPageParser()
        val file = File("src/test/resources/global-methods/BeginTransaction.html")
        val result = parser.parse(file.inputStream())

        assertEquals("BeginTransaction", result.nameRu)
        assertEquals("BeginTransaction", result.nameEn)
        assertTrue(result.signatures.isNotEmpty())
    }

    @Test
    fun `test parse NumberInWords - English documentation`() {
        val parser = MethodPageParser()
        val file = File("src/test/resources/global-methods/NumberInWords.html")
        val result = parser.parse(file.inputStream())

        assertEquals("NumberInWords", result.nameRu)
        assertEquals("NumberInWords", result.nameEn)
        assertTrue(result.signatures.isNotEmpty())
    }

    @Test
    fun `test parse StrTemplate - English documentation`() {
        val parser = MethodPageParser()
        val file = File("src/test/resources/global-methods/StrTemplate.html")
        val result = parser.parse(file.inputStream())

        assertEquals("StrTemplate", result.nameRu)
        assertEquals("StrTemplate", result.nameEn)
        assertTrue(result.signatures.isNotEmpty())
    }

    @Test
    fun `test parse InputDate - English documentation`() {
        val parser = MethodPageParser()
        val file = File("src/test/resources/global-methods/InputDate.html")
        val result = parser.parse(file.inputStream())

        assertEquals("InputDate", result.nameRu)
        assertEquals("InputDate", result.nameEn)
        assertTrue(result.signatures.isNotEmpty())
    }
}
