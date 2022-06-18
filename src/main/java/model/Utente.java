package model;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Utente {

    private String name;
    private String surname;
    private Long punti;
}
