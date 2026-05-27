const express = require("express");
const {
  sendNotification,
  sendWelcomeEmail,
  sendOrderNotification,
  sendBulkNotification
} = require("../controllers/notificationController");

const router = express.Router();

// Endpoints de notificaciones
router.post("/notify", sendNotification);
router.post("/notify/welcome", sendWelcomeEmail);
router.post("/notify/order", sendOrderNotification);
router.post("/notify/bulk", sendBulkNotification);

// Ruta de prueba
router.get("/health", (req, res) => {
  res.json({ status: "Notification service OK" });
});

module.exports = router;