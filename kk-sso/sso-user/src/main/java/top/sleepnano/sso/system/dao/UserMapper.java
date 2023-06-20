package top.sleepnano.sso.system.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.sleepnano.sso.system.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     *  基于用户名获取用户信息
     * @param username 用户名
     * @return 用户对象
     */
    @Select("Select id,user_name,user_pass,user_addr,user_phone,user_no,create_time,modify_time " +
            "from user_list where user_name = #{username}")
    @DS("master")
    User selectUserByUsername(String username);

    /**
     * 基于用户id查询用户权限
     * @param userNo 用户id
     * @return 用户的权限
     * 涉及到的表:tb_user_roles,tb_role_menus,tb_menus
     */
    @Select("SELECT" +
            "    DISTINCT m.`perms`" +
            "FROM" +
            "    sys_user_role ur" +
            "        LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`" +
            "        LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`" +
            "        LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`" +
            "WHERE" +
            "        user_id = #{userNo}" +
            "  AND r.`status` = 0" +
            "  AND m.`status` = 0")
    List<String> selectUserPermissions(String userNo);
}
