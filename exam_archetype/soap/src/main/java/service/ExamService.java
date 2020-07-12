package service;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import db.DatabaseManager;
import entity.Message;
import jms.QueueSender;
import org.jboss.annotation.security.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

@SecurityDomain("exam-security-domain")
@DeclareRoles("exam_group")
@Stateless
@WebService(name = "exam_service")
@WebContext(authMethod = "BASIC")
public class ExamService {

    @Inject
    DatabaseManager dbManager;

    @Inject
    QueueSender queueSender;

    @PermitAll
    @WebMethod
    public long push(String data) {
        long id = dbManager.saveData(data);
        queueSender.callAction(id);
        return id;
    }

    @RolesAllowed("exam_group")
    @WebMethod
    public boolean check(long id) {
        Message msg = dbManager.getMessage(id);
        return msg != null && msg.getEndTime() != null;
    }
}