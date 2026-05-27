const express = require("express");
const mongoose = require("mongoose");
const orderRoutes = require("./routes/orderRoutes");

const app = express();

// Middleware
app.use(express.json());

// Conexión a MongoDB
mongoose.connect("mongodb://localhost:27017/orders");

mongoose.connection.on("connected", () => {
  console.log("✅ Conectado a MongoDB (orders)");
});

mongoose.connection.on("error", (err) => {
  console.log("❌ Error conectando a MongoDB:", err);
});

// Rutas
app.use("/api", orderRoutes);

// Ruta de prueba
app.get("/", (req, res) => {
  res.json({ message: "Order Service funcionando!" });
});

// Iniciar servidor
const PORT = 3003;
app.listen(PORT, () => {
  console.log(`🚀 Order service corriendo en http://localhost:${PORT}`);
});