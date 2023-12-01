package com.example.ram.service;

import com.example.ram.model.Elementos;
import com.example.ram.model.Reserva;
import com.example.ram.model.Salon;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class PdfGeneratorService {

    public byte[] generatePdf(List<Salon> salones, Authentication authentication) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        String username = "N/A";
        if (authentication != null) {
            username = authentication.getName();
        }

     // Crear un estilo personalizado para el título
        Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLUE);
        Font titulodFont = new Font(Font.HELVETICA, 14, Font.NORMAL, Color.BLUE);
        Font titulotFont = new Font(Font.HELVETICA, 14, Font.NORMAL, Color.GREEN);
        Font tituloaFont = new Font(Font.HELVETICA, 15, Font.BOLD, Color.black);
        document.open();
        Paragraph titulo = new Paragraph("Lista de Salones:", tituloFont);
        Paragraph tituloa = new Paragraph("Administrador:  ",tituloaFont);
        document.add(new Paragraph(titulo));
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(tituloa);
    	document.add(new Paragraph(username));
    	document.add(Chunk.NEWLINE);
    	document.add(Chunk.NEWLINE);
        
        
        // Agregar el nombre del usuario al PDF
   
        for (Salon salon : salones) {
        	   Paragraph titulod = new Paragraph(salon.getNombre(),titulodFont);
        	   Paragraph titulot = new Paragraph("Servicios: ",titulotFont);
        	   
        	
            // Añadir información del salón al PDF
        
            document.add(new Paragraph(titulod));
            document.add(new Paragraph("Precio: " + salon.getPrecio()));
            document.add(new Paragraph("Disponibilidad: " + salon.getDisponibilidad()));
            document.add(new Paragraph("Capacidad: " + salon.getCapacidad()));
            document.add(new Paragraph("Ciudad: " + salon.getUbicacion()));
            document.add(new Paragraph("Localidad:  " + salon.getBarrio()));
            document.add(new Paragraph("Direccion: " + salon.getDireccion()));
            
            document.add(new Paragraph(titulot));
            document.add(Chunk.NEWLINE);
            for (Elementos elemento : salon.getElementos()) {
                document.add(new Paragraph(elemento.getNombre()));
            }
            document.add(Chunk.NEWLINE);
            // Aquí debes manejar la imagen de manera especial, ya que no se puede agregar directamente como texto.
            // Puedes convertirla a un formato admitido por iText y agregarla al PDF.
            if (salon.getImagen() != null && salon.getImagen().length > 0) {
               
                try {
                    Image imagen = convertirBytesAImagen(salon.getImagen(),120,100);
                    document.add(imagen);
                    // Resto del código...
                } catch (IOException e) {
                    e.printStackTrace(); // O maneja la excepción de acuerdo a tus necesidades
                }
            }

            document.add(Chunk.NEWLINE);
           
            document.add(new LineSeparator());
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
        }

        document.close();

        return baos.toByteArray();
    }
    
    private Image convertirBytesAImagen(byte[] imagenBytes,float width, float height) throws IOException, BadElementException {
        // Convertir bytes a cadena Base64
        String imagenBase64 = Base64.encodeBase64String(imagenBytes);
        // Crear una imagen a partir de la cadena Base64
        Image imagen = Image.getInstance(Base64.decodeBase64(imagenBase64));
        imagen.scaleAbsolute(width, height);
        return imagen;
    }
    
    
    
    public byte[] generaterPdf(List<Reserva> reservas, Authentication authentication) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMM yyyy");
        String userEmail = "N/A";

	      if (authentication != null) {
	          if (authentication instanceof OAuth2AuthenticationToken) {
	              OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	              userEmail = oauthToken.getPrincipal().getAttribute("email");
	          } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
	              UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
	              userEmail = auth.getName();
	          }
	      }

     // Crear un estilo personalizado para el título
        Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLUE);
        Font titulodFont = new Font(Font.HELVETICA, 14, Font.NORMAL, Color.BLUE);
        Font titulotFont = new Font(Font.HELVETICA, 14, Font.NORMAL, Color.GREEN);
        Font tituloaFont = new Font(Font.HELVETICA, 15, Font.BOLD, Color.black);
        document.open();
        Paragraph titulo = new Paragraph("Informe de Reservas:", tituloFont);
        Paragraph tituloa = new Paragraph("Administrador:  ",tituloaFont);
        document.add(new Paragraph(titulo));
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(tituloa);
    	document.add(new Paragraph(userEmail));
    	document.add(Chunk.NEWLINE);
    	document.add(Chunk.NEWLINE);
        
        
        // Agregar el nombre del usuario al PDF
   
        for (Reserva reserva : reservas) {
        	   Paragraph titulod = new Paragraph(reserva.getId(),titulodFont);
        	   Paragraph titulot = new Paragraph("Salon: ",titulotFont);
        	   
        	
            // Añadir información del salón al PDF
        
            document.add(new Paragraph("Codigo de reserva: "+titulod));
            document.add(new Paragraph("Nombre de quien reserva: " + reserva.getNombre()));
            document.add(new Paragraph("Apellidos: " + reserva.getApellido()));
            document.add(new Paragraph("Numero celular: " + reserva.getCelular()));
            document.add(new Paragraph("Email de la persona quien reserva: " + reserva.getEmail()));
            document.add(new Paragraph("Fecha de reserva:  " + formatoFecha.format(reserva.getFecha())));
            document.add(new Paragraph("Nombre salon de reserva:  " + reserva.getSalon().getNombre()));
            document.add(new Paragraph("Codigo salon de reserva:  " + reserva.getSalon().getId()));
            
           
            /*
            document.add(new Paragraph(titulot));
            document.add(Chunk.NEWLINE);
            for (Salon salon : reserva.getSalon()) {
                document.add(new Paragraph("Nombre:  "+salon.getNombre()));
                document.add(new Paragraph("Codigo sala:  "+salon.getId()));
            }
            document.add(Chunk.NEWLINE);
           
           */
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(new LineSeparator());
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
        }

        document.close();

        return baos.toByteArray();
    }
    
    
    
    
    
    
}

