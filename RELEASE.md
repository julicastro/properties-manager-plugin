# 📦 Guía para Crear un Release

Esta guía explica paso a paso cómo publicar una nueva versión del plugin **Properties Manager - Goose Edition** 🦆.

---

## 🎯 Checklist Rápido

- [ ] Incrementar versión en archivos
- [ ] Compilar el plugin (.zip)
- [ ] Commit y push del código
- [ ] Crear tag Git
- [ ] Crear release en GitHub
- [ ] Subir archivo .zip al release
- [ ] Actualizar updatePlugins.xml
- [ ] Commit y push del updatePlugins.xml
- [ ] Verificar actualización en IntelliJ

---

## 📋 Pasos Detallados

### 1️⃣ Incrementar Número de Versión

Cambiar la versión en **2 archivos**:

#### A. `gradle.properties`

```properties
pluginVersion = 1.0.X  # ← Cambiar aquí (ej: 1.0.2 → 1.0.3)
```

#### B. `src/main/resources/META-INF/plugin.xml`

Actualizar **2 lugares**:

**a) Tag `<version>`:**
```xml
<version>1.0.X</version>  <!-- ← Cambiar versión -->
```

**b) Tag `<change-notes>`:**
```xml
<change-notes><![CDATA[
    <h4>Versión 1.0.X</h4>  <!-- ← Nueva versión -->
    <ul>
        <li>🎯 Descripción de cambios principales</li>
        <li>✨ Nuevas funcionalidades</li>
        <li>🐛 Bugs corregidos</li>
    </ul>
    <h4>Versión 1.0.Y</h4>  <!-- ← Versiones anteriores -->
    <ul>
        <li>...</li>
    </ul>
]]></change-notes>
```

---

### 2️⃣ Compilar el Plugin

**Comando:**
```bash
.\gradlew.bat clean buildPlugin
```

**Resultado:**
```
build\distributions\properties-manager-plugin-1.0.X.zip
```

✅ Verificar que el archivo `.zip` se creó correctamente.

---

### 3️⃣ Commit y Push del Código

**Comandos:**
```bash
# Agregar archivos modificados
git add gradle.properties src/main/resources/META-INF/plugin.xml

# Commit con mensaje descriptivo
git commit -m "Bump version to 1.0.X"

# Push al repositorio
git push
```

---

### 4️⃣ Crear Tag Git

**Comandos:**
```bash
# Crear tag anotado (recomendado)
git tag -a v1.0.X -m "Release version 1.0.X"

# Push del tag
git push origin v1.0.X
```

**Verificar:**
```bash
git tag  # Debe mostrar v1.0.X
```

---

### 5️⃣ Crear Release en GitHub

#### Opción A: Interfaz Web (Recomendado)

1. **Ir a Releases:**
   ```
   https://github.com/julicastro/properties-manager-plugin/releases
   ```

2. **Click en "Create a new release"**

3. **Configurar release:**
   - **Tag:** Seleccionar `v1.0.X` (ya creado)
   - **Title:** `v1.0.X - Descripción breve`
   - **Description:**
     ```markdown
     ## 🦆 Properties Manager - Goose Edition v1.0.X

     ### ✨ Novedades
     - Nueva funcionalidad X
     - Mejora en Y

     ### 🐛 Correcciones
     - Corregido bug Z

     ### 📦 Instalación
     Ver [INSTALLATION.md](https://github.com/julicastro/properties-manager-plugin/blob/master/INSTALLATION.md)
     ```

4. **Subir archivo .zip:**
   - Arrastrar o seleccionar: `build\distributions\properties-manager-plugin-1.0.X.zip`

5. **Click en "Publish release"**

#### Opción B: Desde Terminal (Avanzado)

Requiere GitHub CLI (`gh`):
```bash
gh release create v1.0.X build/distributions/properties-manager-plugin-1.0.X.zip \
  --title "v1.0.X - Descripción" \
  --notes "Release notes aquí"
```

---

### 6️⃣ Actualizar updatePlugins.xml

Este paso es **CRÍTICO** para que IntelliJ detecte la actualización.

Editar `updatePlugins.xml`:

**a) Cambiar URL del .zip:**
```xml
<plugin id="com.properties.manager" 
        url="https://github.com/julicastro/properties-manager-plugin/releases/download/v1.0.X/properties-manager-plugin-1.0.X.zip" 
        version="1.0.X">
```

**b) Cambiar versión:**
```xml
version="1.0.X"
```

**c) Actualizar change-notes:**
```xml
<change-notes><![CDATA[
    <h4>Versión 1.0.X</h4>  <!-- ← Nueva versión -->
    <ul>
        <li>✨ Cambios principales</li>
    </ul>
    <h4>Versión 1.0.Y</h4>  <!-- ← Versiones anteriores -->
    <ul>
        <li>...</li>
    </ul>
]]></change-notes>
```

---

### 7️⃣ Commit y Push del updatePlugins.xml

**Comandos:**
```bash
git add updatePlugins.xml
git commit -m "Update plugin repository to v1.0.X"
git push
```

