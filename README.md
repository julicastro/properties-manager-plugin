# Properties Manager Plugin 🦆

Plugin para IntelliJ IDEA que permite gestionar archivos `.properties` de forma interactiva mediante checkboxes en un panel lateral.

> **📦 Para instalación rápida del equipo**: Ver [INSTALLATION.md](INSTALLATION.md)

## 📋 Características

- ✅ Lectura dinámica de archivos `.properties`
- ✅ Agrupación automática por prefijo numérico (sin prefijo, 1., 2., etc.)
- ✅ Preserva comentarios y estructura del archivo
- ✅ UI declarativa con Kotlin UI DSL
- ✅ Panel lateral tipo Tool Window (como Maven/Gradle)

## 🎯 Objetivo

Facilitar la edición de archivos `.properties` de cualquier proyecto Java. El plugin lee el archivo dinámicamente y muestra checkboxes que permiten activar/desactivar valores predefinidos según el nombre de cada property.

## 📝 Reglas de Valores Automáticos

| Tipo de Property | Valor al Checkear | Ejemplo |
|------------------|-------------------|---------|
| Contiene `.sites` | `MLA,MLB,MLC,MLM,MLU` | `1.this.is.a.sites=MLA,MLB,MLC,MLM,MLU` |
| Contiene `.users` | `0,1,2,3,4,5,6,7,8,9` | `1.this.is.a.users=0,1,2,3,4,5,6,7,8,9` |
| Contiene `.list.modes` | `LIST,LIST_WEBVIEW` | `1.this.is.a.list.modes=LIST,LIST_WEBVIEW` |
| Al descheckear | Se borra el valor (queda vacío) | `1.this.is.a.sites=` |

## 🏗️ Estructura del Código

```
src/main/kotlin/com/properties/manager/
├── PropertiesToolWindowFactory.kt    # UI del panel lateral con checkboxes dinámicos
├── PropertiesFileReader.kt           # Lee y parsea archivos .properties
├── PropertiesFileUpdater.kt          # Actualiza el archivo preservando comentarios
└── PropertyValueResolver.kt          # Resuelve valores según el nombre de la key
                                      # (.sites, .users, .list.modes)

src/main/resources/META-INF/
└── plugin.xml                        # Configuración del plugin (Tool Window)
```

## 🔧 Instalación

### Para Usuarios Finales

Ver instrucciones completas en [INSTALLATION.md](INSTALLATION.md)

### Para Desarrolladores - Crear Releases

Ver guía completa en [RELEASE.md](RELEASE.md)

**Instalación rápida con auto-update:**

1. En IntelliJ: `Settings → Plugins → ⚙️ → Manage Plugin Repositories`
2. Agregar: `https://raw.githubusercontent.com/julianemanue_meli/properties-manager-plugin/master/updatePlugins.xml`
3. Buscar "Properties Manager" en Marketplace y instalar

### Para Desarrolladores

#### 🚀 Probar el Plugin (Modo Desarrollo)

```bash
# Abre una nueva ventana de IntelliJ con el plugin ya instalado
./gradlew runIde
```

**Pasos:**
1. Ejecutar el comando arriba
2. Esperar que se abra nueva ventana de IntelliJ
3. En esa ventana: `File → Open` → Abrir proyecto con archivos `.properties`
4. Probar el plugin en el panel lateral "Properties Manager"

#### 📦 Compilar el Plugin (Para Release)

```bash
# Compila y genera el .zip para distribución
./gradlew clean buildPlugin
```

El archivo `.zip` se genera en: `build/distributions/properties-manager-plugin-1.0.0.zip`

#### 🔄 Workflow de Desarrollo

```bash
# 1. Hacer cambios en el código
# 2. Probar cambios:
./gradlew runIde

# 3. Cerrar ventana de prueba
# 4. Hacer más cambios
# 5. Repetir paso 2
```

## 🚀 Uso

1. Instalar el plugin en IntelliJ IDEA
2. Abrir un proyecto que tenga archivos `.properties` en alguna de estas ubicaciones:
   ```
   core/app-shared/shared/config/src/main/resources/category.properties
   test-project/src/main/resources/properties/app.properties
   ```
3. En el panel lateral derecho aparece **"Properties Manager"**
4. Se muestran checkboxes agrupados por prefijo
5. **Checkear** → completa valor automático
6. **Descheckear** → borra el valor

## 📂 Proyecto de Prueba

El plugin incluye un proyecto de prueba en `test-project/` con un archivo de ejemplo:

```
test-project/src/main/resources/properties/app.properties
```

## 🛠️ Tecnologías

- **Kotlin** 1.9.22
- **IntelliJ Platform SDK** (versión 2023.2.5)
- **Kotlin UI DSL** para la interfaz
- **Gradle** como build system

## 📖 Ejemplo de `app.properties`

```properties
#comment 1
this.is.a.sites=
this.is.a.users=
this.is.a.list.modes=

#comment 2
1.this.is.a.sites=MLA,MLB,MLC,MLM,MLU
1.this.is.a.users=0,1,2,3,4,5,6,7,8,9
1.this.is.a.list.modes=LIST,LIST_WEBVIEW

#comment 3
2.this.is.a.sites=MLA,MLB,MLC,MLM,MLU
2.this.is.a.users=0,1,2,3,4,5,6,7,8,9
2.this.is.a.list.modes=LIST,LIST_WEBVIEW

# comment 4
example.sites=
example.users=
example.list.modes=

# comment 5
3.test.sites=MLA,MLB,MLC,MLM,MLU
3.test.users=0,1,2,3,4,5,6,7,8,9
3.test.list.modes=LIST,LIST_WEBVIEW
```

## 🔄 Flujo de Trabajo

1. El plugin detecta archivos `.properties` en las rutas configuradas
2. Agrupa las properties por comentarios (cada comentario inicia un grupo)
3. Muestra checkboxes para cada property y checkbox maestro por grupo
4. Al marcar un checkbox:
   - Detecta el tipo de property (`.sites`, `.users`, `.list.modes`)
   - Completa automáticamente con el valor correspondiente
5. Al marcar el checkbox maestro de un comentario:
   - Activa todas las properties del grupo simultáneamente
6. Al desmarcar un checkbox:
   - Elimina el valor dejando solo la key vacía

## 📝 Notas

- El plugin preserva todos los comentarios del archivo
- Mantiene la estructura y formato original
- Solo modifica los valores de las properties
- Agrupa properties por comentarios (cada comentario con sus properties siguientes forma un grupo)
- Checkbox maestro en cada comentario permite activar/desactivar todo el grupo
- Detecta automáticamente si una property debe estar marcada (si tiene valor no vacío)

