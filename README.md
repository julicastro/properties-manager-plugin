# Properties Manager Plugin 🦆

Plugin para IntelliJ IDEA que permite gestionar archivos `app.properties` de forma interactiva mediante checkboxes en un panel lateral.

> **📦 Para instalación rápida del equipo**: Ver [INSTALLATION.md](INSTALLATION.md)

## 📋 Características

- ✅ Lectura dinámica del archivo `app.properties`
- ✅ Agrupación automática por prefijo numérico (sin prefijo, 1., 2., etc.)
- ✅ Preserva comentarios y estructura del archivo
- ✅ UI declarativa con Kotlin UI DSL
- ✅ Panel lateral tipo Tool Window (como Maven/Gradle)

## 🎯 Objetivo

Facilitar la edición de archivos `app.properties` ubicados en `src/main/resources/properties/app.properties` de cualquier proyecto Java. El plugin lee el archivo dinámicamente y muestra checkboxes que permiten activar/desactivar valores predefinidos según el nombre de cada property.

## 📝 Reglas de Valores Automáticos

| Tipo de Property | Valor al Checkear | Ejemplo |
|------------------|-------------------|---------|
| Contiene `.site` | `MLA` | `1.category.site=MLA` |
| Contiene `.user` | `1,2,3,4,5,6,7,8,9,0` | `1.category.user=1,2,3,4,5,6,7,8,9,0` |
| Contiene `.app` | `LIST,LIST_WEBVIEW` | `1.category.app=LIST,LIST_WEBVIEW` |
| Al descheckear | Se borra el valor (queda vacío) | `1.category.site=` |

## 🏗️ Estructura del Código

```
src/main/kotlin/com/properties/manager/
├── PropertiesToolWindowFactory.kt    # UI del panel lateral con checkboxes dinámicos
├── PropertiesFileReader.kt           # Lee y parsea el app.properties
├── PropertiesFileUpdater.kt          # Actualiza el archivo preservando comentarios
└── PropertyValueResolver.kt          # Detecta qué valor poner según el nombre de la key

src/main/resources/META-INF/
└── plugin.xml                        # Configuración del plugin (Tool Window)
```

## 🔧 Instalación

### Para Usuarios Finales

Ver instrucciones completas en [INSTALLATION.md](INSTALLATION.md)

**Instalación rápida con auto-update:**

1. En IntelliJ: `Settings → Plugins → ⚙️ → Manage Plugin Repositories`
2. Agregar: `https://raw.githubusercontent.com/julicastro/properties-manager-plugin/master/updatePlugins.xml`
3. Buscar "Properties Manager" en Marketplace y instalar

### Para Desarrolladores

#### 🚀 Probar el Plugin (Modo Desarrollo)

```bash
# Abre una nueva ventana de IntelliJ con el plugin ya instalado
.\gradlew.bat runIde
```

**Pasos:**
1. Ejecutar el comando arriba
2. Esperar que se abra nueva ventana de IntelliJ
3. En esa ventana: `File → Open` → Abrir proyecto con `app.properties`
4. Probar el plugin en el panel lateral "Properties Manager"

#### 📦 Compilar el Plugin (Para Release)

```bash
# Compila y genera el .zip para distribución
.\gradlew.bat buildPlugin
```

El archivo `.zip` se genera en: `build\distributions\properties-manager-plugin-1.0.0.zip`

#### 🔄 Workflow de Desarrollo

```bash
# 1. Hacer cambios en el código
# 2. Probar cambios:
.\gradlew.bat runIde

# 3. Cerrar ventana de prueba
# 4. Hacer más cambios
# 5. Repetir paso 2
```

## 🚀 Uso

1. Instalar el plugin en IntelliJ IDEA
2. Abrir un proyecto que tenga `app.properties` en la ubicación esperada:
   ```
   src/main/resources/properties/app.properties
   ```
3. En el panel lateral derecho aparece **"Properties Manager"**
4. Se muestran checkboxes agrupados por prefijo
5. **Checkear** → completa valor automático
6. **Descheckear** → borra el valor

## 📂 Proyecto de Prueba

El plugin está diseñado para trabajar con el proyecto ubicado en:

```
C:\Workspace\random-apps\intellij-properties-plugin\
```

Ese proyecto tiene un archivo de ejemplo en:

```
src\main\resources\properties\app.properties
```

## 🛠️ Tecnologías

- **Kotlin** 1.9.22
- **IntelliJ Platform SDK** (versión 2023.2.5)
- **Kotlin UI DSL** para la interfaz
- **Gradle** como build system

## 📖 Ejemplo de `app.properties`

```properties
# Category properties
1.category.site=MLA
1.category.user=1,2,3,4,5,6,7,8,9,0
1.category.app=LIST,LIST_WEBVIEW

# Price properties
2.price.site=
2.price.user=
2.price.app=

# General properties
enabled=true
```

## 🔄 Flujo de Trabajo

1. El plugin detecta el archivo `app.properties`
2. Agrupa las properties por prefijo numérico
3. Muestra checkboxes para cada property
4. Al marcar un checkbox:
   - Detecta el tipo de property (`.site`, `.user`, `.app`)
   - Completa automáticamente con el valor correspondiente
5. Al desmarcar un checkbox:
   - Elimina el valor dejando solo la key

## 📝 Notas

- El plugin preserva todos los comentarios del archivo
- Mantiene la estructura y formato original
- Solo modifica los valores de las properties
- Soporta agrupación por prefijos numéricos (1., 2., 3., etc.)

