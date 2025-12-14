package com.properties.manager

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Reads and parses app.properties files from the project.
 */
class PropertiesFileReader(private val project: Project) {
    
    companion object {
        // Multiple possible paths to search for properties files
        private val PROPERTIES_PATHS = listOf(
            "test-project/src/main/resources/properties/app.properties",  // Test project path
            "core/app-shared/shared/config/src/main/resources/category.properties"  // Category properties path
        )
    }
    
    /**
     * Property entry containing key, value and metadata.
     */
    data class PropertyEntry(
        val key: String,
        val value: String,
        val lineNumber: Int,
        val isComment: Boolean = false,
        val rawLine: String = ""
    )
    
    /**
     * Grouped properties by prefix.
     */
    data class PropertyGroup(
        val prefix: String,
        val entries: List<PropertyEntry>
    )
    
    /**
     * Finds the app.properties file in the project.
     * Searches in multiple possible locations.
     *
     * @return VirtualFile if found, null otherwise
     */
    fun findPropertiesFile(): VirtualFile? {
        val baseDir = project.baseDir ?: return null
        
        // Try each possible path
        for (path in PROPERTIES_PATHS) {
            val file = baseDir.findFileByRelativePath(path)
            if (file != null && file.exists()) {
                return file
            }
        }
        
        return null
    }
    
    /**
     * Reads all properties from the file.
     *
     * @param file The properties file
     * @return List of property entries
     */
    fun readProperties(file: VirtualFile): List<PropertyEntry> {
        val entries = mutableListOf<PropertyEntry>()
        var lineNumber = 0
        
        BufferedReader(InputStreamReader(file.inputStream, Charsets.UTF_8)).use { reader ->
            reader.forEachLine { line ->
                lineNumber++
                val trimmedLine = line.trim()
                
                when {
                    trimmedLine.isEmpty() || trimmedLine.startsWith("#") -> {
                        // Comment or empty line
                        entries.add(PropertyEntry("", "", lineNumber, isComment = true, rawLine = line))
                    }
                    trimmedLine.contains("=") -> {
                        // Property line
                        val parts = trimmedLine.split("=", limit = 2)
                        val key = parts[0].trim()
                        val value = if (parts.size > 1) parts[1].trim() else ""
                        entries.add(PropertyEntry(key, value, lineNumber, rawLine = line))
                    }
                    else -> {
                        // Unknown format, keep as comment
                        entries.add(PropertyEntry("", "", lineNumber, isComment = true, rawLine = line))
                    }
                }
            }
        }
        
        return entries
    }
    
    /**
     * Groups properties by comment blocks.
     * Each comment starts a new group with all properties following it until the next comment.
     *
     * @param entries List of property entries
     * @return List of property groups
     */
    fun groupProperties(entries: List<PropertyEntry>): List<PropertyGroup> {
        val groups = mutableListOf<PropertyGroup>()
        val currentGroupEntries = mutableListOf<PropertyEntry>()
        var groupName = "Sin prefijo"
        var groupIndex = 0
        
        entries.forEach { entry ->
            if (entry.isComment && entry.rawLine.trim().isNotEmpty()) {
                // Found a comment - if we have accumulated entries, save them as a group
                if (currentGroupEntries.isNotEmpty()) {
                    groups.add(PropertyGroup(
                        prefix = groupName,
                        entries = currentGroupEntries.toList()
                    ))
                    currentGroupEntries.clear()
                    groupIndex++
                }
                
                // Start new group with this comment
                val commentText = entry.rawLine.trim().removePrefix("#").trim()
                groupName = commentText.ifEmpty { "Grupo $groupIndex" }
                currentGroupEntries.add(entry)
            } else if (!entry.isComment && entry.key.isNotEmpty()) {
                // Add property to current group
                currentGroupEntries.add(entry)
            }
        }
        
        // Add last group if not empty
        if (currentGroupEntries.isNotEmpty()) {
            groups.add(PropertyGroup(
                prefix = groupName,
                entries = currentGroupEntries.toList()
            ))
        }
        
        return groups
    }
    
    /**
     * Extracts the numeric prefix from a property key.
     *
     * @param key Property key
     * @return Prefix string (e.g., "1." or empty)
     */
    private fun extractPrefix(key: String): String {
        val regex = Regex("^(\\d+)\\..*")
        val matchResult = regex.find(key)
        return matchResult?.groupValues?.get(1)?.plus(".") ?: ""
    }
}

