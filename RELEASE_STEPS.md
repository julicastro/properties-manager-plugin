# 📦 Pasos para Crear un Release

## Configuración Inicial (Solo una vez)

### 1. Subir el código a GitHub

```bash
git init
git add .
git commit -m "Initial commit - Properties Manager Plugin v1.0.0"
git branch -M master
git remote add origin https://github.com/julicastro/properties-manager-plugin.git
git push -u origin master
```

### 2. ~~Reemplazar TU-USUARIO en los archivos~~ ✅ Ya hecho

Los archivos ya tienen configurado el usuario: **julicastro**

---

## Para Cada Nueva Versión

### 1. Actualizar el número de versión

Editar `gradle.properties`:

```properties
pluginVersion = 1.0.1  # ← Incrementar versión
```

### 2. Compilar el plugin

```bash
.\gradlew.bat clean buildPlugin
```

### 3. Crear un tag Git

```bash
git tag -a v1.0.1 -m "Release version 1.0.1"
git push origin v1.0.1
```

### 4. Crear el Release en GitHub

#### Opción A: Automático (usando GitHub Actions)

Al hacer `git push origin v1.0.1`, GitHub Actions automáticamente:
- Compila el plugin
- Crea el release
- Sube el archivo .zip

#### Opción B: Manual

1. Ir a: `https://github.com/julicastro/properties-manager-plugin/releases`
2. Click en **"Create a new release"**
3. En "Tag version": Seleccionar `v1.0.1`
4. En "Release title": `Release 1.0.1`
5. En "Describe this release": Agregar notas de cambios
6. **Arrastrar** el archivo `build/distributions/properties-manager-plugin-1.0.1.zip`
7. Click en **"Publish release"**

### 5. Actualizar updatePlugins.xml

Editar `updatePlugins.xml` y agregar la nueva versión:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<plugins>
    <!-- Nueva versión -->
    <plugin id="com.properties.manager" 
            url="https://github.com/julicastro/properties-manager-plugin/releases/download/v1.0.1/properties-manager-plugin-1.0.1.zip" 
            version="1.0.1">
        <name>Properties Manager - Goose Edition</name>
        <description><![CDATA[...]]></description>
        <vendor>...</vendor>
        <idea-version since-build="232" until-build=""/>
        <change-notes><![CDATA[
            <h4>Versión 1.0.1</h4>
            <ul>
                <li>🐛 Corrección de bugs</li>
                <li>✨ Nueva funcionalidad X</li>
            </ul>
        ]]></change-notes>
    </plugin>
</plugins>
```

Commitear y pushear:

```bash
git add updatePlugins.xml
git commit -m "Update plugin repository to v1.0.1"
git push
```

### 6. Notificar al equipo

Los usuarios que tengan configurado el repositorio personalizado recibirán una notificación automática en IntelliJ sobre la nueva versión disponible.

---

## 🔍 Verificar que funciona

1. En IntelliJ de otro usuario:
   - `Settings → Plugins → Installed`
   - Debería aparecer una notificación de update disponible

2. O manualmente:
   - `Settings → Plugins → ⚙️ → Check for Plugin Updates`

---

## 🚨 Troubleshooting

### El release automático no se crea

Verifica que:
- El workflow esté en `.github/workflows/release.yml`
- GitHub Actions esté habilitado en el repo
- El tag comience con `v` (ej: `v1.0.1`, no `1.0.1`)

### IntelliJ no detecta actualizaciones

Verifica que:
- El `updatePlugins.xml` esté en la rama `master`
- La URL sea accesible públicamente
- El número de versión sea mayor que la instalada
- El campo `version` en `updatePlugins.xml` coincida con el del release

### El plugin no se instala

Verifica que:
- El archivo `.zip` se haya subido correctamente al release
- El nombre del archivo coincida con el de `updatePlugins.xml`
- El plugin sea compatible con la versión de IntelliJ del usuario

