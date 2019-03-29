package com.github.mqtask.runner;

import com.github.mqtask.constant.MqtaskOperTypeEnum;
import com.github.mqtask.service.MqtaskService;
import com.github.mqtask.util.CommonUtils;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

@Component
public class MqtaskCommandRunner implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(MqtaskCommandRunner.class);

    @Autowired
    private MqtaskService mqtaskService;

    private String filePath;

    private String operation;

    private Options options = new Options() {{
        this.addOption("f", "file", true, "配置文件路径");
        this.addOption("o", "operation", true, "操作类型 start, stop");
        this.addOption("h", "help", false, "显示帮助");
    }};

    private void parseCommandLine(String[] args) {
        try {
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(this.options, args);
            if (!commandLine.hasOption("h") && !commandLine.hasOption("f") && !commandLine.hasOption("o")) {
                printHelp();
                return;
            }

            if (commandLine.hasOption("h")) {
                printHelp();
                return;
            }

            if (commandLine.hasOption("f")) {
                this.filePath = commandLine.getOptionValue("f");
                logger.info("filePath=" + filePath);
            }

            if (commandLine.hasOption("o")) {
                this.operation = commandLine.getOptionValue("o");
                logger.info("operation=" + this.operation);
            }

            executeCommand();

        } catch (Exception e) {
            CommonUtils.logError(logger, e);
        }
    }

    private void executeCommand() {
        if (StringUtils.isEmpty(this.filePath) || StringUtils.isEmpty(operation)) {
            logger.error("Missing options: f|o");
            return;
        }

        File file = new File(this.filePath);
        if (!file.exists()) {
            logger.error("配置文件不存在,请重新选择");
            return;
        }

        if (!MqtaskOperTypeEnum.STOP.toString().equalsIgnoreCase(this.operation)
                && !MqtaskOperTypeEnum.START.toString().equalsIgnoreCase(this.operation)) {
            logger.error("操作类型有误，请重新填写");
            return;
        }

        boolean result = false;
        if (MqtaskOperTypeEnum.START.toString().equalsIgnoreCase(this.operation)) {
            result = this.mqtaskService.startQueues(this.filePath);
        } else {
            result = this.mqtaskService.stopQueues(this.filePath);
        }

        System.out.println(this.operation + " operation [" + (result ? "SUCCESS" : "FAIL") + "]");
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("mqtask [OPTIONS]", options, false);
    }

    @Override
    public void run(String... args) throws Exception {
        parseCommandLine(args);
    }
}
