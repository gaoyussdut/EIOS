package top.toptimus.entity.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.secutiry.AuthRepository;

@Component
public class AuthQueryFacadeEntity {

    @Autowired
    private AuthRepository authRepository;


    /**
     * 判定用户Id的UserTask的权限
     *
     * @param userId     user id
     * @param userTaskId user task id
     * @return 判断是否有权限
     */
    public boolean checkUserTaskIdAuth(String userId, String userTaskId) {
        return authRepository.checkUserTaskIdAuth(userId, userTaskId);
    }
}
