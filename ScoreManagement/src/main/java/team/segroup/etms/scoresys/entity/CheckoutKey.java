package team.segroup.etms.scoresys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer atid;
    private String  nid;
}
