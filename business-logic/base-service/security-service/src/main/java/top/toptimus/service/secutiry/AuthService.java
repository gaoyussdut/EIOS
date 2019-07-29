package top.toptimus.service.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.entity.security.query.AuthQueryFacadeEntity;

@Service
public class AuthService {

    @Autowired
    private AuthQueryFacadeEntity authQueryFacadeEntity;

    /**
     * 判定用户Id的UserTask的权限
     *
     * @param userId     user id
     * @param userTaskId user task id
     * @return 判断是否有权限
     */
    public boolean checkUserTaskIdAuth(String userId, String userTaskId) {
        return authQueryFacadeEntity.checkUserTaskIdAuth(userId, userTaskId);
    }
}
