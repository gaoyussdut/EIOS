package top.toptimus.entity.processIns.aop;

import com.google.common.collect.Lists;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.entity.processDefinition.query.ProcessQueryFacadeEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenDataSqlRetrieveEntity;
import top.toptimus.processDefinition.UserTaskDto;
import top.toptimus.processModel.UserTaskPoInfoModel;
import top.toptimus.repository.processIns.UserTaskInsRepository;
import top.toptimus.token.TokenDto;

import java.util.List;

@Aspect
@Component
public class UserTaskPoInfoAop {
    @Autowired
    private UserTaskInsRepository userTaskInsRepository;
    @Autowired
    private ProcessQueryFacadeEntity processQueryFacadeEntity;
    @Autowired
    private TokenDataSqlRetrieveEntity tokenDataSqlRetrieveEntity;
    @Autowired
    private TokenEventEntity tokenEventEntity;

    /**
     * 实例化流程节点的任务
     *
     * @param retValue 实例化流程节点的任务model
     */
    @AfterReturning(returning = "retValue", pointcut = "execution(" + "public * top.toptimus.entity.processIns.event.ProcessInsEventEntity.insertProcessIns(..))")
    public void generateUserTaskPoInfo(Object retValue) {
        UserTaskPoInfoModel userTaskPoInfoModel = (UserTaskPoInfoModel) retValue;
        // 持久化
        userTaskInsRepository.saveAll(userTaskPoInfoModel.getUserTaskInsDTOS());

        // 将t_user_task_po_config 数据实例化到t_user_task_po_ins里面
        // 1.根据其中一个UserTaskId查找其流程所有相关的UserTaskId
        List<UserTaskDto> userTaskDtos = processQueryFacadeEntity.findAllByUserTaskId(userTaskPoInfoModel.getUserTaskId());
        // 2.查找
        for (UserTaskDto userTaskDto : userTaskDtos) {

            String tableName = tokenDataSqlRetrieveEntity.getTableName(userTaskDto.getMetaId());
            if (userTaskDto.getUserTaskId().equals(userTaskPoInfoModel.getUserTaskId())) {
                //判断该条token是否已经存入
                if (tokenDataSqlRetrieveEntity.getExistTokenIds(tableName, Lists.newArrayList(userTaskPoInfoModel.getBillTokenId())).size() == 0) {
                    //token
                    tokenEventEntity.insertTokenId(new TokenDto().build(tableName, userTaskPoInfoModel.getBillTokenId()));
                }
            }
        }

    }

}
