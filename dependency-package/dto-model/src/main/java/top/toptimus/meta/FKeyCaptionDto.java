package top.toptimus.meta;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * meta 中 fkey的默认caption
 * Created by JiangHao on 2018/8/7.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FKeyCaptionDto implements Serializable {

    private static final long serialVersionUID = 6333893943223351046L;

    private String key;
    private String caption;

}
