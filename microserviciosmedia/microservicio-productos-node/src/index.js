const express = require("express");
const mongoose = require("mongoose");
const productRoutes = require("./routes/productRoutes");

const app = express();

// Middleware
app.use(express.json());

// Conexión a MongoDB
mongoose.connect("mongodb://localhost:27017/products");

mongoose.connection.on("connected", () => {
  console.log("✅ Conectado a MongoDB (products)");
});

mongoose.connection.on("error", (err) => {
  console.log("❌ Error conectando a MongoDB:", err);
});

// Rutas
app.use("/api", productRoutes);

// Ruta de prueba
app.get("/", (req, res) => {
  res.json({ message: "Product Service funcionando!" });
});

// Iniciar servidor
const PORT = 3002;
app.listen(PORT, () => {
  console.log(`🚀 Product service corriendo en http://localhost:${PORT}`);
});