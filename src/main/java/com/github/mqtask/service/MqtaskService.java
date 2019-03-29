package com.github.mqtask.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.mqtask.constant.MqtaskOperTypeEnum;
import com.github.mqtask.util.CommonUtils;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Properties;

@Service
public class MqtaskService {

    private static final Log logger = LogFactory.getLog(MqtaskService.class);

    public boolean stopQueues(String configFile) {
        boolean result = false;
        try {
            operationQueue(configFile, MqtaskOperTypeEnum.STOP);
            result = true;
        } catch (Exception e) {
            CommonUtils.logError(logger, e);
            result = false;
        }

        return result;
    }

    public boolean startQueues(String configFile) {
        boolean result = false;
        try {
            operationQueue(configFile, MqtaskOperTypeEnum.START);
            result = true;
        } catch (Exception e) {
            CommonUtils.logError(logger, e);
            result = false;
        }

        return result;
    }

    private void operationQueue(String configFile, MqtaskOperTypeEnum mqtaskOperTypeEnum) throws Exception {
        File file = new File(configFile);
        String content = FileUtils.readFileToString(file, "UTF-8");
        logger.info("file content=" + content + "=");
        JSONArray jsonArray = JSON.parseArray(content);

        JSONObject tmpQueueManagerJson = null;
        JSONObject tmpQueueJson = null;
        MQQueueManager mqQueueManager = null;
        List<String> queueNames = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            tmpQueueManagerJson =  jsonArray.getJSONObject(i);
            mqQueueManager = getMQQueueManager(tmpQueueManagerJson);
            queueNames = tmpQueueManagerJson.getObject("queues", new TypeReference<List<String>>(){});
            logger.error("queueNames=" + queueNames);

            for (String qname : queueNames) {
                setMQQueueInhibitGet(getMQQueue(mqQueueManager, qname), mqtaskOperTypeEnum);
            }
            mqQueueManager.close();
        }
    }

    private void setMQQueueInhibitGet(MQQueue mqQueue, MqtaskOperTypeEnum mqtaskOperTypeEnum) throws MQException {
        if(MqtaskOperTypeEnum.STOP == mqtaskOperTypeEnum) {
            mqQueue.setInhibitGet(MQConstants.MQQA_GET_INHIBITED);
        } else {
            mqQueue.setInhibitGet(MQConstants.MQQA_GET_ALLOWED);
        }
        mqQueue.close();
    }

    private MQQueueManager getMQQueueManager(JSONObject jsonObject) throws MQException {
        Properties properties = new Properties();
        properties.put("hostname", jsonObject.getString("ip"));
        properties.put("port", jsonObject.getInteger("port"));
        properties.put("ccsid", jsonObject.getInteger("ccsid"));
        properties.put("channel", jsonObject.getString("channel"));
        MQQueueManager mqQueueManager = new MQQueueManager(jsonObject.getString("name"), properties);
        return mqQueueManager;
    }

    private MQQueue getMQQueue(MQQueueManager mqQueueManager, String name) throws MQException {
        return  mqQueueManager.accessQueue(name, MQConstants.MQOO_SET);
    }
}
