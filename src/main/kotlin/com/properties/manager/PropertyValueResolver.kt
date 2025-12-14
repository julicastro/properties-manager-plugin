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
            key.contains(".sites", ignoreCase = true) -> "MLA,MLB,MLC,MLM,MLU"
            key.contains(".users", ignoreCase = true) -> "0,1,2,3,4,5,6,7,8,9"
            key.contains(".list.modes", ignoreCase = true) -> "LIST,LIST_WEBVIEW"
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

