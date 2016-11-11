package com.qzt360.service;

import com.google.common.base.Splitter;
import com.qzt360.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by zhaogj on 04/11/2016.
 */
@Service
public class EmailService {

    @Value("${system.strManagerRecipients}")
    private String strManagerRecipients;

    private Splitter splitter = Splitter.on(",").trimResults();

    public void sendEmail2Manager(String strSubject, String strText) {
        EmailUtil.sendSimpleTextEmail(splitter.split(strManagerRecipients), strSubject, strText);
    }
}