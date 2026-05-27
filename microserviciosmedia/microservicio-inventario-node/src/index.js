const express = require("express");
const mongoose = require("mongoose");
const inventoryRoutes = require("./routes/inventoryRoutes");

const app = express();

// Middleware
app.use(express.json());

// Logging de peticiones
app.use((req, res, next) => {
  console.log(`📦 ${req.method} ${req.url}`);
  next();
});

// Conexión a MongoDB
mongoose.connect("mongodb://localhost:27017/inventory");

mongoose.connection.on("connected", () => {
  console.log("✅ Conectado a MongoDB (inventory)");
});

mongoose.connection.on("error", (err) => {
  console.log("❌ Error conectando a MongoDB:", err);
});

// Rutas
app.use("/api", inventoryRoutes);

// Ruta de prueba
app.get("/", (req, res) => {
  res.json({ 
    message: "Inventory Service funcionando!",
    endpoints: [
      "GET /api/inventory - Listar todo el inventario",
      "GET /api/inventory/:productId - Ver producto",
      "POST /api/inventory - Crear/actualizar producto",
      "PUT /api/inventory/:productId - Actualizar stock",
      "DELETE /api/inventory/:productId - Eliminar producto",
      "GET /api/inventory/low-stock - Productos con bajo stock"
    ]
  });
});

// Iniciar servidor
const PORT = 3005;
app.listen(PORT, () => {
  console.log(`\n📦 Inventory service corriendo en http://localhost:${PORT}`);
  console.log("Base de datos: MongoDB (inventory)\n");
});