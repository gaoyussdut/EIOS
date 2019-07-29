package top.toptimus.rule.formula;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 源单meta和fkey
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetaAndFkeyDTO implements Serializable {

    private static final long serialVersionUID = 1112874835653163467L;

    private String metaId;
    private String key;
}
