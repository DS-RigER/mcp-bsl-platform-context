/*
 * Copyright (c) 2025 alkoleft. All rights reserved.
 * This file is part of the mcp-bsl-context project.
 *
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 */

package ru.alkoleft.context.infrastructure.export.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * Корневая модель для экспорта контекста платформы в JSON.
 */
data class PlatformContextExport(
    @JsonProperty("version") val version: String,
    @JsonProperty("generatedAt") val generatedAt: Instant,
    @JsonProperty("globalMethods") val globalMethods: List<MethodExport>,
    @JsonProperty("globalProperties") val globalProperties: List<PropertyExport>,
    @JsonProperty("types") val types: List<TypeExport>,
    @JsonProperty("enums") val enums: List<EnumExport>,
    @JsonProperty("methodsByName") val methodsByName: Map<String, List<String>>,
    @JsonProperty("propertiesByName") val propertiesByName: Map<String, List<String>>,
)

/**
 * Экспортируемый тип (объект) платформы.
 */
data class TypeExport(
    @JsonProperty("nameRu") val nameRu: String,
    @JsonProperty("nameEn") val nameEn: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("methods") val methods: List<MethodExport>,
    @JsonProperty("properties") val properties: List<PropertyExport>,
    @JsonProperty("constructors") val constructors: List<ConstructorExport>,
)

/**
 * Экспортируемый метод.
 */
data class MethodExport(
    @JsonProperty("nameRu") val nameRu: String,
    @JsonProperty("nameEn") val nameEn: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("returnType") val returnType: String?,
    @JsonProperty("signatures") val signatures: List<SignatureExport>,
)

/**
 * Экспортируемая сигнатура метода.
 */
data class SignatureExport(
    @JsonProperty("name") val name: String,
    @JsonProperty("syntax") val syntax: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("parameters") val parameters: List<ParameterExport>,
)

/**
 * Экспортируемый параметр.
 */
data class ParameterExport(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("required") val required: Boolean,
    @JsonProperty("defaultValue") val defaultValue: String?,
)

/**
 * Экспортируемое свойство.
 */
data class PropertyExport(
    @JsonProperty("nameRu") val nameRu: String,
    @JsonProperty("nameEn") val nameEn: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("readOnly") val readOnly: Boolean,
)

/**
 * Экспортируемый конструктор.
 */
data class ConstructorExport(
    @JsonProperty("name") val name: String,
    @JsonProperty("syntax") val syntax: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("parameters") val parameters: List<ParameterExport>,
)

/**
 * Экспортируемое перечисление.
 */
data class EnumExport(
    @JsonProperty("nameRu") val nameRu: String,
    @JsonProperty("nameEn") val nameEn: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("values") val values: List<EnumValueExport>,
)

/**
 * Экспортируемое значение перечисления.
 */
data class EnumValueExport(
    @JsonProperty("nameRu") val nameRu: String,
    @JsonProperty("nameEn") val nameEn: String,
    @JsonProperty("description") val description: String,
)
