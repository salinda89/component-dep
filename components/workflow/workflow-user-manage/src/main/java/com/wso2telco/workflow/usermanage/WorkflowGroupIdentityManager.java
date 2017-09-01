package com.wso2telco.workflow.usermanage;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.config.RealmConfiguration;
import org.wso2.carbon.user.core.service.RealmService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkflowGroupIdentityManager extends GroupEntityManager {

    private static Log log = LogFactory.getLog(WorkflowGroupIdentityManager.class);

    private UserStoreManager userStoreManager;

    public WorkflowGroupIdentityManager() {
        try {
            RealmConfiguration config = new RealmConfiguration();
            userStoreManager = ServicesHolder.getInstance().getRealmService().getUserRealm(config).getUserStoreManager();
        } catch (UserStoreException e) {
            String errorMsg = "Error while initiating UserStoreManager";
            log.error(errorMsg, e);
        }
    }

    @Override
    public Group createNewGroup(String groupId) {
        String msg = "Invoked GroupIdentityManager method is not supported by WorkflowGroupIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void insertGroup(Group group) {
        String msg = "Invoked GroupIdentityManager method is not supported by WorkflowGroupIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void deleteGroup(String groupId) {
        String msg = "Invoked GroupIdentityManager method is not supported by WorkflowGroupIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        return new GroupQueryImpl(getProcessEngineConfiguration().getCommandExecutor());
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        String userId = query.getUserId();
        List<Group> groupList = new ArrayList<Group>();
        try {
            String[] roleList = userStoreManager.getRoleListOfUser(userId);
            for (int i = 0; i < roleList.length; i++) {
                GroupEntity group = new GroupEntity(roleList[i]);
                group.setType(roleList[i]);
                groupList.add(group);
            }
        } catch (UserStoreException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        if (null != query.getUserId()) {
            return findGroupsByUser(query.getUserId()).size();
        } else {
            String msg = "Invoked GroupIdentityManager method is not supported by WorkflowGroupIdentityManager.";
            throw new UnsupportedOperationException(msg);
        }
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<Group> groups = new ArrayList<Group>();
        try {
            String[] roles = userStoreManager.getRoleListOfUser(userId);
            for (String role : roles) {
                Group group = new GroupEntity(role);
                groups.add(group);
            }
        } catch (Exception e) {
            String msg = "Failed to get roles of the user: " + userId + ". Returning an empty roles list.";
            log.error(msg, e);
        }
        return groups;
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult,
                                               int maxResults) {
        String msg = "Invoked GroupIdentityManager method is not supported by WorkflowGroupIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        String msg = "Invoked GroupIdentityManager method is not supported by WorkflowGroupIdentityManager.";
        throw new UnsupportedOperationException(msg);
    }
}
