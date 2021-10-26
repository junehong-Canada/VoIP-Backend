package skychatuc.io.proxyserver.sip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriber",
    indexes = {
        @Index(name = "account_idx", columnList = "username, domain", unique = true),
            @Index(name = "username_idx", columnList = "username")
    })
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    The IDENTITY strategy relies on the database auto-increment column.
//    The database generates the primary key after each insert operation.
//    JPA assigns the primary key value after performing the insert operation or upon transaction commit:
    private long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "ha1", nullable = false)
    private String ha1;

    @Column(name = "ha1b", nullable = false)
    private String ha1b;

    // ALTER TABLE subscriber ADD fcmId varchar(255) NOT NULL AFTER ha1b;
    // ALTER TABLE subscriber CHANGE COLUMN fcmId fcm_id varchar(255) NOT NULL;
    @Column(name = "fcmId", nullable = false)   // fcm_id
    private String fcmId;
}
