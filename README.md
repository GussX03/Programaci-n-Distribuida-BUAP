# Programación Distribuida - Guía De Ejecución

## Descripción
Este repositorio contiene prácticas y proyectos de programación distribuida con Java RMI, microservicios en Node.js y clientes web en React.

## Requisitos
- Java 17 (`java`, `javac`, `rmiregistry`)
- Node.js + npm
- Docker Desktop (para MongoDB local)
- Visual Studio Code (opcional)

## Orden De Ejecución
Los proyectos se ejecutan en este orden:

1. `Programas`
2. `analytics-service`
3. `logistics-service`
4. `meeting-service`
5. `sentiment-service`
6. `stream-service`
7. `task-service`
8. `MicroserviciosSimples`
9. `microserviciosmedia`
10. `corporate-service`
11. `recommendation-service`
12. `MComplejidadAlta`

## Estructura General De Arranque

### Proyectos Java (`Programas`)
- Entrar a cada proyecto.
- Compilar:
```bash
javac ejemplo/*.java
```
- Ejecutar servidor y cliente en terminales separadas.

### Proyectos Node + React
- En carpeta backend:
```bash
npm install
node src/server.js
```
- En carpeta frontend (`*-client`):
```bash
npm install
npm start
```

### MongoDB Local (si el backend lo requiere)
```bash
open -a Docker
docker start analytics-mongo || docker run -d --name analytics-mongo -p 27017:27017 mongo:7
```

## Ejecución De `Programas/PracticaSOA_RMI`
Abrir 5 terminales en `Programas/PracticaSOA_RMI/src`.

### Terminal 1
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
cd "Programas/PracticaSOA_RMI/src"
javac ejemplo/*.java
rmiregistry 1099
```

### Terminal 2
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
cd "Programas/PracticaSOA_RMI/src"
java ejemplo.AppServicioNotificaciones
```

### Terminal 3
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
cd "Programas/PracticaSOA_RMI/src"
java ejemplo.AppServicioInventario
```

### Terminal 4
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
cd "Programas/PracticaSOA_RMI/src"
java ejemplo.AppOrquestadorPedidos
```

### Terminal 5
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
cd "Programas/PracticaSOA_RMI/src"
java ejemplo.AppClienteIndustrial
```

## Notas
- Si `rmiregistry` muestra advertencias de `SecurityManager`, son advertencias esperadas en versiones modernas de Java.
- Si `react-scripts` falla por permisos:
```bash
xattr -dr com.apple.quarantine node_modules
chmod -R u+rx node_modules/.bin
```
