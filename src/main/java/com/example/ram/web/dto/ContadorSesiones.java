package com.example.ram.web.dto;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.example.ram.model.User;
import com.example.ram.repository.UserRepository;

@Component
public class ContadorSesiones implements ApplicationListener<AuthenticationSuccessEvent> {

    
	private UserRepository usuarioService;

    public ContadorSesiones(UserRepository usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        User usuario = usuarioService.findByEmail(username);

        if (usuario != null) {
            usuario.setContadorSesiones(usuario.getContadorSesiones() + 1);
            usuarioService.save(usuario);
        }
    }
}
