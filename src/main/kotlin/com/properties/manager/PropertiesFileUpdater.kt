package com.properties.manager

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Updates app.properties file while preserving comments and structure.
 */
class PropertiesFileUpdater(private val project: Project) {
    
    /**
     * Updates a property value in the file.
     *
     * @param file The properties file
     * @param key The property key to update
     * @param newValue The new value to set
     */
    fun updateProperty(file: VirtualFile, key: String, newValue: String) {
        ApplicationManager.getApplication().invokeLater {
            WriteCommandAction.runWriteCommandAction(project) {
                val updatedContent = updatePropertyInContent(file, key, newValue)
                file.setBinaryContent(updatedContent.toByteArray(Charsets.UTF_8))
            }
        }
    }
    
    /**
     * Updates property value in file content while preserving structure.
     *
     * @param file The properties file
     * @param key The property key to update
     * @param newValue The new value
     * @return Updated file content
     */
    private fun updatePropertyInContent(file: VirtualFile, key: String, newValue: String): String {
        val lines = mutableListOf<String>()
        
        BufferedReader(InputStreamReader(file.inputStream, Charsets.UTF_8)).use { reader ->
            reader.forEachLine { line ->
                val trimmedLine = line.trim()
                
                if (trimmedLine.startsWith("$key=") || trimmedLine == key) {
                    // Found the property, update it
                    val updatedLine = if (newValue.isEmpty()) {
                        "$key="
                    } else {
                        "$key=$newValue"
                    }
                    lines.add(updatedLine)
                } else {
                    // Keep line as is
                    lines.add(line)
                }
            }
        }
        
        return lines.joinToString("\n")
    }
    
    /**
     * Updates multiple properties at once.
     *
     * @param file The properties file
     * @param updates Map of key-value pairs to update
     */
    fun updateMultipleProperties(file: VirtualFile, updates: Map<String, String>) {
        ApplicationManager.getApplication().invokeLater {
            WriteCommandAction.runWriteCommandAction(project) {
                var content = String(file.contentsToByteArray(), Charsets.UTF_8)
                
                updates.forEach { (key, newValue) ->
                    content = updatePropertyInContentString(content, key, newValue)
                }
                
                file.setBinaryContent(content.toByteArray(Charsets.UTF_8))
            }
        }
    }
    
    /**
     * Updates property in a string content.
     *
     * @param content File content as string
     * @param key Property key
     * @param newValue New value
     * @return Updated content
     */
    private fun updatePropertyInContentString(content: String, key: String, newValue: String): String {
        val lines = content.split("\n").toMutableList()
        
        for (i in lines.indices) {
            val trimmedLine = lines[i].trim()
            if (trimmedLine.startsWith("$key=") || trimmedLine == key) {
                lines[i] = if (newValue.isEmpty()) {
                    "$key="
                } else {
                    "$key=$newValue"
                }
                break
            }
        }
        
        return lines.joinToString("\n")
    }
}

