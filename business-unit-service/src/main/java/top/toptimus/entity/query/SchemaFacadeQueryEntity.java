package top.toptimus.entity.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.SchemaRepository;
import top.toptimus.schema.SchemaDTO;

/**
 * businessunit query
 *
 * @author lzs
 * @since 2019-8-6
 */
@Component
public class SchemaFacadeQueryEntity {

    @Autowired
    private SchemaRepository schemaRepository;


    public SchemaDTO findSchemaById(String id) {
        return schemaRepository.findSchemaById(id);
    }
}
