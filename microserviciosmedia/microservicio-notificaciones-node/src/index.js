const express = require("express");
const notificationRoutes = require("./routes/notificationRoutes");

const app = express();

// Middleware
app.use(express.json());

// Logging de peticiones (opcional)
app.use((req, res, next) => {
  console.log(`📨 ${req.method} ${req.url}`);
  next();
});

// Rutas
app.use("/api", notificationRoutes);

// Ruta de prueba
app.get("/", (req, res) => {
  res.json({ 
    message: "Notification Service funcionando!",
    endpoints: [
      "POST /api/notify - Enviar email genérico",
      "POST /api/notify/welcome - Email de bienvenida",
      "POST /api/notify/order - Notificación de pedido",
      "POST /api/notify/bulk - Envío masivo"
    ]
  });
});

// Iniciar servidor
const PORT = 3004;
app.listen(PORT, () => {
  console.log(`\n📧 Notification service corriendo en http://localhost:${PORT}`);
  console.log("Los emails se mostrarán en la consola (modo simulado)\n");
});