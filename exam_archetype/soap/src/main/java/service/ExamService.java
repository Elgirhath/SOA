package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import db.DatabaseManager;
import entity.Message;
import jms.QueueSender;

@Stateless
@WebService(name = "exam_service")
public class ExamService {

    @Inject
    DatabaseManager dbManager;

    @Inject
    QueueSender queueSender;

    @WebMethod
    public long push(String data) {
        long id = dbManager.saveData(data);
        queueSender.callAction(id);
        return id;
    }

    @WebMethod
    public boolean check(long id) {
        Message msg = dbManager.getMessage(id);
        return msg != null && msg.getEndTime() != null;
    }
}