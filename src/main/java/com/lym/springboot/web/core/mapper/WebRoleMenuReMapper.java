package com.lym.springboot.web.core.mapper;

import com.lym.springboot.web.core.domain.WebRoleMenuRe;
import com.lym.springboot.web.core.domain.WebRoleMenuReExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebRoleMenuReMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    long countByExample(WebRoleMenuReExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int deleteByExample(WebRoleMenuReExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int insert(WebRoleMenuRe record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int insertSelective(WebRoleMenuRe record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    WebRoleMenuRe selectOneByExample(WebRoleMenuReExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    WebRoleMenuRe selectOneByExampleSelective(@Param("example") WebRoleMenuReExample example, @Param("selective") WebRoleMenuRe.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    List<WebRoleMenuRe> selectByExampleSelective(@Param("example") WebRoleMenuReExample example, @Param("selective") WebRoleMenuRe.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    List<WebRoleMenuRe> selectByExample(WebRoleMenuReExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    WebRoleMenuRe selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") WebRoleMenuRe.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    WebRoleMenuRe selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") WebRoleMenuRe record, @Param("example") WebRoleMenuReExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") WebRoleMenuRe record, @Param("example") WebRoleMenuReExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WebRoleMenuRe record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table web_role_menu_re
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(WebRoleMenuRe record);

    /**
     * 批量保存
     * @param roleMenuRes
     * @return
     */
    int insertBatch(@Param("roleMenuRes") List<WebRoleMenuRe> roleMenuRes);
}