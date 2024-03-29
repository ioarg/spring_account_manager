package jarg.templates.account_manager.security.users.entities;


import javax.persistence.*;

/**
 * A {@link User user's} authorities.
 */
@Entity
@Table(name="authorities")
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "roles")
    private String roles;                   // Comma-separated list of roles

    public Authorities(){
    }

    public Authorities(String roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "id=" + id +
                ", roles='" + roles + '\'' +
                '}';
    }
}
