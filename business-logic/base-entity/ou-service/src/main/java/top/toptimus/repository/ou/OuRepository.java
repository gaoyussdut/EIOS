package top.toptimus.repository.ou;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitBaseInfoDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * ou repo
 *
 * @author gaoyu
 * @since 2019-08-14
 */
@Repository
public class OuRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 保存ou定义
     *
     * @param orgnazitionUnitDao OU定义,包含所有业务组织属性
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void createorgnazitionUnit(OrgnazitionUnitDao orgnazitionUnitDao) {
        String sql = "INSERT INTO orgnazition_unit(ou_id,ou_code,ou_name,create_date,create_user,p_ou_id,level,is_disabled,disable_user,description,is_entity) " +
                "VALUES ('" + orgnazitionUnitDao.getOuID() + "','"
                + orgnazitionUnitDao.getOuCode() + "','"
                + orgnazitionUnitDao.getOuName() + "','"
                + orgnazitionUnitDao.getCreateDate() + "','"
                + orgnazitionUnitDao.getCreateUser() + "','"
                + orgnazitionUnitDao.getPOuID() + "','"
                + orgnazitionUnitDao.getLevel() + "','"
                + orgnazitionUnitDao.isDisabled() + "','"
                + orgnazitionUnitDao.getDisableUser() + "','"
                + orgnazitionUnitDao.getDescription() + "','"
                + orgnazitionUnitDao.isEntity() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyDao失败");
        }
    }

    /**
     * 查询所有ou
     *
     * @return fkey信息
     */
    @Transactional(readOnly = true)
    public List<OrgnazitionUnitDao> findAll() {
        String sql = "SELECT ou_id,ou_code,ou_name,create_date,create_user,enable_date,p_ou_id,level,is_disabled,disable_date,disable_user,description,is_entity from orgnazition_unit";
        return jdbcTemplate.query(sql, new OrgnazitionUnitDaoRowMapper());
    }
}

class OrgnazitionUnitDaoRowMapper implements RowMapper<OrgnazitionUnitDao> {
    @Override
    public OrgnazitionUnitDao mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new OrgnazitionUnitDao(
                new OrgnazitionUnitBaseInfoDto(
                        rs.getString("ou_id")
                        , rs.getString("ou_code")
                        , rs.getString("ou_name")
                        , rs.getDate("create_date")
                        , rs.getString("create_user")
                        , rs.getDate("enable_date")
                        , rs.getString("p_ou_id")
                        , rs.getInt("level")
                        , rs.getBoolean("is_disabled")
                        , rs.getDate("disable_date")
                        , rs.getString("disable_user")
                        , rs.getString("description")
                        , rs.getBoolean("is_entity")
                )
        );
    }
}

class OrgnazitionUnitBaseInfoDtoRowMapper implements RowMapper<OrgnazitionUnitBaseInfoDto> {
    @Override
    public OrgnazitionUnitBaseInfoDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new OrgnazitionUnitBaseInfoDto(
                rs.getString("ou_id")
                , rs.getString("ou_code")
                , rs.getString("ou_name")
                , rs.getDate("create_date")
                , rs.getString("create_user")
                , rs.getDate("enable_date")
                , rs.getString("p_ou_id")
                , rs.getInt("level")
                , rs.getBoolean("is_disabled")
                , rs.getDate("disable_date")
                , rs.getString("disable_user")
                , rs.getString("description")
                , rs.getBoolean("is_entity")
        );
    }
}