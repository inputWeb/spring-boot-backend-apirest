package com.input.springboot.backend.apirest.models.entity; 
 
import java.io.Serializable; //permite convertir el objeto de java a JSON y en una sesion HTTP 
import java.util.List; 
 
import javax.persistence.CascadeType; 
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.FetchType; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.JoinColumn; 
import javax.persistence.JoinTable; 
import javax.persistence.ManyToMany; 
import javax.persistence.Table;
import javax.persistence.UniqueConstraint; 
 
@Entity //Clase mapeada a una tabla de la BD 
@Table(name="usuarios") //Nombre de la tabla que se crea en la BD por defecto pone el nombre de la clase 
public class Usuario implements Serializable{ 
	 
	@Id													//identifica este campo como la PK 
	@GeneratedValue(strategy=GenerationType.IDENTITY)  //Como se genera la clave primaria por defecto 
	private Long id; 
	 
	@Column(unique=true, length=20)	//carateristicas del campo, longitud, unico etc... 
	private String username; 
	 
	@Column(unique=true, length=60) 
	private String password; 
	 
	private Boolean enabled; 
	 
	//Relacion entre tablas y borrado en cascada 
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="usuarios_roles", joinColumns= @JoinColumn(name="usuario_id"),
	inverseJoinColumns=@JoinColumn(name="role_id"),
	uniqueConstraints= {@UniqueConstraint(columnNames= {"usuario_id", "role_id"})})
	private List<Role> roles; //esto es para cambiar el nombre de la tabla y establecer las relaciones del proyectos
	
	private String nombre;
	private String apellido;
	
	@Column(unique=true)
	private String email;
	 
	public Long getId() { 
		return id; 
	} 
 
	public void setId(Long id) { 
		this.id = id; 
	} 
 
	public String getUsername() { 
		return username; 
	} 
 
	public void setUsername(String username) { 
		this.username = username; 
	} 
 
	public String getPassword() { 
		return password; 
	} 
 
	public void setPassword(String password) { 
		this.password = password; 
	} 
 
	public Boolean getEnabled() { 
		return enabled; 
	} 
 
	public void setEnabled(Boolean enabled) { 
		this.enabled = enabled; 
	} 
 
	public List<Role> getRoles() { 
		return roles; 
	} 
 
	public void setRoles(List<Role> roles) { 
		this.roles = roles; 
	} 

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	private static final long serialVersionUID = 1L; //Hay que implementarlo si esta la interfaz serializable 
} 
