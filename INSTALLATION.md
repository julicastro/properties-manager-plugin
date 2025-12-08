# Instalación del Plugin - Properties Manager

## 📦 Para Miembros del Equipo

### Opción 1: Instalación con Auto-Actualización (Recomendado)

1. **Agregar el repositorio personalizado en IntelliJ:**
   - Abrir IntelliJ IDEA
   - Ir a `File → Settings → Plugins`
   - Hacer clic en el ícono **⚙️ (engranaje)**
   - Seleccionar **`Manage Plugin Repositories...`**
   - Hacer clic en **`+`**
   - Agregar esta URL:
     ```
     https://raw.githubusercontent.com/julicastro/properties-manager-plugin/master/updatePlugins.xml
     ```
   - Hacer clic en **OK**

2. **Instalar el plugin:**
   - En la pestaña `Marketplace` buscar: **"Properties Manager"**
   - Hacer clic en **Install**
   - Reiniciar IntelliJ

3. **Actualizaciones automáticas:**
   - IntelliJ detectará automáticamente nuevas versiones
   - Aparecerá una notificación cuando haya updates disponibles

---

### Opción 2: Instalación Manual (Sin Auto-Update)

1. **Descargar el plugin:**
   - Ir a [Releases](https://github.com/julicastro/properties-manager-plugin/releases)
   - Descargar el archivo `.zip` de la última versión

2. **Instalar en IntelliJ:**
   - `File → Settings → Plugins`
   - Hacer clic en **⚙️ → Install Plugin from Disk...**
   - Seleccionar el archivo `.zip` descargado
   - Reiniciar IntelliJ

---

## 🔧 Uso del Plugin

1. **Abrir un proyecto** que contenga:
   ```
   src/main/resources/properties/app.properties
   ```

2. **Abrir el panel lateral:**
   - Buscar el tab **"Properties Manager"** en el lateral derecho
   - Si no aparece, ir a `View → Tool Windows → Properties Manager`

3. **Gestionar properties:**
   - ✅ Marcar checkbox del **comentario** → Activa todo el grupo
   - ✅ Marcar checkbox de **property individual** → Completa valor automático
   - ❌ Desmarcar → Borra el valor
   - 🔄 Botón **"Refrescar"** → Recarga el archivo

---

## 🦆 Reglas de Auto-Completado

| Property contiene | Valor automático |
|-------------------|------------------|
| `.site` | `MLA` |
| `.user` | `1,2,3,4,5,6,7,8,9,0` |
| `.app` | `LIST,LIST_WEBVIEW` |

---

## 🐛 Soporte

Si encuentras problemas, reporta un issue en:
https://github.com/julicastro/properties-manager-plugin/issues

