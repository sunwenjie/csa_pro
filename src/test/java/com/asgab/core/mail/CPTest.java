package com.asgab.core.mail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.asgab.web.AutoEmailNotification;

public class CPTest {

  @Test
  public void testMailTemplate() {
    String path = "/tmp/3ae97080-963b-42dc-918d-b21b551dde57/1.docx";
    List<String> list = new ArrayList<String>();
    list.add(path);
    AutoEmailNotification.rmTmpFile(list);
  }
}
