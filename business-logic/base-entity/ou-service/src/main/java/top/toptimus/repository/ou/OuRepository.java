package top.toptimus.repository.ou;

import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitAttributeDao;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
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
            throw new RuntimeException("保存ou失败");
        }
    }

    /**
     * 保存业务组织属性
     *
     * @param ouId                     ou id
     * @param orgnazitionUnitAttribute 业务组织属性
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrgnazitionUnitAttribute(String ouId, OrgnazitionUnitAttribute orgnazitionUnitAttribute) {
        String sql = "INSERT INTO orgnazition_unit_attribute(ou_id,indicator_type,parent_id,is_cu) "
                + "VALUES ('" + ouId + "','"
                + orgnazitionUnitAttribute.getIndicatorType().name() + "','"
                + orgnazitionUnitAttribute.getParentId() + "','"
                + orgnazitionUnitAttribute.isCU() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存ou失败");
        }
    }

    /**
     * 查询所有ou
     *
     * @return fkey信息
     */
    @Transactional(readOnly = true)
    public List<OrgnazitionUnitDao> findAllOrgnazitionUnitDao() {
        String sql = "SELECT ou_id,ou_code,ou_name,create_date,create_user,enable_date,p_ou_id,level,is_disabled,disable_date,disable_user,description,is_entity from orgnazition_unit;";
        return jdbcTemplate.query(sql, new OrgnazitionUnitDaoRowMapper());
    }

    /**
     * 更新业务组织基础信息
     *
     * @param orgnazitionUnitDao 业务组织dao
     */
    @Transactional
    public void updateOrgnazitionUnitBaseInfo(OrgnazitionUnitDao orgnazitionUnitDao) {
        String sql = "update orgnazition_unit\n" +
                "set \n" +
                (Strings.isNullOrEmpty(orgnazitionUnitDao.getOuCode()) ? "" : "ou_code = '" + orgnazitionUnitDao.getOuCode() + "'\n") +
                (Strings.isNullOrEmpty(orgnazitionUnitDao.getOuName()) ? "" : "ou_name = '" + orgnazitionUnitDao.getOuName() + "'\n") +
                (Strings.isNullOrEmpty(orgnazitionUnitDao.getPOuID()) ? "" : "p_ou_id = '" + orgnazitionUnitDao.getPOuID() + "'\n") +
                "level = '" + orgnazitionUnitDao.getLevel() + "'\n" +
                (Strings.isNullOrEmpty(orgnazitionUnitDao.getDescription()) ? "" : "description = '" + orgnazitionUnitDao.getDescription() + "'\n") +
                orgnazitionUnitDao.isEntity() + "'\n" +
                "where ou_id= '" + orgnazitionUnitDao.getOuID() + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新ou失败");
        }
    }

    /**
     * 查询所有ou
     *
     * @return fkey信息
     */
    @Transactional(readOnly = true)
    public List<OrgnazitionUnitAttributeDao> findAllOrgnazitionUnitAttributeDao() {
        String sql = "SELECT ou_id,indicator_type,parent_id,is_cu from orgnazition_unit_attribute;";
        return jdbcTemplate.query(sql, new OrgnazitionUnitAttributeDaoRowMapper());
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

class OrgnazitionUnitAttributeDaoRowMapper implements RowMapper<OrgnazitionUnitAttributeDao> {
    @Override
    public OrgnazitionUnitAttributeDao mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new OrgnazitionUnitAttributeDao(
                rs.getString("ou_id")
                , rs.getString("parent_id")
                , IndicatorType.valueOf(rs.getString("indicator_type"))
                , rs.getBoolean("is_cu")
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