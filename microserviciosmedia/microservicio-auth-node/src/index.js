const express = require("express");
const mongoose = require("mongoose");
const authRoutes = require("./routes/authRoutes");

const app = express();

// Middleware
app.use(express.json());

// Conexión a MongoDB (sin opciones obsoletas)
mongoose.connect("mongodb://localhost:27017/auth");

mongoose.connection.on("connected", () => {
  console.log("✅ Conectado a MongoDB");
});

mongoose.connection.on("error", (err) => {
  console.log("❌ Error conectando a MongoDB:", err);
});

// Rutas
app.use("/auth", authRoutes);

// Ruta de prueba
app.get("/", (req, res) => {
  res.json({ message: "Auth Service funcionando!" });
});

// Iniciar servidor
const PORT = 3001;
app.listen(PORT, () => {
  console.log(`🚀 Auth service corriendo en http://localhost:${PORT}`);
});