package com.properties.manager

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JPanel

/**
 * Factory for creating the Properties Manager tool window.
 */
class PropertiesToolWindowFactory : ToolWindowFactory {
    
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val propertiesToolWindow = PropertiesToolWindow(project)
        val content = ContentFactory.getInstance().createContent(
            propertiesToolWindow.getContent(),
            "",
            false
        )
        toolWindow.contentManager.addContent(content)
    }
    
    /**
     * Tool window content with checkboxes for properties.
     */
    private class PropertiesToolWindow(private val project: Project) {
        
        private val fileReader = PropertiesFileReader(project)
        private val fileUpdater = PropertiesFileUpdater(project)
        private val mainPanel = JPanel(BorderLayout())
        
        init {
            createUI()
        }
        
        fun getContent(): JPanel = mainPanel
        
        /**
         * Creates the UI with checkboxes grouped by prefix.
         */
        private fun createUI() {
            val propertiesFile = fileReader.findPropertiesFile()
            
            if (propertiesFile == null) {
                showNotFoundMessage()
                return
            }
            
            val entries = fileReader.readProperties(propertiesFile)
            val groups = fileReader.groupProperties(entries)
            
            val contentPanel = JPanel()
            contentPanel.layout = GridBagLayout()
            contentPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            
            val gbc = GridBagConstraints()
            gbc.gridx = 0
            gbc.gridy = 0
            gbc.anchor = GridBagConstraints.WEST
            gbc.fill = GridBagConstraints.HORIZONTAL
            gbc.weightx = 1.0
            gbc.insets = Insets(2, 5, 2, 5)
            
            // Add groups
            groups.forEach { group ->
                // Collect property entries and checkboxes for this group
                data class PropertyCheckbox(val entry: PropertiesFileReader.PropertyEntry, val checkbox: JBCheckBox)
                val propertyCheckboxes = mutableListOf<PropertyCheckbox>()
                
                // Properties and comments in group
                group.entries.forEach { entry ->
                    if (entry.isComment) {
                        // Show comment with master checkbox
                        val commentText = entry.rawLine.trim().removePrefix("#").trim()
                        if (commentText.isNotEmpty()) {
                            val commentPanel = JPanel(BorderLayout())
                            commentPanel.border = BorderFactory.createEmptyBorder(10, 0, 2, 0)
                            commentPanel.isOpaque = false
                            
                            // Master checkbox to toggle all properties in this group
                            val masterCheckbox = JBCheckBox()
                            masterCheckbox.addActionListener {
                                val isSelected = masterCheckbox.isSelected
                                // Update all properties in this group
                                propertyCheckboxes.forEach { (propEntry, cb) ->
                                    cb.isSelected = isSelected
                                    val newValue = if (isSelected) {
                                        PropertyValueResolver.resolveValue(propEntry.key)
                                    } else {
                                        PropertyValueResolver.resolveEmptyValue()
                                    }
                                    fileUpdater.updateProperty(propertiesFile, propEntry.key, newValue)
                                }
                            }
                            
                            // Comment label
                            val commentLabel = JBLabel("<html><b>$commentText</b></html>")
                            commentLabel.foreground = java.awt.Color(200, 200, 200)
                            
                            commentPanel.add(masterCheckbox, BorderLayout.WEST)
                            commentPanel.add(commentLabel, BorderLayout.CENTER)
                            
                            contentPanel.add(commentPanel, gbc)
                            gbc.gridy++
                        }
                    } else {
                        // Show checkbox for property
                        val checkbox = JBCheckBox(entry.key)
                        checkbox.isSelected = entry.value.isNotEmpty()
                        checkbox.border = BorderFactory.createEmptyBorder(0, 20, 0, 0)
                        
                        checkbox.addActionListener {
                            val newValue = if (checkbox.isSelected) {
                                PropertyValueResolver.resolveValue(entry.key)
                            } else {
                                PropertyValueResolver.resolveEmptyValue()
                            }
                            fileUpdater.updateProperty(propertiesFile, entry.key, newValue)
                        }
                        
                        propertyCheckboxes.add(PropertyCheckbox(entry, checkbox))
                        contentPanel.add(checkbox, gbc)
                        gbc.gridy++
                    }
                }
                
                // Add spacing between groups
                contentPanel.add(Box.createVerticalStrut(5), gbc)
                gbc.gridy++
            }
            
            // Add vertical glue to push everything to the top
            gbc.weighty = 1.0
            gbc.fill = GridBagConstraints.BOTH
            contentPanel.add(Box.createVerticalGlue(), gbc)
            
            // Add refresh button at the top
            val topPanel = JPanel(BorderLayout())
            val refreshButton = JButton("Refrescar")
            refreshButton.addActionListener {
                mainPanel.removeAll()
                createUI()
                mainPanel.revalidate()
                mainPanel.repaint()
            }
            topPanel.add(refreshButton, BorderLayout.EAST)
            topPanel.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
            
            // Add scroll pane
            val scrollPane = JBScrollPane(contentPanel)
            scrollPane.border = BorderFactory.createEmptyBorder()
            
            mainPanel.add(topPanel, BorderLayout.NORTH)
            mainPanel.add(scrollPane, BorderLayout.CENTER)
        }
        
        /**
         * Shows message when properties file is not found.
         */
        private fun showNotFoundMessage() {
            val messagePanel = JPanel(BorderLayout())
            val label = JBLabel(
                "<html><center>No se encontró el archivo de properties<br/>" +
                "<br/>Rutas buscadas:<br/>" +
                "• test-project/src/main/resources/properties/app.properties<br/>" +
                "• core/app-shared/shared/config/src/main/resources/category.properties</center></html>"
            )
            label.horizontalAlignment = JBLabel.CENTER
            messagePanel.add(label, BorderLayout.CENTER)
            mainPanel.add(messagePanel, BorderLayout.CENTER)
        }
    }
}

