package com.properties.manager

/**
 * Resolves property values based on property key naming conventions.
 */
object PropertyValueResolver {
    
    /**
     * Determines the appropriate value for a property key when checked.
     *
     * @param key The property key
     * @return The auto-completed value based on naming rules
     */
    fun resolveValue(key: String): String {
        return when {
            key.contains(".site", ignoreCase = true) -> "MLA"
            key.contains(".user", ignoreCase = true) -> "1,2,3,4,5,6,7,8,9,0"
            key.contains(".app", ignoreCase = true) -> "LIST,LIST_WEBVIEW"
            else -> ""
        }
    }
    
    /**
     * Returns empty value for unchecked properties.
     *
     * @return Empty string
     */
    fun resolveEmptyValue(): String {
        return ""
    }
}

