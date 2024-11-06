package models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="usuarios")
@NamedQueries({
    @NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
    @NamedQuery(name="Usuario.findById", query="SELECT u FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name="Usuario.findByEmail", query="SELECT u FROM Usuario u WHERE u.email = :email"),
    @NamedQuery(name="Usuario.findByEmailAndPassword", query="SELECT u FROM Usuario u WHERE u.email = :email and u.password = :password"),
})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String password;
    @OneToMany(mappedBy="owner", cascade = CascadeType.PERSIST)
    private List<Producto> productos;

    public Usuario() {
    }

    public Usuario(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }    

    public Usuario(String name, String email, String password, List<Producto> productos) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.productos = productos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario[ id=" + id + " ]";
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
    
    public int getSizeOfProductos(){
        return productos.size();
    }
    
}
