# DataStructureVisualizer

## Descripción
DataStructureVisualizer es una aplicación móvil desarrollada en Android que permite visualizar estructuras de datos y algoritmos de manera interactiva. Diseñada como herramienta educativa, facilita el aprendizaje de conceptos fundamentales de programación a través de representaciones visuales.

## Características principales

- **Visualización de estructuras de datos:**
  - Pilas (Stacks)
  - Colas (Queues)
  - Listas doblemente enlazadas
  - Árboles binarios

- **Sistema de usuarios:**
  - Registro e inicio de sesión
  - Perfil personalizable con foto
  - Actualización de datos personales

- **Gestión de datos:**
  - Ingreso manual de datos
  - Carga de archivos CSV
  - Procesamiento y normalización

- **Interfaz moderna:**
  - Diseño con Jetpack Compose
  - Experiencia fluida y responsiva
  - Navegación intuitiva

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Realtime Database
- ViewModel y StateFlow
- Room Database
- Navigation Component
- Material 3 Design

## Requisitos

- Android Studio Arctic Fox o superior
- Kotlin 1.5.1 o superior
- JDK 8
- Dispositivo o emulador con Android 7.0 (API 24) o superior

## Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/DataStructureVisualizer-.git
```

2. Abre el proyecto en Android Studio

3. Sincroniza el proyecto con los archivos Gradle

4. Ejecuta la aplicación en un emulador o dispositivo físico

## Uso

1. **Registro/Inicio de sesión:**
   - Crea una cuenta o inicia sesión con email y contraseña

2. **Navegación principal:**
   - Accede a las diferentes estructuras de datos desde la pantalla principal

3. **Visualizar estructuras:**
   - Selecciona una estructura para ver su representación visual
   - Utiliza los controles para manipular la estructura (agregar/eliminar elementos)

4. **Entrada de datos:**
   - Ingresa datos manualmente separados por comas
   - O sube un archivo CSV con tus datos

## Estructura del proyecto

La aplicación está organizada en los siguientes paquetes:

- **screens**: Contiene las pantallas principales como login, registro, perfil y home
- **views**: Implementaciones de las diferentes visualizaciones de estructuras de datos
- **viewmodels**: Lógica de negocio para las diferentes funcionalidades
- **data**: Modelos de datos y repositorios
- **domain**: Casos de uso y modelos de dominio
- **components**: Componentes reutilizables de UI
- **algorithmLogic**: Implementaciones de algoritmos

## Contribución

1. Haz fork del proyecto
2. Crea una rama para tu funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Realiza tus cambios y haz commit (`git commit -am 'Añade nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crea un nuevo Pull Request

## Licencia

Este proyecto está bajo la licencia [MIT](https://opensource.org/licenses/MIT).

## Contacto

Proyecto desarrollado para la Universidad del Valle de Guatemala.

## Créditos

Desarrollado por el equipo de DataStructureVisualizer

---

© 2024 DataStructureVisualizer
