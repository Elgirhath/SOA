package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import db.DatabaseManager;

@Stateless
@WebService(name = "exam_service")
public class ExamService {

        @Inject
        DatabaseManager dbManager;

        @WebMethod
        public long push(String data) {
                dbManager.saveStartTime(new Date());
                return 1;
        }

        @WebMethod
        public boolean check(long id) {
                return true;
        }

        private void action(long id) {
                try {
                        TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
}