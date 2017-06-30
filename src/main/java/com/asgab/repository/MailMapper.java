package com.asgab.repository;

import java.util.List;

import com.asgab.entity.Mail;
import com.asgab.entity.MailReceiver;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface MailMapper {
  void save(Mail mail);

  void update(Mail mail);

  List<Mail> getNotSendMails();

  Mail get(Long id);

  void saveMailReceiver(MailReceiver mailReceiver);

  List<MailReceiver> getMailReceiversByMailId(Long mailId);
}