⚠️ **IMPORTANTE:** Espera 1-2 minutos después del push para que GitHub procese el cambio.

---

### 8️⃣ Verificar Actualización en IntelliJ

En IntelliJ donde tengas instalado el plugin:

1. **Ir a Settings:**
   ```
   File → Settings → Plugins
   ```

2. **Forzar revisión:**
   ```
   Click en ⚙️ → Check for Plugin Updates
   ```

3. **Verificar actualización:**
   ```
   🔔 "Properties Manager - Goose Edition tiene una actualización disponible"
      Versión actual: 1.0.Y
      Nueva versión: 1.0.X
   ```

4. **Actualizar y reiniciar**

---

## 🔍 Verificación Final

### A. Verificar updatePlugins.xml en GitHub

Abrir en navegador:
```
https://raw.githubusercontent.com/julicastro/properties-manager-plugin/master/updatePlugins.xml
```

Debe mostrar `version="1.0.X"`

### B. Verificar Release

Abrir en navegador:
```
https://github.com/julicastro/properties-manager-plugin/releases/tag/v1.0.X
```

Verificar:
- ✅ Tag correcto
- ✅ Archivo `.zip` adjunto
- ✅ Descripción correcta

---

## 🚨 Troubleshooting

### IntelliJ no detecta la actualización

**Problema:** Después de todo, IntelliJ no muestra actualización disponible.

**Soluciones:**

1. **Esperar 2-3 minutos** después de push del `updatePlugins.xml`

2. **Verificar updatePlugins.xml en GitHub:**
   - URL: `https://raw.githubusercontent.com/.../updatePlugins.xml`
   - Debe mostrar la nueva versión

3. **Forzar revisión manual:**
   ```
   Settings → Plugins → ⚙️ → Check for Plugin Updates
   ```

4. **Limpiar caché de IntelliJ:**
   ```
   File → Invalidate Caches / Restart
   ```

### Build falla

**Problema:** `.\gradlew.bat buildPlugin` falla.

**Soluciones:**

1. **Limpiar build anterior:**
   ```bash
   .\gradlew.bat clean
   ```

2. **Verificar Java 17:**
   ```bash
   java -version  # Debe ser 17 o superior
   ```

3. **Ver errores completos:**
   ```bash
   .\gradlew.bat buildPlugin --stacktrace
   ```

### Tag ya existe

**Problema:** `git tag v1.0.X` dice que el tag ya existe.

**Soluciones:**

1. **Eliminar tag local:**
   ```bash
   git tag -d v1.0.X
   ```

2. **Eliminar tag remoto:**
   ```bash
   git push --delete origin v1.0.X
   ```

3. **Crear tag de nuevo**

---

## 📝 Template de Commit Messages

```bash
# Bump de versión
git commit -m "Bump version to 1.0.X"

# Nueva funcionalidad
git commit -m "feat: Agregar [funcionalidad]"

# Corrección de bug
git commit -m "fix: Corregir [problema]"

# Mejora
git commit -m "improve: Mejorar [aspecto]"

# Actualizar repositorio de plugins
git commit -m "Update plugin repository to v1.0.X"
```

---

## 🎯 Resumen de Archivos a Modificar

| Archivo | Qué Cambiar | Cuándo |
|---------|-------------|--------|
| `gradle.properties` | `pluginVersion` | Cada release |
| `plugin.xml` | `<version>` y `<change-notes>` | Cada release |
| `updatePlugins.xml` | `url`, `version`, `<change-notes>` | Después del release |

---

## ⏱️ Tiempo Estimado por Release

- 📝 Cambiar versiones: **2 minutos**
- 🔨 Compilar plugin: **1 minuto**
- 💾 Commit y push: **1 minuto**
- 🏷️ Crear tag: **30 segundos**
- 🌐 Crear release en GitHub: **3 minutos**
- 📤 Actualizar updatePlugins.xml: **2 minutos**
- ✅ Verificar: **1 minuto**

**Total: ~10 minutos por release**

---

## 🎓 Buenas Prácticas

### Semantic Versioning

Usa versionado semántico: `MAJOR.MINOR.PATCH`

- **MAJOR** (1.x.x): Cambios incompatibles
- **MINOR** (x.1.x): Nuevas funcionalidades compatibles
- **PATCH** (x.x.1): Correcciones de bugs

**Ejemplos:**
- `1.0.0` → `1.0.1`: Bug fix
- `1.0.1` → `1.1.0`: Nueva funcionalidad
- `1.1.0` → `2.0.0`: Cambio incompatible

### Frecuencia de Releases

- 🐛 **Hotfix**: Inmediato (bugs críticos)
- 🔄 **Minor**: Semanal/Mensual (nuevas features)
- 🚀 **Major**: Cuando sea necesario (cambios grandes)

### Testing Antes de Release

1. Ejecutar `.\gradlew.bat runIde`
2. Probar todas las funcionalidades
3. Verificar que no hay errores
4. Probar en proyecto real (`test-project`)

---

## 📚 Referencias

- [Semantic Versioning](https://semver.org/)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github)
- [IntelliJ Plugin Development](https://plugins.jetbrains.com/docs/intellij/)

