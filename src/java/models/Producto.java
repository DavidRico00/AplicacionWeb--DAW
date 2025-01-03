package models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="productos")
@NamedQueries({
    @NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p"),
    @NamedQuery(name="Producto.findByWord", query="SELECT p FROM Producto p WHERE p.nombre LIKE :palabra"),
})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String rutaImg;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario owner;
    @OneToMany(mappedBy="product", cascade = CascadeType.PERSIST)
    private List<Comentario> comentarios;

    public Producto() {
    }
    
    public Producto(String nombre, String descripcion, String rutaImg, Usuario owner) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutaImg = rutaImg;
        this.owner = owner;
    }

    public Producto(String nombre,String descripcion, String rutaImg, Usuario owner, List<Comentario> comentarios) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutaImg = rutaImg;
        this.owner = owner;
        this.comentarios = comentarios;
    }  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Productor[ id=" + id + " ]";
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaImg() {
        return rutaImg;
    }

    public void setRutaImg(String rutaImg) {
        this.rutaImg = rutaImg;
    }

    public Usuario getOwner() {
        return owner;
    }

    public void setOwner(Usuario owner) {
        this.owner = owner;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

}
