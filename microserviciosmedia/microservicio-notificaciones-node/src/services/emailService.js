// Servicio de email SIMULADO (no envía emails reales, solo los muestra en consola)
class EmailService {
  
  async sendEmail({ to, subject, text, html }) {
    // Simular envío de email
    console.log("\n📧 ===== EMAIL ENVIADO (SIMULADO) =====");
    console.log(`📨 Para: ${to}`);
    console.log(`📌 Asunto: ${subject}`);
    console.log(`📝 Contenido: ${text || html || "Sin contenido"}`);
    console.log("=====================================\n");
    
    // Simular delay de red
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    return { success: true, messageId: `simulated-${Date.now()}` };
  }
  
  // Versión para múltiples destinatarios
  async sendBulkEmail({ recipients, subject, text, html }) {
    console.log("\n📧 ===== EMAILS MASIVOS (SIMULADOS) =====");
    console.log(`👥 Destinatarios: ${recipients.join(", ")}`);
    console.log(`📌 Asunto: ${subject}`);
    console.log(`📝 Contenido: ${text || html || "Sin contenido"}`);
    console.log(`📊 Total: ${recipients.length} emails`);
    console.log("========================================\n");
    
    await new Promise(resolve => setTimeout(resolve, 1500));
    
    return { 
      success: true, 
      sentCount: recipients.length,
      messageIds: recipients.map(() => `simulated-${Date.now()}`)
    };
  }
}

module.exports = new EmailService();