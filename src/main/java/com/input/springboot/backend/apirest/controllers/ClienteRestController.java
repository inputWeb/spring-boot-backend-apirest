//Subida al gitKraken

package com.input.springboot.backend.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.validation.Valid;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.input.springboot.backend.apirest.models.entity.Cliente;
import com.input.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins={"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;						//implementamos los metodos de la interfaz
	
	@GetMapping("/clientes")
	public List<Cliente> index(){								//devuelve una lista de objetos
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page){	
		Pageable pageable = PageRequest.of(page, 4);
		return clienteService.findAll(pageable);
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping("/clientes/{id}")								//devuelve un unico objeto
	public ResponseEntity<?> show(@PathVariable Long id){		//se reponseentity es el objeto que se devuelve puede ser un cliente o un mensaje de error
		
		Cliente cliente = null;									
		Map<String, Object> response = new HashMap<>();			//Map para devolver una respuesta de error
		
		try {
			cliente = clienteService.findById(id);
			if(cliente == null) {						
				response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));   //string con response de error
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);								
			}
			return new ResponseEntity<Cliente>(cliente, HttpStatus.OK); 
			
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al buscar");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/clientes")									//añadir un objeto con metodo post			
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result){		//parsear el objeto JSON que se le pasa
		
		Map<String, Object> response = new HashMap<>();
		Cliente clienteNew = null;
		
		if(result.hasErrors()) {	
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo" + err.getField() + " " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			clienteNew = clienteService.save(cliente);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al crear");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido creado correctamente!");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED); 		//status 201 de HTTP	
	}
	
	@Secured({"ROLE_ADMIN"})
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {  //parsea los datos del cliente y busca el cliente por id
		
		Map<String, Object> response = new HashMap<>();
		Cliente clienteUpdate = null;
		
		clienteUpdate = clienteService.findById(id);					  //devuelve el cliente a modificar
		
		if(result.hasErrors()) {	
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo" + err.getField() + " " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(clienteUpdate == null) {
			response.put("mensaje", "Error no se puede editar, el cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));   //string con response de error
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteUpdate.setApellido(cliente.getApellido());                         //se modifican los datos del cliente con los pasados por parametro
			clienteUpdate.setNombre(cliente.getNombre());
			clienteUpdate.setEmail(cliente.getEmail());
				
			clienteService.save(clienteUpdate);								  //guardar o updatear en la BD
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido modificado correctamente!");
		response.put("cliente", clienteUpdate);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED); 		//status 201 de HTTP	
	}
	
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("clientes/{id}")												  //Eliminar un cliente
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		Cliente clienteDelete = null;
		
		clienteDelete = clienteService.findById(id);
		if(clienteDelete == null) {
			response.put("mensaje", "Error no se puede borrar, el cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));   //string con response de error
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}	
		
		try {
			Cliente cliente = clienteService.findById(id);
			String nombreFotoAnterior = cliente.getFoto();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() >0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			clienteService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al Borrar");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido borrado correctamente!");
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK); 		//status 204 de HTTP
	}
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		
		Map<String, Object> response = new HashMap<>();
		Cliente cliente = clienteService.findById(id);
		
		if(!archivo.isEmpty()) {
			String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");	  //nombre del archivo
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();	//ruta relativa + el nombre del archivo y lo guardamos como ruta absoluta
			
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = cliente.getFoto();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() >0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			cliente.setFoto(nombreArchivo);
			clienteService.save(cliente);
			
			response.put("cliente", cliente);
			response.put("mensaje", "has subido correctamente la imagen: " + nombreArchivo);
			
		}
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		Resource recurso = null;
		
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeErrorException(null, "No se pudo cargar la imagen: " + nombreFoto);
		}
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"" );
		
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}
	
}

