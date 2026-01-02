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

/**
 * Tests for ChecksInfoService.
 */
@DisplayName("ChecksInfoService Tests")
class ChecksInfoServiceTest {
    private lateinit var service: ChecksInfoService

    @BeforeEach
    fun setUp() {
        // Use empty path to test unavailable state
        service =
            ChecksInfoService(
                checksPath = "",
            )
        service.init()
    }

    @Test
    @DisplayName("isAvailable should return false when path is empty")
    fun testIsAvailableWithEmptyPath() {
        assertFalse(service.isAvailable())
    }

    @Test
    @DisplayName("getCheckInfo should return error when path is empty")
    fun testGetCheckInfoWithEmptyPath() {
        val result = service.getCheckInfo("begin-transaction")

        assertContains(result, "❌")
        assertContains(result, "not available")
    }

    @Test
    @DisplayName("searchChecks should return error when path is empty")
    fun testSearchChecksWithEmptyPath() {
        val result = service.searchChecks("transaction")

        assertContains(result, "❌")
        assertContains(result, "not available")
    }

    @Test
    @DisplayName("listChecks should return error when path is empty")
    fun testListChecksWithEmptyPath() {
        val result = service.listChecks()

        assertContains(result, "❌")
        assertContains(result, "not available")
    }
}
