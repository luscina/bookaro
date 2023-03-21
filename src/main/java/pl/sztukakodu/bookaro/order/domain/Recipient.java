package pl.sztukakodu.bookaro.order.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import pl.sztukakodu.bookaro.jpa.BaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
public class Recipient extends BaseEntity {

    String email;
    String name;
    String phone;
    String street;
    String city;
    String zipCode;

    public Recipient(String name, String phone, String street, String city, String zipCode, String email) {
        this.name = name;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.email = email;
    }
}
