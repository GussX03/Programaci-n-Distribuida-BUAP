const emailService = require("../services/emailService");

// Enviar una notificación por email
const sendNotification = async (req, res) => {
  try {
    const { to, subject, text, html } = req.body;
    
    // Validar campos requeridos
    if (!to || !subject || (!text && !html)) {
      return res.status(400).json({ 
        error: "Faltan campos requeridos: to, subject, y text o html son obligatorios" 
      });
    }
    
    // Enviar email simulado
    const result = await emailService.sendEmail({ to, subject, text, html });
    
    res.json({ 
      message: "Notificación enviada exitosamente",
      messageId: result.messageId,
      to: to,
      subject: subject
    });
    
  } catch (error) {
    console.error("Error al enviar notificación:", error);
    res.status(500).json({ error: "Error al enviar la notificación" });
  }
};

// Enviar notificación de bienvenida (ejemplo específico)
const sendWelcomeEmail = async (req, res) => {
  try {
    const { to, username } = req.body;
    
    if (!to || !username) {
      return res.status(400).json({ error: "Faltan campos: to y username" });
    }
    
    const subject = "¡Bienvenido a nuestra plataforma!";
    const html = `
      <div style="font-family: Arial, sans-serif; padding: 20px;">
        <h1>Hola ${username}!</h1>
        <p>Gracias por registrarte en nuestra plataforma.</p>
        <p>Estamos muy contentos de tenerte con nosotros.</p>
        <br>
        <p>Saludos,</p>
        <p>El equipo de soporte</p>
      </div>
    `;
    
    const result = await emailService.sendEmail({ to, subject, html });
    
    res.json({ 
      message: "Email de bienvenida enviado",
      messageId: result.messageId
    });
    
  } catch (error) {
    res.status(500).json({ error: "Error al enviar email de bienvenida" });
  }
};

// Enviar notificación de pedido
const sendOrderNotification = async (req, res) => {
  try {
    const { to, orderId, status, products } = req.body;
    
    if (!to || !orderId || !status) {
      return res.status(400).json({ error: "Faltan campos requeridos" });
    }
    
    const subject = `Actualización de tu pedido #${orderId}`;
    const html = `
      <div style="font-family: Arial, sans-serif;">
        <h2>Tu pedido #${orderId} ha sido ${status}</h2>
        <p>Productos:</p>
        <ul>
          ${products ? products.map(p => `<li>${p.name} x ${p.quantity}</li>`).join('') : '<li>No especificado</li>'}
        </ul>
        <p>Estado actual: <strong>${status}</strong></p>
      </div>
    `;
    
    const result = await emailService.sendEmail({ to, subject, html });
    
    res.json({ 
      message: "Notificación de pedido enviada",
      orderId: orderId,
      status: status
    });
    
  } catch (error) {
    res.status(500).json({ error: "Error al enviar notificación de pedido" });
  }
};

// Simular envío masivo
const sendBulkNotification = async (req, res) => {
  try {
    const { recipients, subject, text } = req.body;
    
    if (!recipients || !recipients.length || !subject) {
      return res.status(400).json({ error: "Faltan campos: recipients (array) y subject" });
    }
    
    const result = await emailService.sendBulkEmail({ recipients, subject, text });
    
    res.json({ 
      message: "Notificaciones masivas enviadas",
      sentCount: result.sentCount,
      recipients: recipients
    });
    
  } catch (error) {
    res.status(500).json({ error: "Error al enviar notificaciones masivas" });
  }
};

module.exports = {
  sendNotification,
  sendWelcomeEmail,
  sendOrderNotification,
  sendBulkNotification
};