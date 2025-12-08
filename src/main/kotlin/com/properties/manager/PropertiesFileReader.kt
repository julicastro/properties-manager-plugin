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
        private const val PROPERTIES_PATH = "src/main/resources/properties/app.properties"
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
     *
     * @return VirtualFile if found, null otherwise
     */
    fun findPropertiesFile(): VirtualFile? {
        val basePath = project.basePath ?: return null
        val propertiesFile = project.baseDir?.findFileByRelativePath(PROPERTIES_PATH)
        return propertiesFile
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
     * Groups properties by numeric prefix (e.g., "1.", "2.", etc.).
     * Preserves comments and original order within each group.
     *
     * @param entries List of property entries
     * @return List of property groups
     */
    fun groupProperties(entries: List<PropertyEntry>): List<PropertyGroup> {
        val groupsWithComments = mutableMapOf<String, MutableList<PropertyEntry>>()
        var lastComment: PropertyEntry? = null
        
        entries.forEach { entry ->
            if (entry.isComment) {
                // Store comment to associate with next property
                lastComment = entry
            } else if (entry.key.isNotEmpty()) {
                val prefix = extractPrefix(entry.key)
                val list = groupsWithComments.getOrPut(prefix) { mutableListOf() }
                
                // Add comment if exists before this property
                if (lastComment != null && lastComment!!.rawLine.trim().isNotEmpty()) {
                    list.add(lastComment!!)
                    lastComment = null
                }
                
                // Add the property
                list.add(entry)
            }
        }
        
        // Sort groups: no prefix first, then numeric prefixes
        val sortedKeys = groupsWithComments.keys.sortedWith(compareBy { key ->
            if (key.isEmpty()) -1
            else key.removeSuffix(".").toIntOrNull() ?: Int.MAX_VALUE
        })
        
        return sortedKeys.map { key ->
            PropertyGroup(
                prefix = if (key.isEmpty()) "Sin prefijo" else key,
                entries = groupsWithComments[key]!!.sortedBy { it.lineNumber }
            )
        }
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

