package com.input.springboot.backend.apirest.models.entity; 
 
import java.io.Serializable; 
import java.util.List; 
 
import javax.persistence.Column; 
import javax.persistence.Entity; 
import javax.persistence.GeneratedValue; 
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.ManyToMany; 
import javax.persistence.Table; 
 
@Entity 
@Table(name="role") 
public class Role implements Serializable { 
 
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id; 
	 
	@Column(unique=true, length=20) 
	private String nombre; 
	 
	/*Para hacer la relacion a la inversa solo hay que llamar a  
	 la propiedad que hemos creado en la otra relacion 'roles' 
	 @ManyToMany(mappedBy="roles") 
	private List<Usuario> usuarios; 
	Generar Getter and Setters*/ 
 
	public Long getId() { 
		return id; 
	} 
 
	public void setId(Long id) { 
		this.id = id; 
	} 
 
	public String getNombre() { 
		return nombre; 
	} 
 
	public void setNombre(String nombre) { 
		this.nombre = nombre; 
	} 
 
	private static final long serialVersionUID = 1L; 
} 
