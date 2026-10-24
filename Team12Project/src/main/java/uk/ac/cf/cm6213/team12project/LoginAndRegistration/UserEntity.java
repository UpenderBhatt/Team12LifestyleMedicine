package uk.ac.cf.cm6213.team12project.LoginAndRegistration;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.cf.cm6213.team12project.LoginAndRegistration.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "users")
@Table(indexes = {
        @Index(name = "index_users_username", columnList = "username", unique = true)
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name= "role_id", referencedColumnName = "id")}
    )

    private List<Role> roles =  new ArrayList<>();

    public UserEntity(){}

    public UserEntity(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }


}
