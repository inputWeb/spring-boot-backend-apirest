package com.input.springboot.backend.apirest.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.input.springboot.backend.apirest.models.entity.Usuario;
import com.input.springboot.backend.apirest.models.services.IUsuarioService;

@Component
public class infoAdicionalToken implements TokenEnhancer{
	
	@Autowired
	private IUsuarioService usuarioService;		//implementamos usuarioService

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {		//añadir mas info al token de acceso
		
			
		Usuario usuario = usuarioService.findByUsername(authentication.getName());				//con el user de acceso podemos obtener los el usuario original de la BD y realizar consultas
		Map<String, Object> info = new HashMap<>();
		//info.put("info_adicional", "username".concat(authentication.getName()));		//con el hash map añadimos el nombre del  usuario
		info.put("nombre", usuario.getNombre());				//obetenemos el nombre del usuario de la BD
		info.put("apellido", usuario.getApellido());					//obtenemos el id del user en la BD
		info.put("email", usuario.getEmail());	
		
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}

}
