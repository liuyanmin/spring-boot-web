package com.lym.springboot.web.core.mapper;

import com.lym.springboot.web.core.domain.WebPermission;
import com.lym.springboot.web.core.domain.WebPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebPermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    long countByExample(WebPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int deleteByExample(WebPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int insert(WebPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int insertSelective(WebPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    WebPermission selectOneByExample(WebPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    WebPermission selectOneByExampleSelective(@Param("example") WebPermissionExample example, @Param("selective") WebPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    List<WebPermission> selectByExampleSelective(@Param("example") WebPermissionExample example, @Param("selective") WebPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    List<WebPermission> selectByExample(WebPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    WebPermission selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") WebPermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    WebPermission selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    WebPermission selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") WebPermission record, @Param("example") WebPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") WebPermission record, @Param("example") WebPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WebPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(WebPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") WebPermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_permission
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}